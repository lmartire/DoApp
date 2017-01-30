package it.unisannio.security.DoApp.generators.semivalidgenerator;


import android.net.Uri;

import org.apache.commons.lang3.RandomStringUtils;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;

public class GenericPathPatternPortURIGenrator {
    public static MalIntent getSemivalidSchemeHostPortPathPatternURIMalIntent(IntentDataInfo datafield) {

        MalIntent mal = new MalIntent(datafield);
        String scheme = datafield.scheme;
        String host = datafield.host;
        String port = datafield.port;
        String pathPattern = datafield.pathPattern;
        String semivalidPathPattern;


        if (pathPattern.contains(".*") || pathPattern.contains("*")) {
            semivalidPathPattern = pathPattern.replace(".*", RandomStringUtils.randomAlphabetic(10));
            semivalidPathPattern = semivalidPathPattern.replace("*", RandomStringUtils.randomAlphanumeric(10));

            if (pathPattern.charAt(0) == '/') {
                mal.setData(Uri.parse(scheme + "://" + host + ":" + port + semivalidPathPattern));
            }
            mal.setData(Uri.parse(scheme + "://" + host + ":" + port + "/" + semivalidPathPattern));
        } else {
            semivalidPathPattern = RandomStringUtils.randomAlphabetic(10);
            if (pathPattern.charAt(0) == '/' || pathPattern.equals("")) {
                mal.setData(Uri.parse(scheme + "://" + host + ":" + port + semivalidPathPattern));
            }
            mal.setData(Uri.parse(scheme + "://" + host + ":" + port + "/" + semivalidPathPattern));

        }


        return mal;
    }
}
