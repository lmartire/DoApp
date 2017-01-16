package it.unisannio.security.DoApp;

import android.content.Context;
import android.content.pm.PackageManager;

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
                    for (IntentFilter.IntentData data : datas) {
                        IntentDataInfo dataInfo = new IntentDataInfo(data, component, pkgname, filter);
                        dataInfoList.add(dataInfo);
                    }
                }
            }
        }
        return dataInfoList;
    }
}
