package com.bluewave.nfcgame.activity;

import android.os.Bundle;

import com.bluewave.nfcgame.R;
import com.bluewave.nfcgame.base.BaseActivity;
import com.bluewave.nfcgame.common.ActivityStarter;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_splash);
        Thread myThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000);
                    ActivityStarter.startMainActivity(SplashActivity.this);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
