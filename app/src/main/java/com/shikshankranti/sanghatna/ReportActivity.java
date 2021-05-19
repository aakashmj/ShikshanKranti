package com.shikshankranti.sanghatna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.app.ProgressDialog.show;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.Name;

public class ReportActivity extends AppCompatActivity {
    private Date curDate;
    private TextToSpeech tts;
    private String toSpeak;
    private String FinalSMS = "";

    private String gender = "";

    Locale loc;
    ImageView mIVQrCode, mIVPhoto;
    private static final String Locale_Preference = "carenestsettings";
    String HtmlEmail = "<b>Dear </b>" + Name + ","
            + "<br /><b>Thanks for visiting Caremate Mini KIOSK. </b><br />"
            + "<br /><b>Your Test Results : </b> <br />"
            + "<br /><b>Name : </b>" + Name + ", " + PatientDetailsAbstractClass.Gender
            + "<br /><b>Mobile Number : </b>" + PatientDetailsAbstractClass.Number + ", "
            + "<br />" + " "
            + "<br /><b>Good to see you again! </b><br/>"
            + "<br /><b> Regards, </b>"
            + "<br /><b> Team Kindraj Corporation!</b>"
            + "<br /><b>www.shikshankrantisanghatna.com </b>"
            + "<br /><b><a href=\\\"http://www.metsl.in\\\">Powered by METSL</a></b>";

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
        setContentView(R.layout.finalreport_layout);
        mIVQrCode = findViewById(R.id.ivqrcode);
        mIVPhoto = findViewById(R.id.ivPhoto);
        if (PatientDetailsAbstractClass.Gallery) {
            mIVPhoto.setImageURI(PatientDetailsAbstractClass.GalleryPhoto);
        } else {
            mIVPhoto.setImageBitmap(PatientDetailsAbstractClass.Photo);

        }
        curDate = new Date();
        String fileName = Environment.getExternalStorageDirectory()
                + "/CarematePi";
        File f = new File(fileName);
        if (!f.exists())
            f.mkdir();
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(loc);
                if (Select_language.langselected == 0) {
                    toSpeak = "Please Find Test Report";
                } else {
                    toSpeak = "कृपया परीक्षण रिपोर्ट देखिये";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        TextView mNameTextView = findViewById(R.id.tvMemberName);
        TextView mTVDob = findViewById(R.id.tvDob);
        mNameTextView.setText(Name);
         mTVDob.setText(PatientDetailsAbstractClass.Age);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);


        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mCloseBtn.setEnabled(true);
        if (PatientDetailsAbstractClass.Gender.contains("F")) {
            gender = "Female";
        } else {
            gender = "Male";
        }
        mCloseBtn.setOnClickListener(v -> {
            mCloseBtn.setEnabled(false);
            if (tts.isSpeaking() && tts != null) {
                tts.stop();
                tts.shutdown();
            }

            Intent i = new Intent(ReportActivity.this, FullscreenActivity.class);
            ReportActivity.this.startActivity(i);
            ReportActivity.this.finish();


        });
        final String welcome = " " + "\n" + "\n" + "      *Welcome to Kiosk*       ";

    }

    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onStart() {
        super.onStart();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(Name, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            mIVQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
/*
        showWorkingDialog();
        new Handler().postDelayed(() -> removeWorkingDialog(), 2000);
*/

    }

    private ProgressDialog working_dialog;

    private void showWorkingDialog() {
        working_dialog = show(ReportActivity.this, "", "Report Generating..", true);
    }

    private void showPrintingDialog() {
        working_dialog = show(ReportActivity.this, "", "Report getting Printed.", true);
    }

    private void removeWorkingDialog() {
        if (working_dialog != null) {
            working_dialog.dismiss();
            working_dialog = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


        //unbindService(usbConnection);


    }

    private float weight;
    private float height;


    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager;
        printManager = (PrintManager) getBaseContext()
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            printAdapter = webView.createPrintDocumentAdapter(jobName);
        }

        // Create a print job with name and adapter instance
        PrintJob printJob;
        printJob = Objects.requireNonNull(printManager).print(jobName, Objects.requireNonNull(printAdapter),
                new PrintAttributes.Builder().build());
        printJob.restart();
        // Save the job object for later status checking
        //mPrintJobs.add(printJob);
    }

    private WebView mWebView;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
    final String strDate = mdformat.format(calendar.getTime());

    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(getApplicationContext());
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                mWebView = null;
            }
        });
        // Generate an HTML document on the fly:
        String htmlDocument = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Caremate Report </title><link href=\"reportcss.css\" rel=\"stylesheet\"><style type=\"text/css\">body {padding-top: 60px;padding-bottom: 40px;}.fixed-header, .fixed-footer {width: 100%;position: fixed;padding: 10px 0;color: #fff;}.fixed-header {top: 0;}.fixed-footer {bottom: 0;position:fixed;}.container {width: 80%;margin: 0 auto;}</style></head><body style=\"font-family: Segoe UI\"><div class=\"fixed-header\"><img src=\"maestroslogo.png\"></div><br><div class=\"detdiv\"><div style=\"width: 30%; float: left\"><span style=\"float:left\"><img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALAAAACkCAYAAAA38KTTAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MUE3QUJBRTMyOTQxMTFFNUEyREE5MDExNTM4QTU0MjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MUE3QUJBRTQyOTQxMTFFNUEyREE5MDExNTM4QTU0MjYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoxQTdBQkFFMTI5NDExMUU1QTJEQTkwMTE1MzhBNTQyNiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoxQTdBQkFFMjI5NDExMUU1QTJEQTkwMTE1MzhBNTQyNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PuO4er8AABiASURBVHja7F3ZjhvH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\" width=\"80\" height=\"80\"> </span><span style=\"font-size: 16px; color: #555555;margin-left:5px\">" + Name + "</span><br><span style=\"font-size: 12px; color: #555555;margin-left:5px\">" + PatientDetailsAbstractClass.Age + " years</span><br><span style=\"font-size: 12px; color: #555555;margin-left:5px\">" + strDate + "</span></div><div style=\"width: 20%; float: left\"><span style=\"float: left\"><img src=\"locationred.png\" width=\"35\" height=\"35\"></span><span style=\"float: left\">Caremate Mini</span><br><span style=\"float: left;font-weight:bold\">Chennai</span></div><div style=\"width: 25%; float: left\"><span style=\"float: left\"><img src=\"HC.png\" width=\"35\" height=\"35\"></span><span style=\"float: left\">Health Checkup</span><br><span style=\"float: left;font-weight:bold\">Pranav Hospital</span></div><div style=\"width: 10%; float: left\"><span style=\"float: left\"><img src=\"ic_weight.png\" width=\"35\" height=\"35\"></span><span style=\"float: left\">" + "100" + " kg</span><br><span style=\"float: left;font-weight:bold\">Weight</span></div><div style=\"width: 10%; float: left\"><span style=\"float: left\"><img src=\"ic_height.png\" width=\"35\" height=\"35\"></span><span style=\"float: left\">" + "50" + " cms</span><br><span style=\"float: left;font-weight:bold\">Height</span></div></div><div class=\"\" style=\"height:680px;\"><div class=\"maindiv\"><div class=\"topdiv\" align=\"center\"><div class=\"col12\" style=\"float: left; min-height: 1px; padding-left: 30px; padding-right: 15px; \"><div class=\"toppnl\"><div class=\"col12\"><img src=\"ic_spotwo.png\" class=\"spanimg\"><span class=\"spanval\">98 %</span><span class=\"spantitle\">SPO2</span></div><div class=\"spanrange\"><span style=\"padding-left:5px\">Normal Range: 90-100 %</span></div></div><div class=\"toppnl\"><div class=\"col12\"><img src=\"ic_pulserate.png\" class=\"spanimg\"><span class=\"spanval\">" + "72" + " bpm</span><span class=\"spantitle\">Pulse Rate</span></div><div class=\"spanrange\"><span style=\"padding-left:5px\">Normal Range: 70-90 bpm</span></div></div><div class=\"toppnl\"><div class=\"col12\"><img src=\"ic_temperature.png\" class=\"spanimg\"><span class=\"spanval\">" + "Temp" + " <sup>o</sup>F</span><span class=\"spantitle\">Temperature</span></div><div class=\"spanrange\"><span style=\"padding-left:5px\">Normal Range: 97-99 <sup>o</sup>F</span></div></div></div></div><div class=\"maindatadiv\"><div class=\"col12\"><div class=\"dataspan\"><div class=\"dataspantilebar\"><img src=\"ic_bmiscale.png\" class=\"dataspanimage\"><span class=\"dataspantitle\">BMI</span><span class=\"dataspanvalue\">" + "bmi" + " Kg/m<sup>2</sup></span></div><table class=\"dataspanrangetbl\" cellpadding=\"0\"><tbody><tr class=\"dataspanrangeth\"><td style=\"text-align: center;  \">Range</td><td style=\"text-align: center;  \">Value</td></tr><tr class=\"dataspanrangetr\"><td>&lt; 18.5</td><td>Underweight</td></tr><tr class=\"dataspanrangetr\"><td>18.5-25</td><td>Healthy</td></tr><tr class=\"dataspanrangetr\"><td>25-29.9</td><td>Overweight</td></tr><tr class=\"dataspanrangetrred\"><td style=\"color: #fff;\">&gt; 30</td><td style=\"color: #fff;\">Obese</td></tr></tbody></table></div><div class=\"dataspan\"><div class=\"dataspantilebar\"><img src=\"ic_systolic.png\" class=\"dataspanimage\"><span class=\"dataspantitle\">BP</span><span class=\"dataspanvalue\">" + "bp" + " mmHg</span></div><table class=\"dataspanrangetbl\" cellpadding=\"0\"><tbody><tr class=\"dataspanrangeth\"><td style=\"text-align: center;  \">Range</td><td style=\"text-align: center;  \"></td><td style=\"text-align: center;  \">Value</td></tr><tr class=\"dataspanrangeth\"><td style=\"text-align: center;  \">Systolic</td><td style=\"text-align: center;  \">Diastolic</td><td style=\"text-align: center;  \"> </td></tr><tr class=\"dataspanrangetr\"><td>&lt; 90</td><td>&lt; 60</td><td>Low</td></tr><tr class=\"dataspanrangetr\"><td>90-120</td><td>60-80</td><td>Normal</td></tr><tr class=\"dataspanrangetr\"><td>120-139</td><td>80-89</td><td>Pre-hypertension</td></tr><tr class=\"dataspanrangetrred\"><td style=\"color: #fff;\">&gt; 139</td><td style=\"color: #fff;\">&gt; 89</td><td style=\"color: #fff;\">High</td></tr></tbody></table></div></div></div></div></div><hr><div style=\"\n" + "float: right;\n" + "width: 30%;\n" + "\"><h3>Authority Signature</h3></div></body></html>";

        // Generate an HTML document on the fly:
        String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + htmlDocument;
// lets assume we have /assets/style.css file
        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    public boolean checkInternetconn() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo == null) {

                Toast.makeText(getApplicationContext(), "Internet Not Connected", Toast.LENGTH_SHORT).show();
                return false;

            } else {

                return true;
            }
        }
    }
}
