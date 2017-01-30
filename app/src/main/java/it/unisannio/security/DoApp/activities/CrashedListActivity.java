package it.unisannio.security.DoApp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import it.unisannio.security.DoApp.GlobalClass;
import it.unisannio.security.DoApp.R;
import it.unisannio.security.DoApp.model.Commons;

public class CrashedListActivity extends AppCompatActivity {

    ListView list;
    TextView tvResult;

    String pathfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crashed_list);

        //path al file contente il report
        pathfile = getIntent().getStringExtra(Commons.pathFile);


        tvResult = (TextView) findViewById(R.id.textViewResult);

        String text = tvResult.getText().toString();
        String crashed =  String.valueOf(getIntent().getIntExtra("numberCrashedComponents", -1));
        String totalComponents = String.valueOf(getIntent().getIntExtra("numberTotalComponents", -1));
        text = StringUtils.replaceOnce(text, "$", crashed);
        text = StringUtils.replaceOnce(text, "$", totalComponents);

        tvResult.setText(text);
        tvResult.setTextColor(Color.WHITE);

        ArrayList<String> crashedComponents = getIntent().getStringArrayListExtra("crashedComponents");

        list = (ListView) findViewById(R.id.listViewCrashedComponents);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_string_entry, crashedComponents);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String componentName = adapter.getItem(position);

                Intent i = new Intent(CrashedListActivity.this, ExceptionListActivity.class);
                i.putExtra("componentName", componentName);
                startActivity(i);
            }
        });


    }

    public void startViewReportActivity(View v){
        Intent i = new Intent(this,ViewReportActivity.class);
        i.putExtra(Commons.pathFile, pathfile);
        startActivity(i);
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
