package it.unisannio.security.DoApp.generators;

import android.content.Intent;
import android.util.Log;

import it.unisannio.security.DoApp.generators.nullgenerator.NullIntentGenerator;
import it.unisannio.security.DoApp.generators.randomgenerator.RandomStringGenerator;
import it.unisannio.security.DoApp.generators.randomgenerator.RandomURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericFileURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericHostURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericPathPortURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericPathPrefixPortURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericPathPrefixURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericPathURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericPortURIGenerator;
import it.unisannio.security.DoApp.generators.semivalidgenerator.GenericSchemeURIGenerator;
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
        }

        //tengo solo lo scheme
        if (data.scheme != null && data.host == null) {
            // scheme  + random
            m = GenericSchemeURIGenerator.getSemivalidSchemeURIMalIntent(data);
            intents.add(m);
        }

        //tengo solo scheme e host
        if (data.scheme != null && data.host != null && data.port == null
                 && data.path == null && data.pathPrefix == null && data.pathPattern == null){
            //scheme + host + random
            m = GenericHostURIGenerator.getSemivalidSchemeHostURIMalIntent(data);
            intents.add(m);
        }

        //(a ^ b ^ c ) ^ ( a && b && c )

        //la porta potrebbe esserci o no
        //la porta ci sta e non ci stanno i tre path*
        if (data.scheme != null && data.host != null && data.port != null &&
                data.path == null && data.pathPrefix == null && data.pathPattern == null) {
            m = GenericPortURIGenerator.getSemivalidSchemeHostPortURIMalIntent(data);
            intents.add(m);
        }

        //la porta ci sta e ci sta uno dei tre path*
        if (data.scheme != null && data.host != null && data.port != null &&
                (data.path != null || data.pathPrefix != null || data.pathPattern != null)){
            //scheme + host + port + random

            if (data.path != null){
                m = GenericPathPortURIGenerator.getSemivalidSchemeHostPortPathURIMalIntent(data);
                intents.add(m);
            }
            if (data.pathPrefix != null){
                m = GenericPathPrefixPortURIGenerator.getSemivalidSchemeHostPortPathPrefixURIMalIntent(data);
                intents.add(m);
            }
            if (data.pathPattern != null){
                //TODO
            }
        }

        //la porta non ci sta e ci stanno i tre path*
        if (data.scheme != null && data.host != null && data.port == null &&
                (data.path != null || data.pathPrefix != null || data.pathPattern != null)) {
            if (data.path != null){
                m = GenericPathURIGenerator.getSemivalidSchemeHostPathURIMalIntent(data);
                intents.add(m);
            }
            if (data.pathPrefix != null){
                m = GenericPathPrefixURIGenerator.getSemivalidSchemeHostPathPrefixURIMalIntent(data);
                intents.add(m);
            }
            if (data.pathPattern != null){
                //TODO
            }
        }

        for (MalIntent x : intents){
            Log.i ("TO-string", x.toString());
        }

        return new ArrayList<>();
        //return intents;
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
