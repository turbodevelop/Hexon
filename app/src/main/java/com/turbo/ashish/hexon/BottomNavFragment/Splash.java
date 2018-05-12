package com.turbo.ashish.hexon.BottomNavFragment;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.turbo.ashish.hexon.PhoneLogin;
import com.turbo.ashish.hexon.R;

public class Splash extends AppCompatActivity {
    private ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.imageView);

        toolbar = getSupportActionBar();
        toolbar.hide();
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imageView.startAnimation(animation);

        Thread th=new Thread(){


            @Override
            public void run() {
                try {
                    sleep(3000);
                }catch (Exception e){

                    e.printStackTrace();
                }finally {
                    Intent obj=new Intent(Splash.this,PhoneLogin.class);
                    startActivity(obj);
                }
            }
        };
        th.start();




    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

