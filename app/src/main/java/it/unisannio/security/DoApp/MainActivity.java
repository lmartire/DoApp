package it.unisannio.security.DoApp;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.FileNotFoundException;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fuzzerService(View v) throws FileNotFoundException, InterruptedException {

       /* Intent service = new Intent(this, LogIntentService.class);
        startService(service);*/

        Intent analize = new Intent(this, FuzzerService.class);
        startService(analize);

        Intent i = new Intent();


        //TODO: inserire il componente da una lista
        ComponentName cn;
        cn = new ComponentName("com.project.antonyflour.intentfilterex",
                "com.project.antonyflour.intentfilterex.ShareActivity");


        i.setComponent(cn);
        startActivity(i);

    }

    public void goAppList (View v){
        Intent i = new Intent(this, AppListActivity.class);
        startActivity(i);
    }


}
