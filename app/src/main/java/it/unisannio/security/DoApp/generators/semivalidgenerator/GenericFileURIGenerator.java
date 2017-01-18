package it.unisannio.security.DoApp.generators.semivalidgenerator;

import android.content.Intent;
import android.net.Uri;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by antonio on 18/01/17.
 */

public class GenericFileURIGenerator {

    public static MalIntent getSemivalidFileURIMalIntent(IntentDataInfo datafield){

        MalIntent mal = new MalIntent(datafield);
        mal.setType(datafield.mimeType);


        //dovrebbe essere random
        mal.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ RandomStringUtils.randomAlphanumeric(10)));

        return mal;
    }
}
