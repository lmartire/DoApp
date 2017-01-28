package it.unisannio.security.DoApp.generators.semivalidgenerator;


import android.net.Uri;

import org.apache.commons.lang3.RandomStringUtils;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import it.unisannio.security.DoApp.model.MalIntent;

public class GenericPathPortURIGenerator {
    public static MalIntent getSemivalidSchemeHostPortPathURIMalIntent(IntentDataInfo datafield) {

        MalIntent mal = new MalIntent(datafield);
        String scheme = datafield.scheme;
        String host = datafield.host;
        String port = datafield.port;
        String path = datafield.path;
        String semiValidPath;

        if (path.charAt(0) == '/')
            semiValidPath = path + "/" + RandomStringUtils.randomAlphanumeric(10);
        else
            semiValidPath = "/" + path + "/" + RandomStringUtils.randomAlphanumeric(10);

        mal.setData(Uri.parse(scheme + "://" + host + ":" + port + semiValidPath));
        return mal;
    }
}
