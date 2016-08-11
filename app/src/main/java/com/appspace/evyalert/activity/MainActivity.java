package com.appspace.evyalert.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.BuildConfig;
import com.appspace.evyalert.R;
import com.appspace.evyalert.fragment.EventListFragment;
import com.appspace.evyalert.fragment.MapFragment;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.util.ChromeCustomTabUtil;
import com.appspace.evyalert.util.GeocoderUtil;
import com.appspace.evyalert.util.Helper;
import com.appspace.evyalert.util.TimeUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private DatabaseReference mDatabaseRoot;
    private DatabaseReference mEventsRef;
    private DatabaseReference mUserEventsRef;
    private FirebaseStorage mStorage;
    private StorageReference mImageStorageRef;

    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;

    // Remote Config keys
    private static final String ABOUT_URL_CONFIG_KEY = "about_url";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    Button btnProfile;
    ImageView ivProfile;
    TextView tvUsername;
    Button btnAbout;

    FloatingActionButton fabAddEvent;

    MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initFirebase();
        initInstances();
        initGoogleApiClient();
        initTab();

        loadProfileData();

        createLocationRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();

        mEventsRef.addChildEventListener(childEventListener);
//        mDatabaseRoot.addChildEventListener(childEventListener);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        mEventsRef.removeEventListener(childEventListener);
//        mDatabaseRoot.removeEventListener(childEventListener);

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopLocationUpdates();

        super.onPause();
    }

    private void initDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                toolbar,
                R.string.open_drawer_menu,
                R.string.close_drawer_menu
        );

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFirebase() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        if (BuildConfig.DEBUG) {
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            mFirebaseRemoteConfig.setConfigSettings(configSettings);
        }
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        fetchConfig();

        mDatabaseRoot = FirebaseDatabase.getInstance().getReference();
        mEventsRef = mDatabaseRoot.child("events");
        mUserEventsRef = mDatabaseRoot.child("user-events");

        mStorage = FirebaseStorage.getInstance();
        mImageStorageRef = mStorage.getReferenceFromUrl("gs://evyalert.appspot.com").child("images");
    }

    private void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
//                                    Toast.LENGTH_SHORT).show();
                            LoggerUtils.log2D("RemoteConfig", "Fetch Succeeded");

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
//                            Toast.makeText(MainActivity.this, "Fetch Failed",
//                                    Toast.LENGTH_SHORT).show();
                            LoggerUtils.log2D("RemoteConfig", "Fetch Failed");
                            FirebaseCrash.report(new Exception("Fetch Remote Config Failed"));
                        }
                    }
                });
    }

    private void initInstances() {


        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(this);

        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(this);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progressing)
                .autoDismiss(false)
                .progress(true, 0)
                .build();

        fabAddEvent = (FloatingActionButton) findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(this);
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

    private void initTab() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(mSectionsPagerAdapter.imageResId[0]);
        tabLayout.getTabAt(1).setIcon(mSectionsPagerAdapter.imageResId[1]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.LOGIN_RESUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(fabAddEvent, R.string.login_ok, Snackbar.LENGTH_SHORT)
                        .show();
                loadProfileData();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Snackbar.make(fabAddEvent, R.string.login_cancel, Snackbar.LENGTH_SHORT)
                            .show();
                    MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .title(R.string.need_login)
                            .content(R.string.need_login_description)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    gotoLoginActivity();
                                }
                            })
                            .show();
                } else {
                    loadProfileData();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view == btnProfile) {
            gotoLoginActivity();
        } else if (view == btnAbout) {
            String url = mFirebaseRemoteConfig.getString(ABOUT_URL_CONFIG_KEY);
            ChromeCustomTabUtil.open(this, url);
        } else if (view == fabAddEvent) {
            Snackbar.make(fabAddEvent, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .show();
            addEvent();
        }
    }

    private void addEvent() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String userPhotoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        String title = "test test";
        String eventPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/evyalert.appspot.com/o/images%2F13320908_960637794056268_7477080000136888361_o.jpg?alt=media&token=0d7f3b97-ec62-46e9-917a-010111ea1ff3";
        String eventTypeIndex = "0";
        String provinceIndex = "0";
        String regionIndex = "0";
        double lat = mCurrentLocation == null ? 16.7 : mCurrentLocation.getLatitude();
        double lng = mCurrentLocation == null ? 100.7 : mCurrentLocation.getLongitude();
