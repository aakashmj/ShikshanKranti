package com.shikshankranti.sanghatna;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.Designation;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.Education;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.School;


public class EducationForm extends AppCompatActivity {
    private EditText metEducation, metSchoolname;
    AppCompatSpinner mspDesignations;
    MaterialButton mbtnNext;
    TextView mHeaderHeading;
    private AwesomeValidation awesomeValidation;
    SharedPreferences sharedPref;
    String sdesignation, sschool, seducation;

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
        setContentView(R.layout.educationform_activity);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("TEACHER");
        arrayList.add("PRINCIPAL");
        arrayList.add("Other Staff");

        sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key), Context.MODE_PRIVATE);
        seducation = sharedPref.getString("education", Education);
        sschool = sharedPref.getString("school", School);
        sdesignation = sharedPref.getString("designation", Designation);

        metEducation = findViewById(R.id.etEducation);
        metEducation.setText(seducation);
        metSchoolname = findViewById(R.id.etSchoolName);
        metSchoolname.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(metSchoolname, InputMethodManager.SHOW_IMPLICIT);
        metSchoolname.setText(sschool);
        mspDesignations = findViewById(R.id.spDesignation);
        // create list of customer
        ArrayList<String> customerList = getDesignationList();

        //Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EducationForm.this, R.layout.spinner_item, customerList);

        //Set adapter
        mspDesignations.setAdapter(adapter);
        if (sdesignation != null) {
            int spinnerPosition = adapter.getPosition(sdesignation);
            mspDesignations.setSelection(spinnerPosition);
        }

        /*if (sdesignation.contentEquals("मुख्याध्यापक")||sdesignation.contentEquals("Principal")) {
            mspDesignations.setSelection(0);
        } else if (sdesignation.contentEquals("शिक्षक")||sdesignation.contentEquals("Teacher")) {
            mspDesignations.setSelection(1);

        } else {
            mspDesignations.setSelection(2);
        }*/
        mspDesignations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    sdesignation = String.valueOf(R.string.principal);
                } else if (position == 1) {
                    sdesignation = String.valueOf(R.string.teacher);
                } else {
                    sdesignation = String.valueOf(R.string.otherteacher);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //submit button click event registration
        mbtnNext = findViewById(R.id.btnNext);
        // mPreviousButton=findViewById(R.id.previousButton);
        mHeaderHeading = findViewById(R.id.headerheaeding);
        mHeaderHeading.setText(R.string.profiledetails);
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mbtnNext.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                School = metSchoolname.getText().toString().trim();
                Education = metEducation.getText().toString().trim();
                Designation = mspDesignations.getSelectedItem().toString();
                editor = sharedPref.edit();
                editor.putString("education", Education);
                editor.putString("school", School);
                editor.putString("designation", Designation);
                editor.apply();
                Intent addressintent = new Intent(EducationForm.this, AddressActivity.class);
                startActivity(addressintent);
                finish();
            }

        });
        mCloseBtn.setOnClickListener(view -> {

            Intent i = new Intent(EducationForm.this, FullscreenActivity.class);
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

    private ArrayList<String> getDesignationList() {
        ArrayList<String> designations = new ArrayList<>();
        designations.add(getString(R.string.principal));
        designations.add(getString(R.string.teacher));
        designations.add((getString(R.string.otherteacher)));
        return designations;
    }

}