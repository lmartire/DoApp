package it.unisannio.security.DoApp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            textView.setText("Non è stato esegguito alcun report\nNon si sono verificati crash!");
            button.setVisibility(View.INVISIBLE);
            button.setClickable(false);
        }
        else {
            textView.setText("L'app è crashata. Per maggiori info vai" +
                    " al percorso\n"+pathFile+"\nOppure clicca il bottone");
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    /*Intent intent = new Intent(Intent.ACTION_EDIT);
                    Uri uri = Uri.parse(pathFile);
                    // intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setDataAndType(uri, "text/plain");
                    startActivity(intent);*/

                    Intent i = new Intent(EndActivity.this,ViewReportActivity.class);
                    i.putExtra(Commons.pathFile, pathFile);
                    startActivity(i);
                }
            });

        }
    }

/*    public void openFolder(View v){
        Intent intent = new Intent(Intent.ACTION_EDIT);
        Uri uri = Uri.parse(pathFile);
       // intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setDataAndType(uri, "text/plain");

        startActivity(intent);
        //startActivityForResult(intent, 0);
       // startActivity(Intent.createChooser(intent, "Open folder"));
    }

    public void  onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode) {
            case 0: {
                Toast.makeText(this,""+data.getDataString(),Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
