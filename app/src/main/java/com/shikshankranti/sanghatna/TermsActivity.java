package com.shikshankranti.sanghatna;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;


@SuppressLint("Registered")
public class TermsActivity extends AppCompatActivity {
    MaterialButton mbtnNext;
    CheckBox mcbAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final Locale loc = new Locale("hin", "IND");

        mcbAgree = findViewById(R.id.cbagree);
        mbtnNext = findViewById(R.id.nextButton);
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mCloseBtn.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(TermsActivity.this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Close?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //if user pressed "yes", then he is allowed to exit from application
                Intent i = new Intent(TermsActivity.this, FullscreenActivity.class);
                startActivity(i);
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            });
            AlertDialog alert = builder.create();
            alert.show();
        });

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

