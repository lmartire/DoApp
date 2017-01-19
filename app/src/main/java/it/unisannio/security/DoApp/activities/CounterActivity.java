package it.unisannio.security.DoApp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import it.unisannio.security.DoApp.R;

public class CounterActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        tv = (TextView) findViewById(R.id.textView3);
        tv.setText(getIntent().getStringExtra("msg"));
    }
}
