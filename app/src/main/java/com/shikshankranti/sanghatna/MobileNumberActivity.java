package com.shikshankranti.sanghatna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

public class MobileNumberActivity extends AppCompatActivity {

    MaterialButton mbtnSubmit;
    EditText mETMobile,mETOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        mETMobile=findViewById(R.id.etMobileNumber);
        mETOTP=findViewById(R.id.etOTP);
        mbtnSubmit = findViewById(R.id.btnSubmit);
        mbtnSubmit.setOnClickListener(v -> {
            PatientDetailsAbstractClass.Number=mETMobile.getText().toString();
            Intent i = new Intent(MobileNumberActivity.this, RegisterForm.class);
            MobileNumberActivity.this.startActivity(i);
            MobileNumberActivity.this.finish();
        });




    }
}