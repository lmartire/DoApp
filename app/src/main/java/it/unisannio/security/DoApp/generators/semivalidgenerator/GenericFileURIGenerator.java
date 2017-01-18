package it.unisannio.security.DoApp.generators.semivalidgenerator;

import android.content.Intent;
import android.net.Uri;

import org.apache.commons.lang3.RandomStringUtils;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;


public class GenericFileURIGenerator {

    public static MalIntent getSemivalidFileURIMalIntent(IntentDataInfo datafield){

        MalIntent mal = new MalIntent(datafield);
        mal.setType(datafield.mimeType);


        //dovrebbe essere random
        mal.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ RandomStringUtils.randomAlphanumeric(10)));

        return mal;
    }
}
