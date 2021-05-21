package com.shikshankranti.sanghatna;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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


public class AddressActivity extends AppCompatActivity {

    private EditText mETPermAddress, mETDistrict, mETTaluka, mETPinCode;
    MaterialButton mbtnNext;
    private AwesomeValidation awesomeValidation;
    private TextToSpeech tts;
    private String toSpeak;

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
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(loc);
                if (Select_language.langselected == 0) {
                    toSpeak = "Please Fill Address Details";
                } else {
                    toSpeak = "कृपया पत्ता तपशील भरा";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });

        mETPermAddress = findViewById(R.id.etPermAddress);
        mETDistrict = findViewById(R.id.etDistrict);
        mETTaluka = findViewById(R.id.etTaluka);
        mETPinCode = findViewById(R.id.etPinCode);
        mbtnNext = findViewById(R.id.btnNext);
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);

        awesomeValidation.addValidation(this, R.id.etPermAddress, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.etDistrict, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.etTaluka, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        //    awesomeValidation.addValidation(this, R.id.etPinCode, "^[+]?[0-9]{10,13}$", R.string.mobileerror);

        mbtnNext.setOnClickListener(v -> {
            if (tts.isSpeaking() && tts != null) {
                tts.stop();
                tts.shutdown();
            }
            if (awesomeValidation.validate()) {
                PatientDetailsAbstractClass.Address = mETPermAddress.getText().toString();
                PatientDetailsAbstractClass.District = mETDistrict.getText().toString();
                PatientDetailsAbstractClass.Taluka = mETTaluka.getText().toString();
                PatientDetailsAbstractClass.PinCode = mETPinCode.getText().toString();

                Intent i = new Intent(AddressActivity.this, CaptureActivity.class);
                startActivity(i);
                finish();
            }

        });


        mCloseBtn.setOnClickListener(view -> {
            if (tts.isSpeaking() && tts != null) {
                tts.stop();
                tts.shutdown();
            }

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