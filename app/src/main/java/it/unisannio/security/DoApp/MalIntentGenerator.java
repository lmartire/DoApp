package it.unisannio.security.DoApp;

import it.unisannio.security.DoApp.generators.nullgenerator.NullIntentGenerator;
import it.unisannio.security.DoApp.generators.randomgenerator.RandomStringGenerator;
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

        for(IntentDataInfo data : datas) {

            if(data.mimeType!=null){

                switch (data.mimeType) {
                    case "text/plain":

                        //Null Intent - type unset
                        MalIntent m = new MalIntent(data.getPackageName(), data.getComponent());
                        if(!intents.contains(m))
                            intents.add(m);

                        //Null Intent - type set
                        m = NullIntentGenerator.getNullMalIntent(data.getPackageName(), data.getComponent(), data.mimeType);
                        if(!intents.contains(m))
                            intents.add(m);

                        //Random String
                        m = RandomStringGenerator.getRandomStringMalIntent(data.getPackageName(), data.getComponent(),data.mimeType);
                        if(!intents.contains(m))
                            intents.add(m);

                        break;
                    default:
                        break;
                }
            }
            else if(data.scheme!=null){

                if(data.host!=null){

                }
                else{

                }

            }
        }

        return intents;

    }
}