//        String address = "Thanon Srisaman, Tambon Ban Mai, Amphoe Pak Kret, Chang Wat Nonthaburi 11120";
        String address = GeocoderUtil.getAddress(this, lat, lng);
        String createdAt = TimeUtil.getCurrentTimeStamp();
        long createdAtLong = new Date().getTime();
        writeNewEvent(
                userUid,
                userName,
                userPhotoUrl,
                title,
                eventPhotoUrl,
                eventTypeIndex,
                provinceIndex,
                regionIndex,
                lat,
                lng,
                address,
                createdAt,
                createdAtLong
        );
        GeocoderUtil.getDistrict(this, lat, lng);
        GeocoderUtil.getProvince(this, lat, lng);

        Bundle bundle = new Bundle();
        bundle.putString(Helper.DISTRICT, GeocoderUtil.getDistrict(this, lat, lng));
        bundle.putString(Helper.PROVINCE, GeocoderUtil.getProvince(this, lat, lng));
        mFirebaseAnalytics.logEvent(Helper.SUBMIT_EVENT, bundle);
    }

    private void writeNewEvent(String userUid, String userName, String userPhotoUrl, String title, String eventPhotoUrl, String eventTypeIndex, String provinceIndex, String regionIndex, double lat, double lng, String address, String createdAt, long createdAtLong) {
        String key = mEventsRef.push().getKey();
        Event event = new Event();
        event.userUid = userUid;
        event.userName = userName;
        event.userPhotoUrl = userPhotoUrl;
        event.title = title;
        event.eventPhotoUrl = eventPhotoUrl;
        event.eventTypeIndex = eventTypeIndex;
        event.provinceIndex = provinceIndex;
        event.regionIndex = regionIndex;
        event.lat = lat;
        event.lng = lng;
        event.address = address;
        event.createdAt = createdAt;
        event.createdAtLong = createdAtLong;
        Map<String, Object> eventValues = event.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/events/" + key, eventValues);
//        childUpdates.put("/user-events/" + userUid + "/" + key, eventValues);

        mDatabaseRoot.updateChildren(childUpdates);

        mUserEventsRef.child(userUid).push().setValue(key);
    }

    protected void gotoLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, Helper.LOGIN_RESUEST_CODE);
    }

    private void loadProfileData() {
        setProfile();
    }

    private void setProfile() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            LoggerUtils.log2D("profile", "cannot get firebaseUser");
            gotoLoginActivity();
            return;
        }
        Glide.with(this)
                .load(firebaseUser.getPhotoUrl())
                .bitmapTransform(new CropCircleTransformation(Contextor.getInstance().getContext()))
                .into(ivProfile);

        tvUsername.setText(firebaseUser.getDisplayName());
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.dismiss();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this
        );
        LoggerUtils.log2D("startLocationUpdates", "started");
    }

    void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LoggerUtils.log2D("GoogleApiClient", "onConnectionFailed");
        FirebaseCrash.report(new Exception(connectionResult.getErrorMessage()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LoggerUtils.log2D("GoogleApiClient", "onConnected");

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        LoggerUtils.log2D("GoogleApiClient", "onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
//        LoggerUtils.log2I("onLocationChanged", location.toString());
        mCurrentLocation = location;


        MapFragment fragment = (MapFragment) mSectionsPagerAdapter.getItem(0);
        if (fragment.isMapReady) {
            fragment.onMyLocationChange(location);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int[] imageResId = {
                R.drawable.ic_map_white_24dp,
                R.drawable.ic_view_list_white_24dp
        };

        String[] tabTitles = {"MAP", "LIST"};

        MapFragment mapFragment;
        EventListFragment eventListFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    if (mapFragment == null) {
                        mapFragment = MapFragment.newInstance();
                    }
                    return mapFragment;
                case 1:
                    if (eventListFragment == null) {
                        eventListFragment = EventListFragment.newInstance();
                    }
                    return eventListFragment;
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
//            Drawable image = getResources().getDrawable(imageResId[position], null);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString("");
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
        }
    }

    ChildEventListener childEventListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LoggerUtils.log2D("ChildEventListener", "onChildAdded: " + dataSnapshot.getKey());
            Event event = dataSnapshot.getValue(Event.class);
            LoggerUtils.log2D("ChildEventListener", event.userName);
            LoggerUtils.log2D("ChildEventListener", event.createdAt);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            LoggerUtils.log2D("ChildEventListener", "onChildChanged: " + dataSnapshot.getKey());
            Event event = dataSnapshot.getValue(Event.class);
            LoggerUtils.log2D("ChildEventListener", event.userName);
            LoggerUtils.log2D("ChildEventListener", event.createdAt);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LoggerUtils.log2D("ChildEventListener", "onChildChanged: " + dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            LoggerUtils.log2D("ChildEventListener", "onChildChanged: " + dataSnapshot.getKey());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            LoggerUtils.log2D("ChildEventListener", "onCancelled: " + databaseError.getMessage());
        }
    };
}
