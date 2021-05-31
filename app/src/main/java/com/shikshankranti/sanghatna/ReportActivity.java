package com.shikshankranti.sanghatna;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;
import com.shikshankranti.sanghatna.database.UsersDetails;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.Address;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.DOB;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.District;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.MemberID;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.Name;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.PhotoPath;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.PinCode;
import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.Taluka;

public class ReportActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private String toSpeak;
    Locale loc;
    ImageView mIVQrCode;
    CircleImageView mIVPhoto;
    TextView mTVLocation, mTvMemberID;
    MaterialButton mbtnChangeDetails;
    MaterialButton mbtnShareID;
    String uuid;
    DatabaseReference mDatabase;


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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        CardView cardView = findViewById(R.id.cvIDCard);
        mIVQrCode = findViewById(R.id.ivqrcode);
        mIVPhoto = findViewById(R.id.ivPhoto);
        mTVLocation = findViewById(R.id.tvLocation);
        mTvMemberID = findViewById(R.id.tvMemberID);
        //  mbtnDownloadIDCard = findViewById(R.id.btnDownloadIDCard);
        mbtnChangeDetails = findViewById(R.id.btnChangeDetails);
        mbtnShareID = findViewById(R.id.btnShareID);
//        mTvMemberID.setText(uuid.substring(0, 5));
        mbtnShareID.setOnClickListener(v -> {
            // Share image
            loadView(cardView);
            shareImage(Uri.parse(Environment.getExternalStorageState() + "/shikshankranti.jpg"));
        });

        mbtnChangeDetails.setOnClickListener(v -> {
            Intent changedetails = new Intent(ReportActivity.this, RegisterForm.class);
            ReportActivity.this.startActivity(changedetails);
            ReportActivity.this.finish();
        });
        mTVLocation.setText("Location : " + PatientDetailsAbstractClass.District.toUpperCase() + "," + PatientDetailsAbstractClass.Taluka.toUpperCase());
        if (PatientDetailsAbstractClass.Gallery) {
            mIVPhoto.setImageURI(PatientDetailsAbstractClass.GalleryPhoto);
        } else {
            mIVPhoto.setImageBitmap(PatientDetailsAbstractClass.Photo);

        }


        String fileName = Environment.getExternalStorageDirectory()
                + "/ShikshanKranti";
        File f = new File(fileName);
        if (!f.exists())
            f.mkdir();
        final Locale loc = new Locale("hin", "IND");

        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(loc);
                if (Select_language.langselected == 0) {
                    toSpeak = "Please View or Share ID Card";
                } else {
                    toSpeak = "कृपया ओळखपत्र पहा किंवा डाउनलोड करा";
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
        mTVDob.setText(PatientDetailsAbstractClass.DOB);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mCloseBtn.setEnabled(true);
        String gender = "";
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
                mDatabase = FirebaseDatabase.getInstance().getReference();
            writeNewUser(PatientDetailsAbstractClass.Number,MemberID,Name,PatientDetailsAbstractClass.Number,Address,DOB,District,Taluka,PinCode,PhotoPath);

            Intent i = new Intent(ReportActivity.this, FullscreenActivity.class);
            ReportActivity.this.startActivity(i);
            ReportActivity.this.finish();


        });

    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredWidth() + 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }

    public void loadView(CardView cardView) {
        try {
            cardView.setDrawingCacheEnabled(true);
            Bitmap bitmap = loadBitmapFromView(cardView);
            cardView.setDrawingCacheEnabled(false);
            String mPath =
                    Environment.getExternalStorageDirectory().toString() + "/shikshankranti.jpg";
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new
                    FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void shareImage(Uri imagePath) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/*");
//set your message
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, imagePath);

        String imgpath = Environment.getExternalStorageDirectory() + File.separator + "shikshankranti.jpg";

        File imageFileToShare = new File(imgpath);

        Uri uri = Uri.fromFile(imageFileToShare);

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        try { // should you to check Whatsapp is installed or not
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException exception) {
            exception.printStackTrace();
        }
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
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 8;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder("Name " + Name + " DOB" + PatientDetailsAbstractClass.DOB, null, QRGContents.Type.TEXT, dimen);
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

    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    final Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
    final String strDate = mdformat.format(calendar.getTime());

    public void writeNewUser(String userId,String memberid,String name,String number, String Address,String dob,String dist,String tal,String pincode,String photopath) {
        UsersDetails user = new UsersDetails(userId,memberid,name,number, Address,dob,  dist, tal, pincode, photopath);

        mDatabase.child("users").child(userId).setValue(user);
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


}
