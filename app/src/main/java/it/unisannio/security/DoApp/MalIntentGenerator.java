package it.unisannio.security.DoApp;

import android.content.Intent;
import android.util.Log;

import it.unisannio.security.DoApp.generators.nullgenerator.NullIntentGenerator;
import it.unisannio.security.DoApp.generators.randomgenerator.RandomStringGenerator;
import it.unisannio.security.DoApp.generators.randomgenerator.RandomURIGenerator;
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

        MalIntent m;

        for(IntentDataInfo data : datas) {

            if(data.mimeType!=null){
                Log.i("GENERATOR", "controllo il mimetype "+data.mimeType);

                switch (data.mimeType) {
                    case "text/plain":

                        //Null Intent - type unset
                        m = new MalIntent(data);
                        if(!intents.contains(m))
                            intents.add(m);

                        //Null Intent - type set
                        m = NullIntentGenerator.getNullMalIntent(data);
                        if(!intents.contains(m))
                            intents.add(m);

                        //Random String
                        m = RandomStringGenerator.getRandomStringMalIntent(data);
                        if(!intents.contains(m))
                            intents.add(m);

                        break;
                    default:

                        Log.i("GENERATOR", "caso default");
                        //Null Intent - type unset
                        m = new MalIntent(data);
                       // m.setAction(Intent.ACTION_VIEW);
                        if(!intents.contains(m))
                            intents.add(m);

                        //Null Intent - type set
                        m = NullIntentGenerator.getNullMalIntent(data);
                        m.setAction(Intent.ACTION_VIEW);
                        if(!intents.contains(m))
                            intents.add(m);

                        //Random String
                        m = RandomStringGenerator.getRandomStringMalIntent(data);
                        m.setAction(Intent.ACTION_VIEW);
                        if(!intents.contains(m))
                            intents.add(m);

                        //Random URI
                        m = RandomURIGenerator.getRandomURIMalIntent(data);
                        m.setAction(Intent.ACTION_VIEW);
                        if(!intents.contains(m))
                            intents.add(m);

                        break;
                }
            }
            else if(data.scheme!=null){

                if(data.host!=null){

                }
                else{

                }

            }
            //il campo data è assente
            else{}
        }

        Log.i("GENERATOR", "numero intent: "+intents.size());
        return intents;

    }
}
