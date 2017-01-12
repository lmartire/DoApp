package it.unisannio.security.DoApp.generators.randomgenerator;

import android.content.Intent;

import com.jaredrummler.apkparser.model.AndroidComponent;

import it.unisannio.security.DoApp.model.MalIntent;

/**
 * Created by antonio on 12/01/17.
 */

public class RandomStringGenerator {

    public static MalIntent getRandomStringMalIntent(String pkgname, AndroidComponent component, String mimetype){

        MalIntent mal = new MalIntent(pkgname,component);
        mal.setType(mimetype);
        mal.putExtra(Intent.EXTRA_TEXT, "ciao");

        return mal;
    }
}
