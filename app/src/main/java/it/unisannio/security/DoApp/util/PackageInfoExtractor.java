package it.unisannio.security.DoApp.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import it.unisannio.security.DoApp.model.IntentDataInfo;
import com.jaredrummler.apkparser.ApkParser;
import com.jaredrummler.apkparser.model.AndroidComponent;
import com.jaredrummler.apkparser.model.AndroidManifest;
import com.jaredrummler.apkparser.model.IntentFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 05/01/17.
 */

public class PackageInfoExtractor {

    private Context context;

    public PackageInfoExtractor(Context context){
        this.context = context;
    }

    public List<AndroidComponent> extractComponents(String pkgname){
        ApkParser manifestParser = null;
        List<AndroidComponent> components = new ArrayList<AndroidComponent>();
        try {
            manifestParser = ApkParser.create(context.getPackageManager(), pkgname);
            AndroidManifest manifest = null;
            try{
                manifest = manifestParser.getAndroidManifest();
                components = manifest.getComponents();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return components;
    }

    public List<IntentDataInfo> extractIntentFiltersDataType(String pkgname){
        List<AndroidComponent> components = extractComponents(pkgname);
        List<IntentDataInfo> dataInfoList = new ArrayList<IntentDataInfo>();

        for (AndroidComponent component : components) {
            //se il componente non esporta intent-filter, non ci interessa
            if (!component.intentFilters.isEmpty()) {
                List<IntentFilter> filters = component.intentFilters;
                for (IntentFilter filter : filters) {
                    //recupero la lista dei datatype per ogni intent-filter
                    List<IntentFilter.IntentData> datas = filter.dataList;

                    //creiamo una lista per ogni campo dell'URI
                    List<String> listScheme = new ArrayList<String>();
                    List<String> listHost = new ArrayList<String>();
                    List<String> listPort = new ArrayList<String>();
                    List<String> listPath = new ArrayList<String>();
                    List<String> listPathPrefix = new ArrayList<String>();
                    List<String> listPathPattern = new ArrayList<String>();

                    for (IntentFilter.IntentData data : datas) {
                        if (data.mimeType != null ){
                            IntentDataInfo dataInfo = new IntentDataInfo(data, component, pkgname, filter);
                            dataInfoList.add(dataInfo);
                        }
                        if (data.scheme != null && !listScheme.contains(data.scheme))
                            listScheme.add(data.scheme);
                        if (data.host != null && !listHost.contains(data.host))
                            listHost.add(data.host);
                        if (data.port != null && !listPort.contains(data.port))
                            listPort.add(data.port);
                        if (data.path != null  && !listPath.contains(data.path))
                            listPath.add(data.path);
                        if (data.pathPrefix != null && !listPathPrefix.contains(data.pathPrefix))
                            listPathPrefix.add(data.pathPrefix);
                        if (data.pathPattern != null && !listPathPattern.contains(data.pathPattern))
                            listPathPattern.add(data.pathPattern);
                    }

                    //cerchiamo di fare una combinazione tra tutti i casi
                    for (String scheme : listScheme){
                        IntentFilter.IntentData dataScheme = new IntentFilter.IntentData(scheme,
                                null, null, null, null, null, null, null);
                        IntentDataInfo infoScheme = new IntentDataInfo(dataScheme, component, pkgname, filter);
                        dataInfoList.add(infoScheme);


                        for (String host : listHost){
                            IntentFilter.IntentData dataHost = new IntentFilter.IntentData(scheme,
                                    host, null, null, null, null, null, null);
                            IntentDataInfo infoHost = new IntentDataInfo(dataHost, component, pkgname, filter);
                            dataInfoList.add(infoHost);

                            //  la porta potrebbe non esserci, invece i vari path si
                            if (!listPort.isEmpty()){
                                for (String port : listPort) {
                                    IntentFilter.IntentData dataPort = new IntentFilter.IntentData(scheme,
                                            host, port, null, null, null, null, null);
                                    IntentDataInfo infoPort = new IntentDataInfo(dataPort, component, pkgname, filter);
                                    dataInfoList.add(infoPort);

                                    for (String path : listPath) {
                                        IntentFilter.IntentData dataPath = new IntentFilter.IntentData(scheme,
                                                host, port, path, null, null, null, null);
                                        IntentDataInfo infoPath = new IntentDataInfo(dataPath, component, pkgname, filter);
                                        dataInfoList.add(infoPath);
                                    }
                                    for (String pathPrefix : listPathPrefix) {
                                        IntentFilter.IntentData dataPathPrefix = new IntentFilter.IntentData(scheme,
                                                host, port, null, null, pathPrefix, null, null);
                                        IntentDataInfo infoPathPrefix = new IntentDataInfo(dataPathPrefix, component, pkgname, filter);
                                        dataInfoList.add(infoPathPrefix);
                                    }
                                    for (String pathPattern : listPathPattern) {
                                        IntentFilter.IntentData dataPathPattern = new IntentFilter.IntentData(scheme,
                                                host, port, null, pathPattern, null, null, null);
                                        IntentDataInfo infoPathPattern = new IntentDataInfo(dataPathPattern, component, pkgname, filter);
                                        dataInfoList.add(infoPathPattern);
                                    }
                                }
                            }
                            else {
                                for (String path : listPath) {
                                    IntentFilter.IntentData dataPath = new IntentFilter.IntentData(scheme,
                                            host, null, path, null, null, null, null);
                                    IntentDataInfo infoPath = new IntentDataInfo(dataPath, component, pkgname, filter);
                                    dataInfoList.add(infoPath);
                                }
                                for (String pathPrefix : listPathPrefix) {
                                    IntentFilter.IntentData dataPathPrefix = new IntentFilter.IntentData(scheme,
                                            host, null, null, null, pathPrefix, null, null);
                                    IntentDataInfo infoPathPrefix = new IntentDataInfo(dataPathPrefix, component, pkgname, filter);
                                    dataInfoList.add(infoPathPrefix);
                                }
                                for (String pathPattern : listPathPattern) {
                                    IntentFilter.IntentData dataPathPattern = new IntentFilter.IntentData(scheme,
                                            host, null, null, pathPattern, null, null, null);
                                    IntentDataInfo infoPathPattern = new IntentDataInfo(dataPathPattern, component, pkgname, filter);
                                    dataInfoList.add(infoPathPattern);
                                }
                            }
                        }
                    }
                }
            }
        }
        return dataInfoList;
        //return new ArrayList<IntentDataInfo>();
    }
}
