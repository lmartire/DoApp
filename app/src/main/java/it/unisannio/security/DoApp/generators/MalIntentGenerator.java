package it.unisannio.security.DoApp.generators;

import android.content.Intent;
import android.util.Log;

import it.unisannio.security.DoApp.generators.nullgenerator.NullIntentGenerator;
import it.unisannio.security.DoApp.generators.randomgenerator.RandomStringGenerator;
import it.unisannio.security.DoApp.generators.randomgenerator.RandomURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericFileURIGenerator;
import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 05/01/17.
 */

public class MalIntentGenerator {

    public static List<MalIntent> createFromIntentData(List<IntentDataInfo> datas){

        List<MalIntent> intents = new ArrayList<MalIntent>();

        //per ogni campo data dichiarato nel manifest
        for(IntentDataInfo data : datas) {

            List<MalIntent> intents_for_single = generateForSingleData(data);
            addIfNotContains(intents, intents_for_single);

            //devo generare i malIntent per ogni ACTION dichiarata
            if(!data.getFilter().actions.isEmpty()){

                //per ogni azione relativa al campo data
                for(String action : data.getFilter().actions) {

                    List<MalIntent> intents_with_action = cloneList(intents_for_single);
                    setActionForAll(action, intents_with_action);

                    addIfNotContains(intents, intents_with_action);
                }
            }

        }


        Log.i("GENERATOR", "numero intent: "+intents.size());
        //STAMPA
        Log.i("*MALINTENT", "ACTION");
        for(MalIntent m : intents){
            Log.i("*MALINTENT", m.toString());
        }

        return intents;

    }


    /**
     * Generate a list of MalIntent by IntentDataInfo analysis
     * The method doesn't set the ACTION field
     * @param data
     * @return
     */
    private static List<MalIntent> generateForSingleData(IntentDataInfo data){

        List<MalIntent> intents = new ArrayList<MalIntent>();
        MalIntent m;
        if (data.mimeType != null) {
            Log.i("GENERATOR", "controllo il mimetype " + data.mimeType);

            switch (data.mimeType) {
                case "text/plain":

                    //Null Intent - type unset
                    m = new MalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    //Null Intent - type set
                    m = NullIntentGenerator.getNullMalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    //Random String
                    m = RandomStringGenerator.getRandomStringMalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    break;
                default:

                    Log.i("GENERATOR", "caso default");
                    //Null Intent - type unset
                    m = new MalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    //Null Intent - type set
                    m = NullIntentGenerator.getNullMalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    //Random String
                    m = RandomStringGenerator.getRandomStringMalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    //Random URI
                    m = RandomURIGenerator.getRandomURIMalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    //Semivalid URI to file
                    m = GenericFileURIGenerator.getSemivalidFileURIMalIntent(data);
                    if (!intents.contains(m))
                        intents.add(m);

                    break;
            }
        } else if (data.scheme != null) {

            if (data.host != null) {

            } else {

            }

        }

        return intents;
    }

    private static void setActionForAll(String action, List<MalIntent> malIntents){
        for(MalIntent m : malIntents){
            m.setAction(action);
        }
    }

    private static void addIfNotContains(List<MalIntent> totalIntents, List<MalIntent> intents){
        for(MalIntent m : intents){
            if(!totalIntents.contains(m))
                totalIntents.add(m);
        }
    }

    private static List<MalIntent> cloneList(List<MalIntent> intents){
        List<MalIntent> cloned = new ArrayList<MalIntent>();
        for(MalIntent m : intents){
            cloned.add(m.clone());
        }
        return cloned;
    }
}
