package it.unisannio.security.DoApp.model;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jaredrummler.apkparser.model.AndroidComponent;

/**
 * Created by antonio on 05/01/17.
 */

public class MalIntent extends Intent{

    private AndroidComponent targetComponent;

    public MalIntent(IntentDataInfo datafield){
        super();
        this.targetComponent = datafield.getComponent();
        String packageName = datafield.getPackageName();

        // se il nome inizia per '.' è necessario creare il nome completo del componente
        // altrimenti si va in ActivityNotFoundException durante lo startActivity()
        String name = targetComponent.name;
        if(name.startsWith("."))
            name = packageName+name;

        this.setComponent(new ComponentName(packageName, name));
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
        // filterEquals fa: Returns true if action, data, type, class, and categories are the same.
        if(this.filterEquals(mal1)){
            //dobbiamo fare il confronto per gli extra in qualche modo
            Bundle b1 = mal1.getExtras();
            Bundle b = this.getExtras();
            if (b1 == null && b == null)
                return true;
            else if (b1 == null ^ b == null) // xor: ^
                return false;
            else {
                // dynamic binding (speriamo che va)
                return equalsObject(b.get(Intent.EXTRA_TEXT), b1.get(Intent.EXTRA_TEXT)) &&
                        equalsObject(b.get(Intent.EXTRA_STREAM), b1.get(EXTRA_STREAM));
            }
        }
        return false;
    }

    private static boolean equalsObject(Object o, Object o1){
        if ( o == null && o1 == null)
            return true;
        else if (o == null ^ o1 == null)
            return false;
        else return o.equals(o1);
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
