package it.unisannio.security.DoApp.model;

import android.util.Log;

import com.jaredrummler.apkparser.model.AndroidComponent;
import com.jaredrummler.apkparser.model.IntentFilter;

/**
 * Created by antonio on 05/01/17.
 */

public class IntentDataInfo extends IntentFilter.IntentData{

    private AndroidComponent component;
    private String packageName;
    private IntentFilter filter; //intent-filter in which is declared the <data> field

    public IntentDataInfo(IntentFilter.IntentData data, AndroidComponent component, String packageName, IntentFilter filter) {
        super(data.scheme, data.host, data.port, data.path, data.pathPattern, data.pathPrefix, data.mimeType, data.type);
        this.component = component;
        this.packageName = packageName;
        this.filter = filter;
        Log.i("*****FILTERS: ", component.name+ ":   " + scheme +
                " " + host + " " + port + " " + path + " " + pathPrefix + " " +pathPattern);
    }

    public AndroidComponent getComponent(){
        return component;
    }

    public void setComponent(AndroidComponent component){
        this.component = component;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public IntentFilter getFilter() {
        return filter;
    }

    public void setFilter(IntentFilter filter) {
        this.filter = filter;
    }
}
