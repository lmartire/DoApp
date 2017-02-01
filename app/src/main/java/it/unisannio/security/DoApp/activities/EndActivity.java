package it.unisannio.security.DoApp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.unisannio.security.DoApp.R;
import it.unisannio.security.DoApp.model.Commons;

public class EndActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Button button;
        final String pathFile;
        final TextView textView;

        textView = (TextView) findViewById(R.id.textViewVisit);
        button = (Button) findViewById(R.id.buttonEnd);


        Intent i = getIntent();
        pathFile = i.getStringExtra(Commons.pathFile);
        if(pathFile == null){
            textView.setText("The app didn't crash.\n No report generated");
            button.setVisibility(View.INVISIBLE);
            button.setClickable(false);
        }
        else {
            textView.setText("The app was crashed");

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(EndActivity.this,ViewReportActivity.class);
                    i.putExtra(Commons.pathFile, pathFile);
                    startActivity(i);
                }
            });
        }
        textView.setTextColor(Color.WHITE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }
}
