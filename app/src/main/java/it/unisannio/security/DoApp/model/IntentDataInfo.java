package it.unisannio.security.DoApp.model;

import com.jaredrummler.apkparser.model.AndroidComponent;
import com.jaredrummler.apkparser.model.IntentFilter;

/**
 * Created by antonio on 05/01/17.
 */

public class IntentDataInfo extends IntentFilter.IntentData{

    private AndroidComponent component;
    private String packageName;

    public IntentDataInfo(IntentFilter.IntentData data, AndroidComponent component, String packageName) {
        super(data.scheme, data.host, data.port, data.path, data.pathPattern, data.pathPrefix, data.mimeType, data.type);
        this.component = component;
        this.packageName = packageName;
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
}
