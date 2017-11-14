package com.surajhuljute.bloodbud;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();

        r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ActivityLogin.class));
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        moveForward();
    }

    private void moveForward() {
        handler.removeCallbacks(r);
        handler.postDelayed(r, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }


}
