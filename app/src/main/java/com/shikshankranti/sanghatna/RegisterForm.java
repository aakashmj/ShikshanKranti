package com.shikshankranti.sanghatna;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.Calendar;

import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.DOB;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.FName;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.LName;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.MName;


public class RegisterForm extends AppCompatActivity {
    private EditText metFirstName, metMiddleName, metLastName;
    MaterialButton mbtnNext;
    EditText mETDOB;
    TextView mHeaderHeading;
    private AwesomeValidation awesomeValidation;
    SharedPreferences sharedPref;
    String sdob, sfname, smname,slname;
    SharedPreferences.Editor editor;


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

        sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key), Context.MODE_PRIVATE);
        sfname = sharedPref.getString("fname", FName);
        smname = sharedPref.getString("mname", MName);
        slname = sharedPref.getString("lname", LName);
        sdob = sharedPref.getString("dob", DOB);

        metFirstName = findViewById(R.id.etFirstName);
        metFirstName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(metFirstName, InputMethodManager.SHOW_IMPLICIT);
        metFirstName.setText(sfname);
        metMiddleName = findViewById(R.id.etMiddleName);
        metMiddleName.setText(smname);
        metLastName = findViewById(R.id.etLastName);
        metLastName.setText(slname);
        mETDOB = findViewById(R.id.etDOB);
        mETDOB.setText(sdob);
        //submit button click event registration
        new DateInputMask(mETDOB);
        mbtnNext = findViewById(R.id.btnNext);
        // mPreviousButton=findViewById(R.id.previousButton);
        mHeaderHeading = findViewById(R.id.headerheaeding);
        mHeaderHeading.setText(R.string.profiledetails);
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mbtnNext.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                FName = metFirstName.getText().toString().trim();
                MName = metMiddleName.getText().toString().trim();
                LName = metLastName.getText().toString().trim();
                PatientDetailsAbstractClass.DOB = mETDOB.getText().toString().trim();
                editor = sharedPref.edit();
                editor.putString("fname", FName);
                editor.putString("mname", MName);
                editor.putString("lname", LName);
                editor.putString("dob", PatientDetailsAbstractClass.DOB);
                editor.apply();
                Intent addressintent = new Intent(RegisterForm.this, EducationForm.class);
                startActivity(addressintent);
                finish();
            }

        });
        mCloseBtn.setOnClickListener(view -> {

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

    private ArrayList<String> getDesignationList()
    {
        ArrayList<String> designations = new ArrayList<>();
        designations.add(getString(R.string.principal));
        designations.add(getString(R.string.teacher));
        designations.add((getString(R.string.otherteacher)));
        return designations;
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


    public static class DateInputMask implements TextWatcher {

        private String current = "";
        private final Calendar cal = Calendar.getInstance();
        private final EditText input;

        public DateInputMask(EditText input) {
            this.input = input;
            this.input.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().equals(current)) {
                return;
            }

            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8) {
                String ddmmyyyy = "DDMMYYYY";
                clean = clean + ddmmyyyy.substring(clean.length());
            } else {
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day = Integer.parseInt(clean.substring(0, 2));
                int mon = Integer.parseInt(clean.substring(2, 4));
                int year = Integer.parseInt(clean.substring(4, 8));

                mon = mon < 1 ? 1 : Math.min(mon, 12);
                cal.set(Calendar.MONTH, mon - 1);
                year = (year < 1900) ? 1900 : Math.min(year, 2100);
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                clean = String.format("%02d%02d%02d", day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = Math.max(sel, 0);
            current = clean;
            input.setText(current);
            input.setSelection(Math.min(sel, current.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}