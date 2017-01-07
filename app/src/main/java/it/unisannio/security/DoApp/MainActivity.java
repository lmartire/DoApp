package it.unisannio.security.DoApp;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.FileNotFoundException;
import java.io.IOException;

import it.unisannio.security.DoApp.model.Commons;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //@luigi observa!
        getSU();
    }

    public void fuzzerService(View v) throws FileNotFoundException, InterruptedException {

        Intent i = new Intent(this, FuzzerService.class);

        //TODO: sta roba deve venire da una lista di scelta dell'app
        i.putExtra(Commons.pkgName, "com.project.antonyflour.intentfilterex");
        startService(i);

    }

    public void goAppList (View v){
        Intent i = new Intent(this, AppListActivity.class);
        startActivity(i);
    }

    private java.lang.Process getSU(){
        java.lang.Process suProcess = null;
        try {
            suProcess = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suProcess;
    }


}
