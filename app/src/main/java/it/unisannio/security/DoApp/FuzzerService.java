package it.unisannio.security.DoApp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import it.unisannio.security.DoApp.model.ExceptionReport;
import it.unisannio.security.DoApp.model.LogCatMessage;
import it.unisannio.security.DoApp.model.MalIntent;
import it.unisannio.security.DoApp.parser.LogCatMessageParser;
import it.unisannio.security.DoApp.parser.MessagesFilter;
import it.unisannio.security.DoApp.model.IntentDataInfo;
import com.jaredrummler.apkparser.model.AndroidComponent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class FuzzerService extends IntentService {

    public FuzzerService() {
        super("FuzzerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //estraggo il nome dell'app su cui fare fuzzing
            String pkgname = intent.getStringExtra("pkgname");
            if(pkgname!=null && !pkgname.isEmpty()) {
                fuzz(pkgname);
            }
        }
    }

    private void fuzz(String pkgname){

        //lista contenente i risultati
        List<ExceptionReport> results = new ArrayList<ExceptionReport>();

        Log.i("*DEBUG", "recupero la lista dei datatype");

        //recupero la lista dei datatype degli IntentFilter esportati dall'app
        PackageInfoExtractor extractor = new PackageInfoExtractor(this);
        List<IntentDataInfo> datas = extractor.extractIntentFiltersDataType(pkgname);

        Log.i("*DEBUG", "creo gli intent malevoli");
        //ottengo la lista degli intent malevoli
        //TODO: creare la classe MalIntentGenerator e relativo metodo
        List<MalIntent> malIntents = MalIntentGenerator.createFromIntentData(datas);

        Log.i("*DEBUG", "Intent creati, inizio il fuzzing");

        int num=0;

        //invio uno alla volta gli intent
        for(MalIntent i : malIntents){


            Log.i("*DEBUG", "Intent n. " + (++num));

            //pulisco il logcat .... se solo si pulisse! invece non funziona manco per il cazzo
            //clearLogCat();

            //invio l'intent malevolo
            switch (i.getTargetComponent().type){
                case AndroidComponent.TYPE_ACTIVITY :
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    break;
                case AndroidComponent.TYPE_BROADCAST_RECEIVER :
                    sendBroadcast(i);
                    break;
                case AndroidComponent.TYPE_SERVICE:
                    startService(i);
                    break;
                case AndroidComponent.TYPE_CONTENT_PROVIDER:
                    //BAH
                    break;
            }

            //devo attendere perchè Android è lento come la morte
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //analizzo il logcat alla ricerca di un crash
            List<String> lines = readLogCat();

            LogCatMessageParser parser = new LogCatMessageParser();

            //recupero una lista di LogCatMessage
            List<LogCatMessage> messages = parser.processLogLines(lines);


            Log.i("*DEBUG", "leggo il logcat");

            //analizzo i messaggi alla ricerca di FATAL EXCEPTION
            List<ExceptionReport> reports = MessagesFilter.filterByFatalException(messages);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("*DEBUG", "recupero PID app");
            int appPid = getAppPID(pkgname);
            Log.i("*DEBUG", "PID: "+String.valueOf(appPid));
            Log.i("*DEBUG", "Analizzo i report, numero di report: "+reports.size());

            int num2 = 0;
            for(ExceptionReport ex : reports){

                Log.i("*DEBUG", "Report: "+(++num2));
                Log.i("*DEBUG", ex.toString());

                if(ex.getAppName().contains(pkgname) && (ex.getPID() == appPid)){
                    Log.i("*DEBUG", "Aggiungo il report n. "+num2);
                    results.add(ex);

                    Log.i("*DEBUG", "killo l'app");
                    killApp(appPid);

                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TODO: effettuare il triage sulla lista di ExceptionReport

        //temporaneo
        scriviSuFile(results, "/storage/sdcard0/result.txt");
        Log.i("*DEBUG", "FINITO");
    }

    private void clearLogCat(){
        try {
            Runtime.getRuntime().exec(new String[]{"su", "-c","logcat -c"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readLogCat(){
        BufferedReader bufferedReader;
        List<String> lines = new ArrayList<String>();

        String[] commands = {"su", "-c","logcat -d -v long"};
        java.lang.Process process;
        try {
            process = Runtime.getRuntime().exec(commands);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }


    private List<String> readLogCat(java.lang.Process suProcess){
        BufferedReader bufferedReader;
        List<String> lines = new ArrayList<String>();

        try {
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes("logcat -d -v long \n");
            os.flush();
            bufferedReader = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }

    private void killApp(int PID){
        java.lang.Process suProcess = null;
        String[] commands = {"su", "-c","kill "+PID};
        try {
            suProcess = Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAppPID(String pkgname) {
        BufferedReader bufferedReader;
        String[] commands = {"su", "-c","pidof "+pkgname};
        java.lang.Process process;
        String line = null;
        try {
            process = Runtime.getRuntime().exec(commands);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(line!=null)
            return Integer.parseInt(line);
        else
            return -1;
    }

    private void scriviSuFile(List<ExceptionReport> reports, String nomeFile){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(nomeFile);
            PrintStream ps = new PrintStream(fileOutputStream);
            int i=1;
            for(ExceptionReport er : reports){
                ps.println( (i++) + er.toString());
            }
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
