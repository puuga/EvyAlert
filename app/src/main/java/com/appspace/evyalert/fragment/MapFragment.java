package com.appspace.evyalert.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.R;
import com.appspace.evyalert.activity.MainActivity;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.model.Province;
import com.appspace.evyalert.util.EventIconUtil;
import com.appspace.evyalert.util.TimeUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    public static final String TAG = "MapFragment";

    SupportMapFragment mapFragment;
    GoogleMap googleMap;

    Location mLocation;
    public boolean isMapReady = false;
    boolean wasFirstLocationFig = false;
    boolean isAceptableAcculacy = false;

    Event[] events;
    Marker[] markers;
    Circle circle;

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

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LoggerUtils.log2D("onMapClick", latLng.toString());
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
        if (googleMap == null)
            return;
        if (!wasFirstLocationFig) {
            LoggerUtils.log2D("moveCamera", "FirstLocationFig");
            wasFirstLocationFig = true;
            LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    public void moveCameraToMyLocation(LatLng latLng, int zoom) {
        if (googleMap == null)
            return;
        LoggerUtils.log2D("moveCamera", "FirstLocationFig");
        wasFirstLocationFig = true;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.animateCamera(cameraUpdate);
    }

    public void moveCameraToLatLng(LatLng latLng) {
        if (googleMap == null)
            return;
        LoggerUtils.log2D("moveCamera", "moveCameraToLatLng");
        wasFirstLocationFig = true;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        googleMap.animateCamera(cameraUpdate);
    }

    public void moveCameraToProvince(Province province) {
        if (googleMap == null)
            return;
        LoggerUtils.log2D("moveCamera", "moveCameraToProvince");
        wasFirstLocationFig = true;
        LatLngBounds provinceBound = new LatLngBounds(
                new LatLng(province.boundLatMin, province.boundLngMin),
                new LatLng(province.boundLatMax, province.boundLngMax));
        LoggerUtils.log2D("moveCamera", provinceBound.toString());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(provinceBound, 100);
        googleMap.animateCamera(cameraUpdate);
    }

    public void focusOnMarker(int index) {
        if (googleMap == null) {
            return;
        }
        if (index > markers.length - 1) {
            return;
        }

        markers[index].showInfoWindow();
        moveCameraToLatLng(markers[index].getPosition());
    }

    public void drawCircle(LatLng latLng, int radius) {
        if (googleMap == null) {
            return;
        }

        circle = googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeWidth(10)
                .strokeColor(Color.RED));
    }

    public void createMarker(Event[] events) {
        if (googleMap == null) {
            return;
        }
        this.events = events;

        googleMap.clear();

        markers = new Marker[events.length];

        int markerCount = 0;

        for (int i = 0; i < events.length; i++) {
            Event event = events[i];

            LatLng latLng = new LatLng(event.lat, event.lng);


            int eventTypeIndex = Integer.parseInt(event.eventTypeIndex);

            Marker marker = null;
            try {
                marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("@" + event.userName)
                                .snippet(event.title + "\n" + TimeUtil.timpStampFormater(event.createdAtLong))
                                .icon(BitmapDescriptorFactory
                                        .fromResource(EventIconUtil.eventColorIcons[eventTypeIndex]))
                );
            } catch (NullPointerException e) {
                FirebaseCrash.report(e);

                marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("@" + event.userName)
                                .snippet(event.title + "\n" + TimeUtil.timpStampFormater(event.createdAtLong))
                );
            }

            marker.setTag(i);
            markers[i] = marker;
            markerCount++;
        }

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getActivity(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        LoggerUtils.log2D("marker", "count: " + events.length + ", " + markerCount);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Event event = events[(int) marker.getTag()];
        LoggerUtils.log2D("onMarkerClick", "event id: " + event.eventUid);
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Event event = events[(int) marker.getTag()];
        LoggerUtils.log2D("onInfoWindowClick", "event id: " + event.eventUid);
        ((MainActivity) getActivity()).showEventCommentActivity(event);
    }
}
