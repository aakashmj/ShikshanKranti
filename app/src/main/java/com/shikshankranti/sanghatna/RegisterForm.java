package com.shikshankranti.sanghatna;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class RegisterForm extends AppCompatActivity {
    private EditText metFirstName, metMiddleName, metLastName;
    MaterialButton mbtnNext;
    TextView mETDOB;

    private TextToSpeech tts;
    private String toSpeak;
    TextView mHeaderHeading;
    Locale loc;
    private AwesomeValidation awesomeValidation;
    int year;
    int month;
    int day;
    Calendar myCalendar;


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
        setContentView(R.layout.profiledetails_activity);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        final Locale loc = new Locale("hin", "IND");

        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(loc);
                if (Select_language.langselected == 0) {
                    toSpeak = "Please Register the Form";
                } else {
                    toSpeak = "कृपया रजिस्टर फॉर्म भरा";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        metFirstName = findViewById(R.id.etFirstName);
        metMiddleName = findViewById(R.id.etMiddleName);
        metLastName = findViewById(R.id.etLastName);
        mETDOB = findViewById(R.id.etDOB);

        myCalendar = new GregorianCalendar();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;
            updateLabel();
        };

        mETDOB.setOnClickListener(v -> {
            new DatePickerDialog(RegisterForm.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        });

        mbtnNext = findViewById(R.id.btnNext);
        // mPreviousButton=findViewById(R.id.previousButton);
        mHeaderHeading = findViewById(R.id.headerheaeding);
        mHeaderHeading.setText(R.string.profiledetails);


        ImageButton mCloseBtn = findViewById(R.id.closeBtn);


        //    awesomeValidation.addValidation(this, R.id.etFirstName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        //  awesomeValidation.addValidation(this, R.id.etMiddleName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        // awesomeValidation.addValidation(this, R.id.etLastName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        //  awesomeValidation.addValidation(this, R.id.etDOB, "^[+]?[0-9]{10,13}$", R.string.mobileerror);


        mbtnNext.setOnClickListener(v -> {
            if (tts.isSpeaking() && tts != null) {
                tts.stop();
                tts.shutdown();
            }
            if (awesomeValidation.validate()) {
                PatientDetailsAbstractClass.Name = metFirstName.getText().toString().trim() + " " + metMiddleName.getText().toString().trim() + " " + metLastName.getText().toString();
                PatientDetailsAbstractClass.DOB = mETDOB.getText().toString().trim();
                Intent addressintent = new Intent(RegisterForm.this, AddressActivity.class);
                startActivity(addressintent);
                finish();
            }

        });
        mCloseBtn.setOnClickListener(view -> {

            if (tts.isSpeaking()) {
                tts.stop();
            }
            Intent i = new Intent(RegisterForm.this, FullscreenActivity.class);
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


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    public static class CustomViewGroup extends ViewGroup {
        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            // Intercepted touch!
            return true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    private String getAge(int year, int month, int day) {
        Log.i("getAge", day + "/" + month + "/" + year);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        int ageInt = age;
        return Integer.toString(ageInt);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mETDOB.setText(sdf.format(myCalendar.getTime()));
    }

}