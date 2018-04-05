package com.example.matt.locatecafets;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location
            mMap.setMyLocationEnabled(true);
        }
        //creates the buttons for zoom and compass
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        if (getIntent().getParcelableArrayListExtra("cafets") != null) {
            // Getting Current Location
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (myLocation != null) {
                LatLng myCoordinates = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }

            ArrayList<Cafeteria> cafetArrList = getIntent().getParcelableArrayListExtra("cafets");
            updateOrderList(myLocation, cafetArrList);
            for (int i = 0; i < cafetArrList.size(); i++) {
                if (i == 0) {
                    //closest cafet is not in the same color
                    mMap.addMarker(new MarkerOptions()
                            .position(cafetArrList.get(i).getCoordinates())
                            .title("Marker in " + cafetArrList.get(i).getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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



}
