package com.shikshankranti.sanghatna;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class MobileNumberActivity extends AppCompatActivity {

    MaterialButton mbtnSubmit;
    EditText mETMobile;
    private AwesomeValidation awesomeValidation;
    private TextToSpeech tts;
    private String toSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        setContentView(R.layout.activity_mobile_number);
        final Locale loc = new Locale("hin", "IND");
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(loc);
                if (Select_language.langselected == 0) {
                    toSpeak = "Please Accept Terms and Conditions";
                } else {
                    toSpeak = "कृपया अटी व शर्ती स्वीकारा";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });

        mETMobile = findViewById(R.id.etMobileNumber);
        awesomeValidation.addValidation(this, R.id.etMobileNumber, "^[+]?[0-9]{10,13}$", R.string.mobileerror);

        mbtnSubmit = findViewById(R.id.btnSubmit);
        mbtnSubmit.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                PatientDetailsAbstractClass.Number = mETMobile.getText().toString();
                Intent pintent = new Intent(MobileNumberActivity.this, VerificationActivity.class);
                pintent.putExtra("phonenumber", PatientDetailsAbstractClass.Number);
                MobileNumberActivity.this.startActivity(pintent);
                MobileNumberActivity.this.finish();
            }
        });


    }
}