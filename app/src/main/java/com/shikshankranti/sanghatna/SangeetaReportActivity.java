package com.shikshankranti.sanghatna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;
import com.rey.material.app.Dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

public class SangeetaReportActivity extends AppCompatActivity {
    ImageView mIVQrCode;
    CircleImageView mIVPhoto;
    TextView mTVLocation, mTvMemberID, mNameTextView, mTVDob;
    MaterialButton mbtnChangeDetails;
    AppCompatButton mbtnShareID;
    DatabaseReference mDatabase;

    String smobilenumber, smemberid, sdistrict, saddress, staluka, sdob, sname, sphotopath, spincode;
    private ProgressDialog progessDialog;




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
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key), Context.MODE_PRIVATE);
        sname = sharedPref.getString("name", Name);
        smobilenumber = sharedPref.getString("mobnumber", PatientDetailsAbstractClass.Number);
        sdistrict = sharedPref.getString("district", District);
        saddress = sharedPref.getString("address", Address);
        staluka = sharedPref.getString("taluka", Taluka);
        spincode = sharedPref.getString("pincode", PinCode);
        sdob = sharedPref.getString("dob", DOB);
        sphotopath = sharedPref.getString("fbphotopath", PhotoPath);
        if (smobilenumber.length() < 10) {
            Intent changedetails = new Intent(SangeetaReportActivity.this, MobileNumberActivity.class);
            SangeetaReportActivity.this.startActivity(changedetails);
            SangeetaReportActivity.this.finish();
        }
        this.progessDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);

        if (!progessDialog.isShowing()) {
            progessDialog.setMessage("Generating ID Card...");
            progessDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progessDialog.setIndeterminate(true);
            progessDialog.setCancelable(true);
            progessDialog.show();
        }

       /* photouri = sharedPref.getString("photouri", String.valueOf(GalleryPhoto));
        camerauri = sharedPref.getString("cameraUri",String.valueOf(CameraURI));
*/
        CardView cardView = findViewById(R.id.cvIDCard);
        mIVQrCode = findViewById(R.id.ivqrcode);
        mIVPhoto = findViewById(R.id.ivPhoto);
        mTVLocation = findViewById(R.id.tvLocation);
        mTvMemberID = findViewById(R.id.tvMemberID);
        mbtnChangeDetails = findViewById(R.id.btnChangeDetails);
        mbtnShareID = findViewById(R.id.btnShareID);
        mNameTextView = findViewById(R.id.tvMemberName);
        mTVDob = findViewById(R.id.tvDob);

        MemberID = getDeviceId(SangeetaReportActivity.this);//task.getResult();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("memberid", MemberID);
        editor.apply();
        mTvMemberID.setText(MemberID.substring(0, 8).toUpperCase());
        mNameTextView.setText(sname);
        mTVDob.setText(sdob);
        mTVLocation.setText(sdistrict.trim() + "," + staluka.toUpperCase().trim());

        //  mbtnDownloadIDCard = findViewById(R.id.btnDownloadIDCard);
        mbtnShareID.setOnClickListener(v -> {
            // Share image
            loadView(cardView);
            if (image != null) {
                galleryAddPic(image.getAbsolutePath());
            }
            assert image != null;
            shareImageReport(android.net.Uri.parse(image.getAbsolutePath()));
            //       shareImage(Uri.parse(image.getAbsolutePath()));
        });
    /*    mbtnDownloadIDCard.setOnClickListener(v -> {
            loadView(cardView);
        });*/
        mbtnChangeDetails.setOnClickListener(v -> {
            editor.clear();
            Intent changedetails = new Intent(SangeetaReportActivity.this, RegisterForm.class);
            SangeetaReportActivity.this.startActivity(changedetails);
            SangeetaReportActivity.this.finish();
        });
       /* if (PatientDetailsAbstractClass.Gallery) {
            if(GalleryPhoto!=null)
                Glide.with(getApplicationContext()).load(GalleryPhoto);
          //  mIVPhoto.setImageURI(GalleryPhoto);
        } else {
            if(Photo!=null)
            //mIVPhoto.setImageBitmap(Photo);
            Glide.with(getApplicationContext()).load(Photo);
        }*/
        if (isOnline()) {
            FirebaseDatabase.getInstance().getReference().child("image").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // getting a DataSnapshot for the location at the specified
                    // relative path and getting in the link variable
                    //      String link = dataSnapshot.getValue(String.class);
                    // loading that data into rImage
                    // variable which is ImageView
                    Glide.with(getApplicationContext()).load(sphotopath).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progessDialog.dismiss();
                            assert e != null;
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SangeetaReportActivity.this, FullscreenActivity.class);
                            SangeetaReportActivity.this.startActivity(i);
                            SangeetaReportActivity.this.finish();

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progessDialog.dismiss();

                            return false;
                        }
                    }).into(mIVPhoto);

                    //    Picasso.with(SangeetaReportActivity.this).load(sphotopath).into(mIVPhoto);

                }

                // this will called when any problem
                // occurs in getting data
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // we are showing that error message in toast
                    Toast.makeText(SangeetaReportActivity.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            final Dialog dialog = new Dialog(this);
            dialog.setTitle("Internet Unavailable ..Would you like to make it on ?");
            dialog.cornerRadius(10);
            dialog.positiveAction("OK");
            dialog.positiveActionClickListener(v -> SangeetaReportActivity.this.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
            dialog.negativeAction("CANCEL");
            dialog.negativeActionClickListener(v -> dialog.dismiss());
            dialog.show();
        }

        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mCloseBtn.setEnabled(true);

        mCloseBtn.setOnClickListener(v -> {
            mCloseBtn.setEnabled(false);
            Intent i = new Intent(SangeetaReportActivity.this, FullscreenActivity.class);
            SangeetaReportActivity.this.startActivity(i);
            SangeetaReportActivity.this.finish();


        });

    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        int height = v.getMeasuredWidth() + 300;
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }

    String phtopath = null;
    File image;

    public void loadView(CardView cardView) {
        try {
            cardView.setDrawingCacheEnabled(true);
            Bitmap bitmap = loadBitmapFromView(cardView);
            cardView.setDrawingCacheEnabled(false);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                phtopath = bundle.getString("photopath");
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(System.currentTimeMillis());
            File storageDir = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
            if (!storageDir.exists())
                storageDir.mkdirs();
            image = File.createTempFile(
                    timeStamp,                   /* prefix */
                    ".jpeg",                     /* suffix */
                    storageDir                   /* directory */
            );
            //imageFile = new File(phtopath);
            FileOutputStream outputStream = new
                    FileOutputStream(image);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void shareImageReport(Uri imagePath) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/*");
//set your message
        shareIntent.putExtra(Intent.EXTRA_TEXT, imagePath);

        //    String imgpath = Environment.getExternalStorageDirectory() + File.separator + "shikshankranti.jpg";

        File imageFileToShare = new File(imagePath.getPath());

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
        qrgEncoder = new QRGEncoder("Member ID " + MemberID + "\n" + " Name " + sname + "\n" + " Mobile No " + smobilenumber + "\n" + " DOB " + sdob + "\n" + " Address " + saddress + "\n" + " Taluka " + staluka + "\n" + " District " + sdistrict + "\n" + " Pin Code " + spincode, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            mIVQrCode.setImageBitmap(bitmap);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            writeNewUser(smobilenumber, MemberID, sname, smobilenumber, saddress, sdob, sdistrict, staluka, spincode, sphotopath);

        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        //unbindService(usbConnection);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    public void writeNewUser(String userId, String memberid, String name, String number, String address, String dob, String dist, String tal, String pincode, String photopath) {
     //   UsersDetails user = new UsersDetails(userId, memberid, name, number, Address, dob, dist, tal, pincode, photopath);
        UserDetails userDetails = new UserDetails(userId,memberid,name,number,address,dob,dist,tal,pincode,photopath);
        mDatabase.child("users").child(userId).setValue(userDetails);

    }

    public String getDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.ANDROID_ID;
            }
        }

        return deviceId;
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

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
