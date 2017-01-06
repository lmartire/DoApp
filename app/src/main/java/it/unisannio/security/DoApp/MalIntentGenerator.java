package it.unisannio.security.DoApp;

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

        /*for(IntentDataInfo data : datas) {
            //null intent
            MalIntent i = new MalIntent(data.getPackageName(), data.getComponent());
            intents.add(i);
        }
        */

        MalIntent i1 = new MalIntent(datas.get(0).getPackageName(), datas.get(0).getComponent());
        intents.add(i1);

        MalIntent i2 = new MalIntent(datas.get(0).getPackageName(), datas.get(0).getComponent());
        intents.add(i2);

        return intents;

    }
}
