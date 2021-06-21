package com.shikshankranti.sanghatna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.District;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.PinCode;


public class AddressActivity extends AppCompatActivity {

    private EditText mETPermAddress, mETDistrict, mETTaluka, mETPinCode;
    MaterialButton mbtnNext;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE, View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.address_layout);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        final Locale loc = new Locale("hin", "IND");

        mETPermAddress = findViewById(R.id.etPermAddress);
        mETDistrict = findViewById(R.id.etDistrict);
        mETTaluka = findViewById(R.id.etTaluka);
        mETPinCode = findViewById(R.id.etPinCode);
        mbtnNext = findViewById(R.id.btnNext);
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);

       // awesomeValidation.addValidation(this, R.id.etPermAddress, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        // awesomeValidation.addValidation(this, R.id.etDistrict, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        // awesomeValidation.addValidation(this, R.id.etTaluka, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        //    awesomeValidation.addValidation(this, R.id.etPinCode, "^[+]?[0-9]{10,13}$", R.string.mobileerror);

        mbtnNext.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                PatientDetailsAbstractClass.Address = mETPermAddress.getText().toString();
                District = mETDistrict.getText().toString();
                PatientDetailsAbstractClass.Taluka = mETTaluka.getText().toString();
                PatientDetailsAbstractClass.PinCode = mETPinCode.getText().toString();
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("address",PatientDetailsAbstractClass.Address);
                editor.putString("district", District);
                editor.putString("taluka",PatientDetailsAbstractClass.Taluka);
                editor.putString("pincode",PinCode);
                editor.apply();
                Intent i = new Intent(AddressActivity.this, SangeetaCaptureActivity.class);
                startActivity(i);
                finish();
            }

        });


        mCloseBtn.setOnClickListener(view -> {

            Intent i = new Intent(AddressActivity.this, FullscreenActivity.class);
            startActivity(i);
            finish();


        });
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }


}