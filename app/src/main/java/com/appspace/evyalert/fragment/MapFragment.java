package com.appspace.evyalert.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener {

    public static final String TAG = "MapFragment";

    SupportMapFragment mapFragment;
    GoogleMap googleMap;

    Location mLocation;
    public boolean isMapReady = false;
    boolean wasFirstLocationFig = false;
    boolean isAceptableAcculacy = false;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        isMapReady = true;

        initGoogleMap();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    private void initGoogleMap() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    public void onMyLocationChange(Location location) {
        // moveCameraToMyLocation(location);
        mLocation = location;

        if (location.hasAccuracy()
                && location.getAccuracy() < 50 && location.getAccuracy() != 0.0) {
            isAceptableAcculacy = true;
        }

        if (isAceptableAcculacy && !wasFirstLocationFig) {
            moveCameraToMyLocation();
        }
    }

    public void moveCameraToMyLocation() {
        if (!wasFirstLocationFig) {
            LoggerUtils.log2D("moveCamera", "FirstLocationFig");
            wasFirstLocationFig = true;
            LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(cameraUpdate);
        }

    }
}
