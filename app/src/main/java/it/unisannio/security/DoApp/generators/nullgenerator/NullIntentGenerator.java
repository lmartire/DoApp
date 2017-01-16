package it.unisannio.security.DoApp.generators.nullgenerator;

import com.jaredrummler.apkparser.model.AndroidComponent;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;

/**
 * Created by antonio on 12/01/17.
 */

public class NullIntentGenerator {

    public static MalIntent getNullMalIntent(IntentDataInfo datafield){
        MalIntent mal = new MalIntent(datafield);
        mal.setType(datafield.mimeType);
        return mal;
    }
}
