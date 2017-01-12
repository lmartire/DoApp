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

    @Override
    public boolean equals(Object object){
        MalIntent mal1 = (MalIntent) object;

        if(this.getComponent().getClassName().equalsIgnoreCase(mal1.getComponent().getClassName())){
            if(this.getType()==null && mal1.getType()==null) return true;

            else if(this.getType()==null || mal1.getType()==null) return false;

            else if(this.getType().equalsIgnoreCase(mal1.getType()))
                    return true;
        }

        return false;

    }

    public String toString(){
        String tipo;
        if(this.getType()==null)
            tipo = "null";
        else tipo = this.getType();

        String extra_text;
        if( (this.getExtras()==null) || (this.getExtras().get(Intent.EXTRA_TEXT) == null) ){
            extra_text = "null";
        }
        else{
            extra_text = this.getExtras().get(Intent.EXTRA_TEXT).toString();
        }
        return "type: "+tipo + " - Extra Text: "+ extra_text;
    }
}
