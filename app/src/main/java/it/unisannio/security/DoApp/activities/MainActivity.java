package it.unisannio.security.DoApp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.FileNotFoundException;
import java.io.IOException;

import it.unisannio.security.DoApp.services.FuzzerService;
import it.unisannio.security.DoApp.R;
import it.unisannio.security.DoApp.model.Commons;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSU();
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
