package it.unisannio.security.DoApp.model;

import android.content.ComponentName;
import android.content.Intent;

import com.jaredrummler.apkparser.model.AndroidComponent;

/**
 * Created by antonio on 05/01/17.
 */

public class MalIntent extends Intent{

    private AndroidComponent targetComponent;

    public MalIntent(String packageName, AndroidComponent targetComponent){
        super();
        this.targetComponent = targetComponent;
        this.setComponent(new ComponentName(packageName, targetComponent.name));
    }

    public AndroidComponent getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(AndroidComponent targetComponent) {
        this.targetComponent = targetComponent;
    }
}
