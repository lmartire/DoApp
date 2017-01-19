package it.unisannio.security.DoApp.generators.semivalidgenerator;

import android.net.Uri;

import org.apache.commons.lang3.RandomStringUtils;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;

public class GenericPathPrefixURIGenerator {
    public static MalIntent getSemivalidSchemeHostPathPrefixURIMalIntent(IntentDataInfo datafield) {

        MalIntent mal = new MalIntent(datafield);
        String scheme = datafield.scheme;
        String host = datafield.host;
        String pathPrefix = datafield.pathPrefix;
        String semivalidPathPrefix;
        if(pathPrefix.charAt(pathPrefix.length()-1) != '/')
            semivalidPathPrefix = pathPrefix + "/" + RandomStringUtils.randomAlphanumeric(10);
        else
            semivalidPathPrefix = pathPrefix + RandomStringUtils.randomAlphanumeric(10);

        mal.setData(Uri.parse(scheme + "://" + host  + semivalidPathPrefix));
        return mal;
    }
}
