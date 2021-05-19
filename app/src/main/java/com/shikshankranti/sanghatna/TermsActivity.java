package com.shikshankranti.sanghatna;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;


@SuppressLint("Registered")
public class TermsActivity extends AppCompatActivity {
    MaterialButton mbtnNext;
    CheckBox mcbAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("ShikshanKrani", 0);
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
        setContentView(R.layout.terms_activity);
        mcbAgree = findViewById(R.id.cbagree);
        mbtnNext = findViewById(R.id.nextButton);

        // Using handler with postDelayed called runnable run method

    }

    @Override
    protected void onStart() {
        super.onStart();

        mbtnNext.setOnClickListener(v -> {
            if (mcbAgree.isChecked()) {
                Intent i = new Intent(TermsActivity.this, MobileNumberActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(TermsActivity.this, "Please Agree Terms and Conditions", Toast.LENGTH_LONG).show();

            }
        });


    }
}

