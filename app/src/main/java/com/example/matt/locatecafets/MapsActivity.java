package com.example.matt.locatecafets;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        //creates the buttons for zoom and compass
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMinZoomPreference(13);
        MyInfoWindow myInfoWindow = new MyInfoWindow(this);
        mMap.setInfoWindowAdapter(myInfoWindow);

        if (getIntent().getExtras() != null) {
            // if the users wants to see only one the cafeterias
            selectedCafet = getIntent().getExtras().getParcelable("cafet");
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(selectedCafet.getCoordinates())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(selectedCafet.getName());
            InfoWindowData info = new InfoWindowData();
            info.setId(selectedCafet.getId());
            info.setAddress(selectedCafet.getAddress());
            Marker m = mMap.addMarker(markerOptions);
            m.setTag(info);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedCafet.getCoordinates()));
        }

        // We display all the cafeterias
        // Getting Current Location
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
            Toast.makeText(this, "Position not found", Toast.LENGTH_SHORT).show();
        }

        // Display markers
        for (int i = 0; i < cafetList.size(); i++) {
            Cafeteria cafet = cafetList.get(i);
            // TODO: animate closest
            if (i == 0 && (selectedCafet == null || selectedCafet.getId() != cafet.getId())) {
                //closest cafet is not in the same color

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(cafet.getCoordinates())
                        .title(cafetList.get(i).getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .snippet("Closest to you");
                InfoWindowData info = new InfoWindowData();
                info.setId(cafet.getId());
                info.setAddress(cafet.getAddress());
                Marker m = mMap.addMarker(markerOptions);
                m.setTag(info);
            } else if (selectedCafet == null || selectedCafet.getId() != cafet.getId()) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(cafetList.get(i).getCoordinates())
                        .title(cafetList.get(i).getName());
                Log.d("cafet", String.valueOf(cafet.getId()));
                InfoWindowData info = new InfoWindowData();
                info.setId(cafet.getId());
                info.setAddress(cafet.getAddress());
                Marker m = mMap.addMarker(markerOptions);
                m.setTag(info);
            }

        }

        mMap.setOnMarkerClickListener(markerClickListener);
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

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            if (selectedCafet == null) {
                LatLng myCoordinates = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.isInfoWindowShown()){
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
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

    public void getHybridView(MenuItem item){
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void getSatelliteView(MenuItem item){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void getListView(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this, ListActivity.class);
        startActivity(intent);
    }
}
