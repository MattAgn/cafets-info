package com.example.matt.locatecafets;

import android.content.Context;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Cafeteria[] cafetList = Database.getCafeterias();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO: 06/04/18 Animate closest marker 
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

        if (getIntent().getParcelableArrayListExtra("cafets") != null) {
            // Getting Current Location
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ArrayList<Cafeteria> cafetArrList = getIntent().getParcelableArrayListExtra("cafets");
            if (myLocation != null) {
                LatLng myCoordinates = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
                updateOrderList(myLocation, cafetArrList);
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cafetArrList.get(0).getCoordinates()));
                Toast.makeText(this, "Position not found", Toast.LENGTH_SHORT).show();
            }

            for (int i = 0; i < cafetArrList.size(); i++) {
                if (i == 0) {
                    //closest cafet is not in the same color
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(cafetArrList.get(i).getCoordinates())
                            .title("Marker in " + cafetArrList.get(i).getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet("Closest to you");
                    InfoWindowData info = new InfoWindowData();
                    info.setImage("snowqualmie");
                    Marker m = mMap.addMarker(markerOptions);
                    m.setTag(info);
                } else {
                    mMap.addMarker(new MarkerOptions()
                            .position(cafetArrList.get(i).getCoordinates())
                            .title("Marker in " + cafetArrList.get(i).getName()));
                }
            }
        } else if (getIntent().getExtras().getParcelable("cafet") != null) {
                Cafeteria cafet = getIntent().getExtras().getParcelable("cafet");
                mMap.addMarker(new MarkerOptions()
                        .position(cafet.getCoordinates())
                        .title("Marker in " + cafet.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cafet.getCoordinates()));
        }

        mMap.setOnMarkerClickListener(markerClickListener);
    }

    public void updateOrderList(Location myLocation, ArrayList<Cafeteria> cafetArrList) {
        for (Cafeteria cafet : cafetArrList) {
            float dist = cafet.getLocation().distanceTo(myLocation);
            cafet.setDistanceToMe((int)dist);
        }
        Collections.sort(cafetArrList, new Comparator<Cafeteria>() {
            @Override
            public int compare(Cafeteria c1, Cafeteria c2) {
                return (c1.getDistanceToMe() - c2.getDistanceToMe());
            }
        });
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            LatLng myCoordinates = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
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


    /**
     * Creates the menu of the app defined in menu_app.
     * In our app, the menu is used to choose between the 3 modes of google map : Satellite, Normal and Hybrid
     * @param menu The menu object of the app
     * @return true to show proper execution
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    /**
     * Sets google map to the Normal View
     * Is triggered in the menu by pressing the Normal View Item
     * @param item : the menu item corresponding to the normalView button
     */
    public void getNormalView(MenuItem item){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * Sets google map to the Hybrid View
     * Is triggered in the menu by pressing the Hybrid View Item
     * @param item : the menu item corresponding to the hybridView button
     */
    public void getHybridView(MenuItem item){
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    /**
     * Sets google map to the Satellite View
     * Is triggered in the menu by pressing the Normal Satellite Item
     * @param item the menu item corresponding to the satelliteView button
     */
    public void getSatelliteView(MenuItem item){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
}
