package com.shikshankranti.sanghatna;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MobileNumberActivity extends AppCompatActivity {

    MaterialButton mbtnSubmit;
    EditText mETMobile;
    private AwesomeValidation awesomeValidation;
    private TextToSpeech tts;
    private String toSpeak;
    private FirebaseAuth mAuth;

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
                    toSpeak = "Please Enter Mobile Number";
                } else {
                    toSpeak = "कृपया मोबाइल नंबर भरा";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mCloseBtn.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(MobileNumberActivity.this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Close?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //if user pressed "yes", then he is allowed to exit from application
                Intent i = new Intent(MobileNumberActivity.this, FullscreenActivity.class);
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
       /* mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            // User is signed in
        } else {
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            // No user is signed in
        }
        FirebaseAuth.getInstance().signOut();
*/
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