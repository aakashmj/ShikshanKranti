package com.shikshankranti.sanghatna;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.shikshankranti.sanghatna.PatientDetailsAbstractClass.Name;


public class SangeetaCaptureActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PICTURE = 2;
    private final List<Integer> blockedKeys = new ArrayList<>(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    private final Handler mHandler = new Handler();
    private final Runnable decor_view_settings = () -> getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    ImageView mcapturePic, mivPhoto;
    MaterialButton mbtnNext;
    UploadTask uploadTask;
    StorageReference imagesRef;
    StorageReference riversRef;
    StorageReference storageRef;
    FirebaseStorage storage;
    String currentPhotoPath;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE, View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.capture_layout);
        final Locale loc = new Locale("hin", "IND");
        ImageButton mCloseBtn = findViewById(R.id.closeBtn);
        mcapturePic = findViewById(R.id.btnCapture);
        mbtnNext = findViewById(R.id.btnNext);
        mbtnNext.setEnabled(true);
        ImageView mGallery = findViewById(R.id.ivGallery);
        LinearLayout mLinearBrose = findViewById(R.id.llbrowse);
        mivPhoto = findViewById(R.id.ivPhoto);
        // Get a non-default Storage bucket
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();
        // Create a child reference
// imagesRef now points to "images"
        imagesRef = storageRef.child("images");
        mcapturePic.setOnClickListener(view -> dispatchTakePictureIntent());
        mGallery.setOnClickListener(view -> fetchImageFromGallery());
        mbtnNext.setOnClickListener(v -> {
            Intent reportintent = new Intent(SangeetaCaptureActivity.this, SangeetaReportActivity.class);
            reportintent.putExtra("photopath", currentPhotoPath);
            startActivity(reportintent);
            finish();
        });
        mCloseBtn.setOnClickListener(view -> {
            Intent i = new Intent(SangeetaCaptureActivity.this, FullscreenActivity.class);
            startActivity(i);
            finish();


        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
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
            sendBroadcast(closeDialog);
        }
    }

    Uri selectedImageURI;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
            galleryAddPic();
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageBtn.setImageBitmap(imageBitmap);*/
            PatientDetailsAbstractClass.Gallery = false;
        }
        if (requestCode == SELECT_PICTURE) {
            if (data != null) {
                selectedImageURI = data.getData();
            }
            /*SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key),MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("photouri", String.valueOf(selectedImageURI));
            editor.apply();*/
            PatientDetailsAbstractClass.Gallery = true;
            PatientDetailsAbstractClass.GalleryPhoto = selectedImageURI;
            riversRef = imagesRef.child(selectedImageURI.getLastPathSegment());
            uploadTask = riversRef.putFile(selectedImageURI);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Toast.makeText(SangeetaCaptureActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }).addOnSuccessListener(taskSnapshot -> {

                riversRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.e("Tuts+", "uri: " + uri.toString());
                    PatientDetailsAbstractClass.PhotoPath = uri.toString();
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key),MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("fbphotopath", uri.toString());
                    editor.apply();

                    mbtnNext.setEnabled(true);
                    //Handle whatever you're going to do with the URL here
                });
//                Toast.makeText(getApplicationContext(),taskSnapshot.getMetadata().toString(),Toast.LENGTH_LONG).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

            }).addOnProgressListener(snapshot -> {
                double progress
                        = (100.0
                        * snapshot.getBytesTransferred()
                        / snapshot.getTotalByteCount());
                mbtnNext.setEnabled(false);
               Toast.makeText(getApplicationContext(),"Please Wait..",Toast.LENGTH_SHORT).show();
            });
            mivPhoto.setImageURI(selectedImageURI);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.shikshankranti.sanghatna.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mivPhoto.getWidth();
        int targetH = mivPhoto.getHeight();
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap rotatedbmp = rotateBitmapOrientation(currentPhotoPath);
        if (rotatedbmp != null) {
            PatientDetailsAbstractClass.Photo = rotatedbmp;
        }

// Points to "images/space.jpg"
// Note that you can use variables to create child values
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PatientDetailsAbstractClass.Photo.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();
     //   CameraURI = getImageUri(this,rotatedbmp);
        riversRef = imagesRef.child(imagesRef.getPath());
        uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            riversRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Log.e("Tuts+", "uri: " + uri.toString());
                PatientDetailsAbstractClass.PhotoPath = uri.toString();
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("fbphotopath", uri.toString());
                editor.apply();
                mbtnNext.setEnabled(true);
                //Handle whatever you're going to do with the URL here
            });
            //  Toast.makeText(getApplicationContext(),taskSnapshot.getMetadata().toString(),Toast.LENGTH_LONG).show();

        }).addOnProgressListener(snapshot -> {
            double progress
                    = (100.0
                    * snapshot.getBytesTransferred()
                    / snapshot.getTotalByteCount());
            mbtnNext.setEnabled(false);
            Toast.makeText(getApplicationContext(),"Please Wait..",Toast.LENGTH_SHORT).show();
        });
       /* SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(String.valueOf(R.string.preference_file_key),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("camerauri", String.valueOf(selectedImageURI));
        editor.apply();*/

        //  Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        mivPhoto.setImageBitmap(rotatedbmp);
        PatientDetailsAbstractClass.Photo = rotatedbmp;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert exif != null;
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        // Return result
        return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
    }

    void fetchImageFromGallery() {

        Intent intent = new Intent();
        // Create the File where the photo should go
        File photoFile;
        try {
            photoFile = createImageFile();

            // Continue only if the File was successfully created
            if (photoFile != null) {
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            }
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
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

    /*private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/
}