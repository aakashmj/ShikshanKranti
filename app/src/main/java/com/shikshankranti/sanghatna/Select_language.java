package com.shikshankranti.sanghatna;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Select_language extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private final List<Integer> blockedKeys = new ArrayList<>(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    public static int langselected = 0;
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private TextToSpeech tts;
    private String toSpeak;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

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

        setContentView(R.layout.activity_selectlanguage);
        checkAndRequestPermissions();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key),Context.MODE_PRIVATE);
        String authstatus = sharedPref.getString("deviceauthstatus", "no");
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = sharedPref.getString("LANG", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        MaterialButton menglish = findViewById(R.id.english);
        MaterialButton mhindi = findViewById(R.id.hindi);
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        editor.clear();
        final Locale loc = new Locale("hin", "IND");
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(loc);
                if (Select_language.langselected == 0) {
                    toSpeak = "Please Select Language";
                } else {
                    toSpeak = "कृपया भाषा चुनिए  ";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });

        menglish.setOnClickListener(view -> {
            String lang1 = "en";//Default Language
            switch (view.getId()) {
                case R.id.english:
                    if(authstatus.contains("verified")){
                        Intent i = new Intent(Select_language.this, RegisterForm.class);
                        startActivity(i);
                        finish();
                    }else {
                        if (tts != null && tts.isSpeaking()) {
                            tts.stop();
                        }
                        lang1 = "en";
                        Intent i = new Intent(Select_language.this, TermsActivity.class);
                        langselected = 0;
                        startActivity(i);
                        finish();
                    }

                    break;
                case R.id.hindi:
                    if(authstatus.contains("verified")){
                        Intent i = new Intent(Select_language.this, RegisterForm.class);
                        startActivity(i);
                        finish();
                    }else {
                        if (tts != null && tts.isSpeaking()) {
                            tts.stop();
                        }
                        lang1 = "hi";
                        Intent j = new Intent(Select_language.this, TermsActivity.class);
                        langselected = 1;
                        startActivity(j);
                        finish();
                    }

                    break;

            }

            changeLocale(lang1);//Change Locale on selection basis

        });
        mhindi.setOnClickListener(view -> {
            String lang12 = "en";//Default Language
            switch (view.getId()) {
                case R.id.english:
                    if (tts != null && tts.isSpeaking()) {
                        tts.stop();
                    }
                    lang12 = "en";
                    Intent i = new Intent(Select_language.this, TermsActivity.class);
                    langselected = 0;
                    startActivity(i);
                    finish();

                    break;
                case R.id.hindi:
                    if (tts != null && tts.isSpeaking()) {
                        tts.stop();
                    }
                    lang12 = "hi";
                    Intent j = new Intent(Select_language.this, TermsActivity.class);
                    langselected = 1;
                    startActivity(j);
                    finish();

                    break;

            }

            changeLocale(lang12);//Change Locale on selection basis

        });


        loadLocale();
        mCloseBtn.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(Select_language.this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Close?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //if user pressed "yes", then he is allowed to exit from application
                Intent i = new Intent(Select_language.this, FullscreenActivity.class);
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



       /* boolean net_status = checkInternetconn();

        if(net_status) {

            if (android.os.Build.VERSION.SDK_INT <= 26) {
                Intent uploadintent = UploderService.newSvcIntent(Select_language.this
                );
                startService(uploadintent);
            }
        }*/

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return blockedKeys.contains(event.getKeyCode()) || super.dispatchKeyEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!hasFocus) {

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }


    }


    //Change Locale
    private void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        //   updateTexts();//Update texts according to locale
    }

    //Save locale method in preferences
    private void saveLocale(String lang) {
        editor.putString(Locale_KeyValue, lang);
        editor.commit();
    }

    //Get locale method in preferences
    private void loadLocale() {
        String language = sharedPreferences.getString(Locale_KeyValue, "");
        changeLocale(language);
    }


    private void checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readstorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readstorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }


    }


}
