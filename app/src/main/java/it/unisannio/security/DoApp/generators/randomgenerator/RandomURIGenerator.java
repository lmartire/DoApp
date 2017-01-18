package it.unisannio.security.DoApp.generators.randomgenerator;

import android.content.Intent;
import android.net.Uri;

import com.jaredrummler.apkparser.model.AndroidComponent;

import org.apache.commons.lang3.RandomStringUtils;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;

/**
 * Created by antonio on 12/01/17.
 */

public class RandomURIGenerator {

    public static MalIntent getRandomURIMalIntent(IntentDataInfo datafield){

        MalIntent mal = new MalIntent(datafield);
        mal.setType(datafield.mimeType);

        mal.putExtra(Intent.EXTRA_STREAM, Uri.parse(RandomStringUtils.randomAlphanumeric(10)));

        return mal;
    }
}
