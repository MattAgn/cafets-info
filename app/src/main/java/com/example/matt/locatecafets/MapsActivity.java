package com.example.matt.locatecafets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Cafeteria> cafetList = Database.getCafeterias();
    private Cafeteria selectedCafet;
    private GoogleApiClient googleApiClient;
    private String websiteCafetClicked;
    private Button websiteButton;
    private int buttonSlideValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        websiteButton = findViewById(R.id.website_map_button);
        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (websiteCafetClicked != null) {
                    showLeavingAppAlert(websiteCafetClicked);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO: 06/04/18 Animate closest marker 
        // TODO: 25/04/18 Add button to go to closest marker ? 
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location
            mMap.setMyLocationEnabled(true);
        }

        //Setting the website button slide value
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        buttonSlideValue = width_px/2 - websiteButton.getWidth()/2;

        // Creates the buttons for zoom and compass
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        MyInfoWindow myInfoWindow = new MyInfoWindow(this);
        mMap.setInfoWindowAdapter(myInfoWindow);
        mMap.setOnMarkerClickListener(markerClickListener);
        mMap.setOnMapClickListener(mapClickListener);
        mMap.setOnMapLoadedCallback(mapLoadedCallback);

        Location myLocation = getLocation();
        displayMarkers(myLocation);
    }


    public void displaySelectedCafet() {
        if (getIntent().getExtras() != null) {
            // if the users wants to see only one the cafeterias
            selectedCafet = getIntent().getExtras().getParcelable("cafet");
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(selectedCafet.getCoordinates())
                    .title(selectedCafet.getName());
            InfoWindowData info = new InfoWindowData();
            info.setId(selectedCafet.getId());
            info.setAddress(selectedCafet.getAddress());
            info.setWebsite(selectedCafet.getWebsite());
            Marker m = mMap.addMarker(markerOptions);
            m.setTag(info);
            m.showInfoWindow();
            websiteCafetClicked = selectedCafet.getWebsite();
            int zoom = (int)mMap.getCameraPosition().zoom;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(selectedCafet.getCoordinates().latitude + (double)90/Math.pow(2, zoom),
                            selectedCafet.getCoordinates().longitude), zoom));
            animateWebsiteButton();
        }
    }

    public Location getLocation() {
        if (mMap != null) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location
                mMap.setMyLocationEnabled(true);
            }
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            // We move the camera on user if possible
            if (myLocation != null && selectedCafet == null) {
                LatLng myCoordinates = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
            } else if (selectedCafet == null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cafetList.get(0).getCoordinates()));
            }

            // We sort the list by distance to user
            if (myLocation != null) {
                updateOrderList(myLocation);
            } else {
                Toast.makeText(this, R.string.waiting_gps, Toast.LENGTH_SHORT).show();
            }
            return myLocation;
        }
        return null;
    }

    public void displayMarkers(Location myLocation) {
        if (myLocation != null) {
            updateOrderList(myLocation);
        }
        for (int i = 0; i < cafetList.size(); i++) {
            Cafeteria cafet = cafetList.get(i);
            // TODO: animate closest
            if (i == 0 && myLocation !=null && (selectedCafet == null || selectedCafet.getId() != cafet.getId())) {
                //closest cafet is not in the same color
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(cafet.getCoordinates())
                        .title(cafetList.get(i).getName() + getString(R.string.closest))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .snippet("Closest to you");
                InfoWindowData info = new InfoWindowData();
                info.setId(cafet.getId());
                info.setAddress(cafet.getAddress());
                info.setWebsite(cafet.getWebsite());
                Marker m = mMap.addMarker(markerOptions);
                m.setTag(info);
            } else if (selectedCafet == null || selectedCafet.getId() != cafet.getId()) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(cafetList.get(i).getCoordinates())
                        .title(cafetList.get(i).getName());
                InfoWindowData info = new InfoWindowData();
                info.setId(cafet.getId());
                info.setAddress(cafet.getAddress());
                info.setWebsite(cafet.getWebsite());
                Marker m = mMap.addMarker(markerOptions);
                m.setTag(info);
            }
        }
    }

    public void updateOrderList(Location myLocation) {
        for (Cafeteria cafet : cafetList) {
            float dist = cafet.getLocation().distanceTo(myLocation);
            cafet.setDistanceToMe((int)dist);
        }
        Collections.sort(cafetList, new Comparator<Cafeteria>() {
            @Override
            public int compare(Cafeteria c1, Cafeteria c2) {
                return (c1.getDistanceToMe() - c2.getDistanceToMe());
            }
        });
    }

    // Listeners
    GoogleMap.OnMapLoadedCallback mapLoadedCallback = new GoogleMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            displaySelectedCafet();
        }
    };

    GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (websiteButton.getVisibility() == View.VISIBLE) {
                websiteButton.animate()
                        .translationX(-buttonSlideValue)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                websiteButton.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            mMap.clear();
            if (selectedCafet == null) {
                LatLng myCoordinates = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            } else {
                displaySelectedCafet();
            }
            displayMarkers(loc);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {
            Location myLocation = getLocation();
            mMap.clear();
            if (selectedCafet != null) {
                displaySelectedCafet();
            }
            displayMarkers(myLocation);
        }

        @Override
        public void onProviderDisabled(String provider) {
            showSettingsAlert((provider.equals("gps")));
        }
    };

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.isInfoWindowShown()){
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
                int zoom = (int)mMap.getCameraPosition().zoom;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(marker.getPosition().latitude + (double)90/Math.pow(2, zoom),
                                marker.getPosition().longitude), zoom), 400, null);
                InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                websiteCafetClicked = infoWindowData.getWebsite();
                animateWebsiteButton();
            }
            return true;
        }
    };

    // Menu for different views of the map
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    public void getNormalView(MenuItem item){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void getSatelliteView(MenuItem item){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void getListView(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this, ListActivity.class);
        startActivity(intent);
    }

    public void showLeavingAppAlert(String website){
        final String websiteFinal = website;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(R.string.leaving_app);
        alertDialog.setMessage(R.string.redirection);

        alertDialog.setPositiveButton(R.string.continuee, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(websiteFinal));
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void showSettingsAlert(boolean shouldAppear) {
        if (googleApiClient == null || shouldAppear) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            builder.setAlwaysShow(true); // this is the key ingredient
            PendingResult result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback() {
                @Override
                public void onResult(Result result){
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = ((LocationSettingsResult)result).getLocationSettingsStates();
                    switch (status.getStatusCode())
                    {
                        case LocationSettingsStatusCodes.SUCCESS:
                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MapsActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {}
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
            googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
        }
    }

    public void animateWebsiteButton() {
        // Prepare the View for the animation
        websiteButton.setVisibility(View.VISIBLE);
        websiteButton.setAlpha(0.0f);

        // Start the animation
        websiteButton.animate()
                .translationX(buttonSlideValue)
                .alpha(1.0f)
                .setListener(null);
    }
}
