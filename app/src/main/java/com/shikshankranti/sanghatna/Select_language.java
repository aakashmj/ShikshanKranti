package com.shikshankranti.sanghatna;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Select_language extends AppCompatActivity {
    private final List<Integer> blockedKeys = new ArrayList<>(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    public static int langselected = 0;
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private TextToSpeech tts;
    private String toSpeak;
    private final Handler mHandler = new Handler();
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
        UiChangeListener();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
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
            if(status != TextToSpeech.ERROR) {
                tts.setLanguage(loc);
                if(Select_language.langselected==0){
                    toSpeak = "Please Select Language";
                }else {
                    toSpeak = "कृपया भाषा चुनिए  ";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null,null);
                } else {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });

        menglish.setOnClickListener(view -> {
            String lang1 = "en";//Default Language
            switch (view.getId()) {
                case R.id.english:
                    if (tts!=null&&tts.isSpeaking()) {
                        tts.stop();
                    }
                    lang1 = "en";
                    Intent i = new Intent(Select_language.this, TermsActivity.class);
                    langselected=0;
                    startActivity(i);
                    finish();

                    break;
                case R.id.hindi:
                    if (tts!=null&&tts.isSpeaking()) {
                        tts.stop();
                    }
                    lang1 = "hi";
                    Intent j = new Intent(Select_language.this, TermsActivity.class);
                    langselected=1;
                    startActivity(j);
                    finish();

                    break;

            }

            changeLocale(lang1);//Change Locale on selection basis

        });
mhindi.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String lang = "en";//Default Language
        switch (view.getId()) {
            case R.id.english:
                if (tts!=null&&tts.isSpeaking()) {
                    tts.stop();
                }
                lang = "en";
                Intent i = new Intent(Select_language.this, TermsActivity.class);
                langselected=0;
                startActivity(i);
                finish();

                break;
            case R.id.hindi:
                if (tts!=null&&tts.isSpeaking()) {
                    tts.stop();
                }
                lang = "hi";
                Intent j = new Intent(Select_language.this, TermsActivity.class);
                langselected=1;
                startActivity(j);
                finish();

                break;

        }

        changeLocale(lang);//Change Locale on selection basis

    }
});


        loadLocale();
        mCloseBtn.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(Select_language.this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Close?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    Intent i = new Intent(Select_language.this, FullscreenActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
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

    private final Runnable decor_view_settings = new Runnable()
    {
        public void run()
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            mHandler.postDelayed(decor_view_settings, 500);
        }

        return super.onKeyDown(keyCode, event);
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
            sendBroadcast(closeDialog);}



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

    //Update text methods
   /* private void updateTexts() {
        chooseText.setText(R.string.tap_text);
        menglish.setText(R.string.english);
        mhindi.setText(R.string.hindi);
    }*/
    private void UiChangeListener() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        });
    }
}
