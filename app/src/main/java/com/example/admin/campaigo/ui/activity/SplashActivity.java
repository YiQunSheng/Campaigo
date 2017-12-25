package com.example.admin.campaigo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.campaigo.R;
import com.example.admin.campaigo.util.SharePreferenceUtil;

/**
 * Created by admin on 2017/12/16.
 */

public class SplashActivity extends AppCompatActivity{

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirst = SharePreferenceUtil.getBoolean(SplashActivity.this, "isFirst", true);
                if (isFirst) {
                    SharePreferenceUtil.putBoolean(SplashActivity.this, "isFirst", false);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000);
    }



}
