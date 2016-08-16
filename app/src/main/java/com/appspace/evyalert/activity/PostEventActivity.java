package com.appspace.evyalert.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.BuildConfig;
import com.appspace.evyalert.R;
import com.appspace.evyalert.fragment.PhotoSelectBottomSheetDialogFragment;
import com.appspace.evyalert.fragment.PostEventActivityFragment;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.util.Helper;
import com.appspace.evyalert.util.ImageUtil;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostEventActivity extends AppCompatActivity
        implements PhotoSelectBottomSheetDialogFragment.OnBottomSheetItemClickListener {

    private final int PICK_IMAGE_REQUEST = 1;
    private final int REQUEST_TAKE_PHOTO = 2;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    public static final String EDIT_MODE = "EDIT_MODE";
    public static final int UPDATE_EVENT_REQUEST = 22;

    public Boolean isEditEvent = false;
    public Event event;
    String mCurrentPhotoPath;
    Uri mCurrentUri;

    CoordinatorLayout container;
    Toolbar toolbar;
    public BottomSheetBehavior mBottomSheetBehavior;
    public PhotoSelectBottomSheetDialogFragment bottomSheetDialogFragment;

    MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        getExtra();
        initInstances();
    }

    private void getExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(EDIT_MODE)) {

            isEditEvent = true;
            event = intent.getParcelableExtra(Helper.MODEL_EVENT_KEY);
            LoggerUtils.log2D("PostMessageActivity", "EDIT_MODE isEditNews: " + isEditEvent);
            LoggerUtils.log2D("PostMessageActivity", "EDIT_MODE event: " + event);

            PostEventActivityFragment fragment = (PostEventActivityFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment);
            fragment.checkEditMode();
        }
    }

    private void initInstances() {
        container = (CoordinatorLayout) findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomSheetDialogFragment = new PhotoSelectBottomSheetDialogFragment();
        bottomSheetDialogFragment.setOnBottomSheetItemClickListener(this);

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progressing)
                .autoDismiss(false)
                .progress(true, 0)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post:
//                LoggerUtils.log2D("PostMessageActivity", "POST_MESSAGE_REQUEST - OK");
                mProgressDialog.show();
                if (isEditEvent) {
                    updateEvent();
                } else {
                    postEvent();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateEvent() {
        mProgressDialog.hide();
        finishWithResult(null);
    }

    private void postEvent() {
        mProgressDialog.hide();
        finishWithResult(null);
    }

    void finishWithResult(Event event) {
        if (event != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Helper.MODEL_EVENT_KEY, event);
            setResult(Activity.RESULT_OK, returnIntent);
        } else {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            LoggerUtils.log2D("file_uri", uri.getPath());
            LoggerUtils.log2D("file_uri", uri.toString());
            setImageToView(uri);
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            setImageToView(imageUri);

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(PostEventActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    onCameraClick();
                }
                return;
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    onGalleryClick();
                }
                return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCameraClick() {

    }

    @Override
    public void onGalleryClick() {

    }

    void setImageToView(Uri uri) {
        mCurrentUri = uri;
        PostEventActivityFragment f = (PostEventActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        try {
            Bitmap originalImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Bitmap scaleImage = ImageUtil.scaleTo(originalImage, f.ivEventImage);

            f.ivEventImage.setImageBitmap(scaleImage);

        } catch (IOException e) {
            LoggerUtils.log2D("imagefile", "uri" + uri.getPath());
            FirebaseCrash.report(e);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("en", "US"))
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                LoggerUtils.log2D("createImageFile", "dispatchTakePictureIntent");
                FirebaseCrash.report(e);
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = null;
                try {
                    photoURI = FileProvider.getUriForFile(PostEventActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());
                } catch (IOException e) {
                    LoggerUtils.log2D("createImageFile", "dispatchTakePictureIntent");
                    FirebaseCrash.report(e);
                    return;
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
