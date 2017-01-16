package it.unisannio.security.DoApp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import it.unisannio.security.DoApp.model.Commons;

public class ViewReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        String pathFile = getIntent().getStringExtra(Commons.pathFile);
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
}
