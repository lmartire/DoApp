package it.unisannio.security.DoApp.activities;

/**
 * Created by security on 06/01/17.
 *
 * activity che presenta una progress bar che aspetta il caricamento di tutte
 * le app non di sistema e si permette di sceglierla
 *
 */

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.unisannio.security.DoApp.services.FuzzerService;
import it.unisannio.security.DoApp.util.PackageInfoExtractor;
import it.unisannio.security.DoApp.R;
import it.unisannio.security.DoApp.adapters.AppInfoAdapter;
import it.unisannio.security.DoApp.model.AppInfo;
import it.unisannio.security.DoApp.model.Commons;
import it.unisannio.security.DoApp.model.IntentDataInfo;

import java.util.List;

public class AppListActivity extends AppCompatActivity {


    private int msg = Commons.MSG_PROCESSING;
    private ProgressBar progressBar = null;
    private ListView listView = null;
    private List<AppInfo> listAppInfo = null;
    private AppInfoAdapter appInfoAdapter = null;


    private Thread mGetPkgInfoThread = null;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg){
            switch(msg.what){
                case Commons.MSG_DONE:
                    progressBar.setVisibility(View.GONE);
                    listView.setAdapter(appInfoAdapter);
                    break;
                case Commons.MSG_PROCESSING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case Commons.MSG_ERROR:
                    break;
            }
        }
    };

    private Runnable pkgInfoRunnable = new Runnable(){
        public void run(){
            listAppInfo = AppInfo.getPackageInfo(AppListActivity.this);
            appInfoAdapter = new AppInfoAdapter(AppListActivity.this, listAppInfo);
            msg = Commons.MSG_DONE;
            mHandler.obtainMessage(msg).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        listView = (ListView) findViewById(R.id.app_listview);

        progressBar.setIndeterminate(false);

        mHandler.obtainMessage(msg).sendToTarget();

        if (mGetPkgInfoThread == null){
            mGetPkgInfoThread = new Thread(pkgInfoRunnable);
            mGetPkgInfoThread.start();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                AppInfo appInfo = (AppInfo) appInfoAdapter.getItem(position);

                PackageInfoExtractor extractor = new PackageInfoExtractor(AppListActivity.this, appInfo.getPackageName());
                int testingComponent = extractor.getNumberComponentWithIntentFilters();

                if(testingComponent==0){
                    Toast.makeText(AppListActivity.this, "Intent-filter non presenti!", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(AppListActivity.this, FuzzerService.class);
                    String pkg = appInfo.getPackageName();
                    i.putExtra(Commons.pkgName, pkg);
                    startService(i);
                }
            }

        });

    }
}
