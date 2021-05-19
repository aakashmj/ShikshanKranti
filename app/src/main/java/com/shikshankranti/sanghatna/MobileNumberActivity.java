package com.shikshankranti.sanghatna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;

public class MobileNumberActivity extends AppCompatActivity {

    MaterialButton mbtnSubmit;
    EditText mETMobile,mETOTP;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        setContentView(R.layout.activity_mobile_number);
        mETMobile=findViewById(R.id.etMobileNumber);
        mETOTP=findViewById(R.id.etOTP);
        awesomeValidation.addValidation(this, R.id.etMobileNumber, "^[+]?[0-9]{10,13}$", R.string.mobileerror);

        mbtnSubmit = findViewById(R.id.btnSubmit);
        mbtnSubmit.setOnClickListener(v -> {
            if(awesomeValidation.validate()) {
                PatientDetailsAbstractClass.Number = mETMobile.getText().toString();
                Intent i = new Intent(MobileNumberActivity.this, RegisterForm.class);
                MobileNumberActivity.this.startActivity(i);
                MobileNumberActivity.this.finish();
            }
        });




    }
}