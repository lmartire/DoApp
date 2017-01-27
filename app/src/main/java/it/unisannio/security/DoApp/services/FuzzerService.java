package it.unisannio.security.DoApp.services;

import android.app.IntentService;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import it.unisannio.security.DoApp.GlobalClass;
import it.unisannio.security.DoApp.activities.CounterActivity;
import it.unisannio.security.DoApp.activities.CrashedListActivity;
import it.unisannio.security.DoApp.model.Triager;
import it.unisannio.security.DoApp.util.PackageInfoExtractor;
import it.unisannio.security.DoApp.util.ReportWriter;
import it.unisannio.security.DoApp.activities.EndActivity;
import it.unisannio.security.DoApp.generators.MalIntentGenerator;
import it.unisannio.security.DoApp.model.Commons;
import it.unisannio.security.DoApp.model.ExceptionReport;
import it.unisannio.security.DoApp.model.LogCatMessage;
import it.unisannio.security.DoApp.model.MalIntent;
import it.unisannio.security.DoApp.parser.LogCatMessageParser;
import it.unisannio.security.DoApp.parser.MessagesFilter;
import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.util.UnixCommands;

import com.jaredrummler.apkparser.model.AndroidComponent;
import com.jaredrummler.apkparser.model.IntentFilter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuzzerService extends IntentService {

    private String pathFile;

    //lista contenente i risultati
    private List<ExceptionReport> results = new ArrayList<ExceptionReport>();

    //lista contentente tutti i componenti che sono crashati
    private List<String> crashedComponents = new ArrayList<String>();

    //numero totale di componenti da testare
    private int totalComponents;

    public FuzzerService() {
        super("FuzzerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //estraggo il nome dell'app su cui fare fuzzing
            String pkgname = intent.getStringExtra(Commons.pkgName);
            if(pkgname!=null && !pkgname.isEmpty()) {

                Log.i("DoAppLOG", "Start fuzzing to "+pkgname);

                //pulisco il logcat
                UnixCommands.killAll("logcat");
                UnixCommands.clearLogCat();


                fuzz(pkgname);


                SystemClock.sleep(100);

                //avvio l'activity finale

                if(crashedComponents.size()>0) {

                    //condivido i reports con tutta l'applicazione
                    GlobalClass.reports = results;

                    //avvio l'activity per mostrare i risultati
                    Intent intentResult = new Intent(this, CrashedListActivity.class);
                    intentResult.putStringArrayListExtra("crashedComponents", (ArrayList<String>) crashedComponents);
                    intentResult.putExtra("numberCrashedComponents", crashedComponents.size());
                    intentResult.putExtra("numberTotalComponents", totalComponents);
                    intentResult.putExtra(Commons.pathFile, pathFile);
                    intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentResult);
                }
                else{
                    Intent end = new Intent(this, EndActivity.class);
                    end.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    end.putExtra(Commons.pathFile, pathFile);
                    startActivity(end);
                }
            }
        }
    }

    private void fuzz(String pkgname){

        Log.i("DoAppLOG", "Analizzo il manifest...");

        //recupero la lista dei datatype degli IntentFilter esportati dall'app
        PackageInfoExtractor extractor = new PackageInfoExtractor(this, pkgname);
        totalComponents = extractor.getNumberComponentWithIntentFilters();
        Log.i("DoAppLOG", "Componenti da testare: " + totalComponents);

        List<IntentDataInfo> datas = extractor.extractIntentFiltersDataType();


        Log.i("DoAppLOG", "Creo i MalIntent...");

        //ottengo la lista degli intent malevoli
        List<MalIntent> malIntents = MalIntentGenerator.createFromIntentData(datas);

        Log.i("DoAppLOG", "Creati " + malIntents.size()+" MalIntent:");

        //log per conoscere i malintent creati
        int counter = 1;
        for(MalIntent m : malIntents){
            Log.i("DoAppLOG", "\t "+ (counter++) + ". "+m.toString());
        }


        //usato come spareggio se il PID dell'app è uguale ad una Exception già analizzata
        Date lastTime = null;

        //invio uno alla volta i malintent
        int num=1;
        for(MalIntent malIntent : malIntents){


            Log.i("DoAppLOG", "Invio MalIntent n."+ num);
            Log.i("DoAppLOG", "\t"+ malIntent.toString());

            //invio l'intent malevolo
            try{
                switch (malIntent.getTargetComponent().type) {
                    case AndroidComponent.TYPE_ACTIVITY:
                        malIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(malIntent);
                        break;
                    case AndroidComponent.TYPE_BROADCAST_RECEIVER:
                        sendBroadcast(malIntent);
                        break;
                    case AndroidComponent.TYPE_SERVICE:
                        startService(malIntent);
                        break;
                    case AndroidComponent.TYPE_CONTENT_PROVIDER:
                        //BAH
                        break;
                }
            }
            catch(ActivityNotFoundException e){
                Log.i("DoAppLOG", "ActivityNotFound: "+ malIntent.getComponent().getClassName());
                e.printStackTrace();
            }
            catch (SecurityException se){
                Log.i("DoAppLOG", "SecurityException: "+ malIntent.getComponent().getClassName());
                se.printStackTrace();
            }


            //devo attendere perchè Android è lento come la morte

            SystemClock.sleep(1000);

            //analizzo il logcat alla ricerca di un crash
            Log.i("DoAppLOG", "Analizzo LogCat...");
            List<String> lines = UnixCommands.readLogCat();

            LogCatMessageParser parser = new LogCatMessageParser();

            //recupero una lista di LogCatMessage
            List<LogCatMessage> messages = parser.processLogLines(lines);

            //analizzo i messaggi alla ricerca di FATAL EXCEPTION
            List<ExceptionReport> reports = MessagesFilter.filterByFatalException(messages);

            SystemClock.sleep(1000);

            List<Integer> appPIDs = UnixCommands.getAppPID(pkgname);

            //stampo i/il pid del processo sul log
            String pidsString="";
            for(Integer i : appPIDs)
                pidsString += (" "+i.toString());
            Log.i("DoAppLOG", "PID dell'app: "+pidsString);

            for(ExceptionReport ex : reports){

                if((ex.getAppName().contains(pkgname) || ex.getProcessName().equalsIgnoreCase(pkgname)) && (appPIDs.contains(ex.getPID()))){
                    if(lastTime==null || (ex.getTime().after(lastTime))) {

                        //se non è possibile recuperare il nome del componente dal LogCat, lo setto in riferimento a quello sotto test
                        if(ex.getAppName().equals("null"))
                            ex.setAppName(malIntent.getComponent().getClassName());

                        ex.addMalIntent(malIntent);
                        results.add(ex);

                        //aggiungo il nome del componente alla lista di quelli crashati
                        if(!crashedComponents.contains(ex.getAppName()))
                            crashedComponents.add(ex.getAppName());

                        Log.i("DoAppLOG", "Trovato crash:");
                        Log.i("DoAppLOG", "\t" + ex.toString());

                        lastTime = ex.getTime();
                    }

                }
            }

            //l'app viene killata in qualsiasi caso per rendere il test stateless
            Log.i("DoAppLOG", "Kill app");
            UnixCommands.killAll(pkgname);

            SystemClock.sleep(200);

            Intent intermediate = new Intent(this, CounterActivity.class);
            intermediate.putExtra("msg", "Inviato n." + num + " Intent su "+malIntents.size());
            intermediate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intermediate);

            SystemClock.sleep(1000);
            num++;
        }



        if(results.size()>0) {
            pathFile = ReportWriter.scriviSuFile(results, pkgname);
            List<ExceptionReport> finalResults = Triager.triage(results);
            pathFile = ReportWriter.scriviSuFile(finalResults, pkgname);
        }


        Log.i("DoAppLOG", "Fuzzing Completato!");
    }


}
