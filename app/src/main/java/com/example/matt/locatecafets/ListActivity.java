package com.example.matt.locatecafets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends Activity {

    private List<Integer> distanceValues = Database.getDistanceValues();
    private int maxDistance = -1; //means default value is infinity
    private List<Cafeteria> cafetList = Database.getCafeterias();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ViewGroup resultContainer = findViewById(R.id.result_container);
        Spinner displaySpinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.display_range, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        displaySpinner.setAdapter(adapter);
        displaySpinner.setOnItemSelectedListener(spinnerListener);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location

            // Getting Current Location
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (myLocation != null) {
                LatLng myCoordinates = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                updateOrderList(myLocation);
                updateInterface();
            } else {
                Toast.makeText(this, "Position not found", Toast.LENGTH_SHORT).show();
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

    public void updateInterface() {
        ViewGroup resultContainer = findViewById(R.id.result_container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int childCount = resultContainer.getChildCount();
        // Cleaning the interface
        Log.d("childCount", String.valueOf(childCount));
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                Log.d("child nb", String.valueOf(i));
                resultContainer.removeViewAt(i);
                Log.d("child removed", String.valueOf(i));
            }
        }
        // Showing the results
        for (int i = 0; i < cafetList.size(); i++) {
            int distance = cafetList.get(i).getDistanceToMe();
            if ( distance < maxDistance || maxDistance == -1) {
                String text = String.valueOf(i + 1) + ". " + cafetList.get(i).getName() + " is at ";
                if (distance < 1000) {
                    text += String.valueOf(distance) + "m";
                } else {
                    text += String.valueOf(Math.floor(distance / 100)/10 ) + "km";
                }
                View result = inflater.inflate(R.layout.result_item, null);
                TextView resultText = (TextView) ((ViewGroup) result).getChildAt(0);
                resultText.setText(text);
                resultContainer.addView(result);
                final int indexCafet = i;
                ((ViewGroup) result).getChildAt(2).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Cafeteria cafet = cafetList.get(indexCafet);
                                Intent intent = new Intent(ListActivity.this, MapsActivity.class);
                                intent.putExtra("cafet", cafet);
                                startActivity(intent);
                            }
                        }
                );
            }
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            updateOrderList(loc);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int newMaxDistance = distanceValues.get((int)id);
            if (maxDistance != newMaxDistance ) {
                updateInterface();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };
}
