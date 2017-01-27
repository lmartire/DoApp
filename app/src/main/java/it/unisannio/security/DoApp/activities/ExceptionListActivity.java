package it.unisannio.security.DoApp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unisannio.security.DoApp.GlobalClass;
import it.unisannio.security.DoApp.R;
import it.unisannio.security.DoApp.adapters.MalIntentExpandableListAdapter;
import it.unisannio.security.DoApp.model.ExceptionReport;
import it.unisannio.security.DoApp.model.MalIntent;

public class ExceptionListActivity extends AppCompatActivity {

    TextView tvName;

    ExpandableListView expListView;

    private String componentName;

    //hashmap contenente i report organizzati per tipo di eccezione
    //private HashMap<String, List<ExceptionReport>> mapReports;

    //hashmap contenente i MalIntent organizzati per tipo di eccezione
    private HashMap<String, List<MalIntent>> mapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_list);
        componentName = getIntent().getStringExtra("componentName");

        tvName = (TextView) findViewById(R.id.textViewComponentName);
        tvName.setText(componentName);

        List<ExceptionReport> reportsFiltered = filterByComponentName(GlobalClass.reports);
        List<String> exceptionTypes = getExceptionTypesFromReports(reportsFiltered);

        mapIntent = organizeMalIntentssByExceptionType(exceptionTypes, reportsFiltered);


        expListView = (ExpandableListView) findViewById(R.id.expListView);

        ExpandableListAdapter listAdapter = new MalIntentExpandableListAdapter(this, exceptionTypes, mapIntent);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    private List<ExceptionReport> filterByComponentName(List<ExceptionReport> reports){
        ArrayList<ExceptionReport> filtered = new ArrayList<ExceptionReport>();
        for(ExceptionReport ex : reports){
            if(ex.getAppName().equals(componentName))
                filtered.add(ex);
        }
        return filtered;
    }

    private List<String> getExceptionTypesFromReports(List<ExceptionReport> reports){
        ArrayList<String> types = new ArrayList<String>();
        for(ExceptionReport ex : reports){
            if(!types.contains(ex.getType())){
                types.add(ex.getType());
            }
        }
        return types;
    }

    private HashMap<String, List<ExceptionReport>> organizeReportsByExceptionType(List<String> exceptionTypes, List<ExceptionReport> reportsFiltered){
        HashMap<String, List<ExceptionReport>> newhash = new HashMap<String, List<ExceptionReport>>();

        for(String type : exceptionTypes){
            List<ExceptionReport> tmpReports = new ArrayList<ExceptionReport>();
            for(ExceptionReport ex : reportsFiltered){
                if(ex.getType().equals(type)){
                    tmpReports.add(ex);
                }
            }
            newhash.put(type, tmpReports);
        }

        return newhash;
    }

    private HashMap<String, List<MalIntent>> organizeMalIntentssByExceptionType(List<String> exceptionTypes, List<ExceptionReport> reportsFiltered){
        HashMap<String, List<MalIntent>> newhash = new HashMap<String, List<MalIntent>>();

        for(String type : exceptionTypes){
            List<MalIntent> tmpMalintent = new ArrayList<MalIntent>();
            for(ExceptionReport ex : reportsFiltered){
                if(ex.getType().equals(type)){
                    for(MalIntent m : ex.getMalIntents()){
                        tmpMalintent.add(m);
                    }
                }
            }
            newhash.put(type, tmpMalintent);
        }

        return newhash;
    }
}
