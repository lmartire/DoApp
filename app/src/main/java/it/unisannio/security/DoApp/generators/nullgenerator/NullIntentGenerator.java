package it.unisannio.security.DoApp.generators.nullgenerator;

import com.jaredrummler.apkparser.model.AndroidComponent;

import it.unisannio.security.DoApp.model.MalIntent;

/**
 * Created by antonio on 12/01/17.
 */

public class NullIntentGenerator {

    public static MalIntent getNullMalIntent(String pkgname, AndroidComponent component, String mimetype){
        MalIntent mal = new MalIntent(pkgname,component);
        mal.setType(mimetype);
        return mal;
    }
}
