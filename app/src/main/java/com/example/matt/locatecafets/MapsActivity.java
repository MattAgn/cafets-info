package com.example.matt.locatecafets;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        Cafeteria cafet = getIntent().getExtras().getParcelable("cafet");
        mMap.addMarker(new MarkerOptions().position(cafet.getCoordinates()).title("Marker in " + cafet.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cafet.getCoordinates()));

        if (ContextCompat.checkSelfPermission (this.getApplicationContext (), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location
            mMap.setMyLocationEnabled(true);
        }
        //creates the buttons for zoom and compass
        mMap.getUiSettings().setZoomGesturesEnabled (true) ;
        mMap.getUiSettings ().setCompassEnabled (true) ;

        /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location;
        try {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
        } catch (SecurityException e) {
            Toast.makeText(this, "Erreur getLastKnowLocation GPS .", Toast.LENGTH_SHORT).show();
        }*/
    }
}
