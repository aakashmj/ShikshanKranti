package com.shikshankranti.sanghatna;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


@SuppressLint("Registered")
public class FullscreenActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Animation animBlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("ShikshanKrani", 0);
        final String status = settings.getString("MemberId", "");
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE, View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_fullscreen);
        ImageView mimageView = findViewById(R.id.img_logo);
        TextView mTV_Welcome = findViewById(R.id.tv_welcome);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        mimageView.startAnimation(animBlink);
        mTV_Welcome.startAnimation(animBlink);
        mimageView.setVisibility(View.VISIBLE);
        mTV_Welcome.setVisibility(View.VISIBLE);
        // Using handler with postDelayed called runnable run method
        new Handler().postDelayed(() -> {
            animBlink.cancel();
            if (!status.isEmpty()) {
                Intent i = new Intent(FullscreenActivity.this, Select_language.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(FullscreenActivity.this, Select_language.class);
                startActivity(i);
                finish();
            }


        }, 2 * 1000); // wait for 3 seconds

    }


}

