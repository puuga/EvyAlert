package com.appspace.evyalerts.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalerts.BuildConfig;
import com.appspace.evyalerts.R;
import com.appspace.evyalerts.fragment.PhotoSelectBottomSheetDialogFragment;
import com.appspace.evyalerts.fragment.PostEventActivityFragment;
import com.appspace.evyalerts.manager.ApiManager;
import com.appspace.evyalerts.model.Event;
import com.appspace.evyalerts.util.FileUtil;
import com.appspace.evyalerts.util.FirebaseStorageUtil;
import com.appspace.evyalerts.util.GeocoderUtil;
import com.appspace.evyalerts.util.Helper;
import com.appspace.evyalerts.util.ImageUtil;
import com.appspace.evyalerts.util.TimeUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostEventActivity extends AppCompatActivity implements
        PhotoSelectBottomSheetDialogFragment.OnBottomSheetItemClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PostEventActivity";

    private final int PICK_IMAGE_REQUEST = 1;
    private final int REQUEST_TAKE_PHOTO = 2;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    public static final String EDIT_MODE = "EDIT_MODE";

    public boolean isEditEvent = false;
    public boolean canPostEvent = false;
    public Event event;
    String mCurrentPhotoPath;
    Uri mCurrentUri;

    private GoogleApiClient mGoogleApiClient;

    CoordinatorLayout container;
    Toolbar toolbar;
    public BottomSheetBehavior mBottomSheetBehavior;
    public PhotoSelectBottomSheetDialogFragment bottomSheetDialogFragment;

    MaterialDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseStorage mStorage;
    private StorageReference mImageStorageRef;

    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        getExtra();
        initInstances();
        initGoogleApiClient();
        initFirebase();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
        } else {
            latitude = intent.getDoubleExtra(Helper.LATITUDE_KEY, 0);
            longitude = intent.getDoubleExtra(Helper.LONGITUDE_KEY, 0);
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

    private void initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void initFirebase() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mStorage = FirebaseStorage.getInstance();
        mImageStorageRef = mStorage.getReferenceFromUrl("gs://evyalert.appspot.com").child("images");
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
//                LoggerUtils.log2D("PostEventActivity", "POST_MESSAGE_REQUEST - OK");
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
        LoggerUtils.log2D("PostEventActivity", "updateEvent");
        mProgressDialog.dismiss();
        finishWithResult(null);
    }

    private void postEvent() {
        LoggerUtils.log2D("PostEventActivity", "postEvent");
        PostEventActivityFragment fragment = (PostEventActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);

        if (!canPostEvent) {
            LoggerUtils.log2D("PostEventActivity", "!canPostEvent");
            mProgressDialog.dismiss();
            InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            fragment.focusOnSpnProvince();
            return;
        }

        final String title = fragment.edtEventTitle.getText().toString();

        // check data from ui
        if (fragment.eventTypeIndex == -1) {
            mProgressDialog.dismiss();

            Snackbar.make(container, R.string.must_select_at_last_1_type, Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        final String eventTypeIndex = String.valueOf(fragment.eventTypeIndex);
        final String provinceIndex = String.valueOf(fragment.provinceIndex + 1);

        // read file if select to resize and upload resized file to firebase
        if (mCurrentUri != null) {
            // read file if select to resize
            File imageFile = new File(mCurrentUri.getPath());
            if (!imageFile.canRead()) {
                LoggerUtils.log2D("file", "!canread");

                LoggerUtils.log2D("file", "FileHelper - mCurrentUri");
                String realPathFromURI = FileUtil.getPath(this, mCurrentUri);
                try {
                    imageFile = new File(realPathFromURI);
                    if (!imageFile.canRead()) {
                        LoggerUtils.log2D("file", "!canread");
                        mProgressDialog.dismiss();
                        return;
                    }
                } catch (NullPointerException e) {
                    mProgressDialog.dismiss();
                    FirebaseCrash.logcat(Log.ERROR, TAG, "realPathFromURI");
                    FirebaseCrash.report(e);
                }
            }
            File resizedFile = null;
            try {
                resizedFile = FileUtil.createJpgImageFile("resized");
                mCurrentPhotoPath = "file:" + resizedFile.getAbsolutePath();
            } catch (IOException e) {
                LoggerUtils.log2D("createImageFile", "resizesFile");
                FirebaseCrash.report(e);
                mProgressDialog.dismiss();
                return;
            }
            resizedFile = ImageUtil.resizeDown(imageFile, resizedFile);
            if (resizedFile == null)
                resizedFile = imageFile;
            Uri imageUriToUpload = Uri.fromFile(resizedFile);

            // upload file to firebase
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String filename = TimeUtil.getHashStringFromNow() + ".jpg";
            StorageReference uploadImageRef = mImageStorageRef.child(userId + "/" + filename);
            UploadTask uploadTask = uploadImageRef.putFile(imageUriToUpload);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    long byteTransferred = taskSnapshot.getBytesTransferred();
                    long totalByteCount = taskSnapshot.getTotalByteCount();
                    double progress = (100.0 * byteTransferred) / totalByteCount;
                    LoggerUtils.log2D("upload_firebase", "Upload is " + progress + "% done");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.logcat(Log.ERROR, TAG, "uploadTask event photo");
                            FirebaseCrash.report(e);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String url = FirebaseStorageUtil.getMediaDownloadUrl(downloadUrl);
                            LoggerUtils.log2D("upload_firebase", "Uploaded at: " + url);

                            doPostEvent(title, url, eventTypeIndex, provinceIndex);
                        }
                    });
        } else {
            doPostEvent(title, "", eventTypeIndex, provinceIndex);
        }
    }

    private void doPostEvent(String title, final String eventPhotoUrl, String eventTypeIndex, String provinceIndex) {
        if (latitude == 0 && longitude == 0) {
            return;
        }
        if (title.equals(""))
            title = getString(R.string.default_event_title);
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String userPhotoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        String regionIndex = "0";
        final double lat = latitude;
        final double lng = longitude;

        final String district = GeocoderUtil.getDistrict(this, lat, lng);
        final String province = GeocoderUtil.getProvince(this, lat, lng);

        Call<Event> call = ApiManager.getInstance().getAPIService()
                .postEvent(
                        userUid,
                        userName,
                        userPhotoUrl,
                        title,
                        eventPhotoUrl,
                        eventTypeIndex,
                        provinceIndex,
                        regionIndex,
                        String.valueOf(lat),
                        String.valueOf(lng),
                        GeocoderUtil.getAddress(this, lat, lng),
                        String.valueOf(TimeUtil.getCurrentTime())
                );
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event event = response.body();
                LoggerUtils.log2D("api", "postEvent OK: " + response.message());
                LoggerUtils.log2D("api", "postEvent OK: " + event.createdAt);

                Bundle bundle = new Bundle();
                bundle.putString(Helper.DISTRICT, district);
                bundle.putString(Helper.PROVINCE, province);
                if (eventPhotoUrl.equals(""))
                    bundle.putBoolean(Helper.SUBMIT_WITH_IMAGE, false);
                else
                    bundle.putBoolean(Helper.SUBMIT_WITH_IMAGE, true);
                mFirebaseAnalytics.logEvent(Helper.SUBMIT_EVENT, bundle);

                mProgressDialog.dismiss();
                finishWithResult(event);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "doPostEvent");
                FirebaseCrash.report(t);
                LoggerUtils.log2D("api", "postEvent onFailure: " + t.getMessage());

                mProgressDialog.dismiss();
                Snackbar.make(container, "Error", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
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

//        bottomSheetDialogFragment.dismiss();
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri uri = data.getData();
            LoggerUtils.log2D("file_uri", uri.getPath());
            LoggerUtils.log2D("file_uri", uri.toString());
            setImageToView(uri);
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            int angle = ImageUtil.checkImageOrientation(imageUri);
            if (angle != 0) {
                LoggerUtils.log2D("checkImageOrientation", String.valueOf(angle));
                ImageUtil.rotateImage(imageUri, angle);
            }
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
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
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
        LoggerUtils.log2D("bottom_sheet", "onCameraClick");
        bottomSheetDialogFragment.dismiss();

        // check android.permission.WRITE_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(PostEventActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
//            bottomSheetDialogFragment.dismiss();
            dispatchTakePictureIntent();
        } else {
            ActivityCompat.requestPermissions(PostEventActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onGalleryClick() {
        LoggerUtils.log2D("bottom_sheet", "onGalleryClick");
        bottomSheetDialogFragment.dismiss();

        // check android.permission.READ_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(PostEventActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
//            bottomSheetDialogFragment.dismiss();

            Intent intent = new Intent();
            // Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        } else {
            ActivityCompat.requestPermissions(PostEventActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = FileUtil.createJpgImageFile("");
                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
                LoggerUtils.log2D("mCurrentPhotoPath", mCurrentPhotoPath);
            } catch (IOException e) {
                LoggerUtils.log2D("createImageFile", "dispatchTakePictureIntent");
                FirebaseCrash.report(e);
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = null;
                photoURI = FileProvider.getUriForFile(PostEventActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (latitude==0 && longitude==0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
