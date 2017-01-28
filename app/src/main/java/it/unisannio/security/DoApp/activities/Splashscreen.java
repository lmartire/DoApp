package it.unisannio.security.DoApp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import it.unisannio.security.DoApp.R;

public class Splashscreen extends Activity {
    HTextView hTextView;
    HTextView hTextView2;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        hTextView = (HTextView) findViewById(R.id.text);



        StartAnimations();

    }
    private void StartAnimations() {




        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        hTextView.setAnimateType(HTextViewType.SCALE);
        hTextView.animateText("DoApp");
        anim.reset();

        ImageView iv = (ImageView) findViewById(R.id.splash);

        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splashscreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(intent);
                    Splashscreen.this.finish();

                } catch (InterruptedException e) {

                } finally {
                    Splashscreen.this.finish();
                }
            }
        };
        splashTread.start();
    }

}