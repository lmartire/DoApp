package it.unisannio.security.DoApp.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import it.unisannio.security.DoApp.R;
import it.unisannio.security.DoApp.model.Commons;
import it.unisannio.security.DoApp.model.MalIntent;

public class ViewReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        String pathFile = getIntent().getStringExtra(Commons.pathFile);

        TextView tv = (TextView) findViewById(R.id.notetitle);
        tv.setTextColor(Color.WHITE);
        tv.setText(""+pathFile);

        //Get the text file
        File file = new File(pathFile);

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

//Find the view by its id
        EditText edit = (EditText) findViewById(R.id.note);

//Set the text
        edit.setFocusable(false);
        edit.setFocusableInTouchMode(false);
        edit.clearFocus();
        edit.setTag(edit.getKeyListener());
        edit.setKeyListener(null);
        edit.setText(text.toString());
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
