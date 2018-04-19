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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ListActivity extends Activity {

    private int maxDistance = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location
            if (getIntent().getParcelableArrayListExtra("cafets") != null) {
                // Getting Current Location
                LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);
                Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                ArrayList<Cafeteria> cafetArrList = getIntent().getParcelableArrayListExtra("cafets");
                if (myLocation != null) {
                    LatLng myCoordinates = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    updateOrderList(myLocation, cafetArrList);
                    ViewGroup resultContainer = findViewById(R.id.result_container);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    int childCount = resultContainer.getChildCount();
                    if (childCount != 0) {
                        for (int i = 0; i < childCount; i++) {
                            resultContainer.removeViewAt(i);
                        }
                    }
                    for (int i = 0; i < cafetList.length; i++) {
                        if (cafetList[i].getDistanceToMe() < maxDistance) {
                            String text = String.valueOf(i + 1) + ". " + cafetList[i].getName() + " is at " + String.valueOf(cafetList[i].getDistanceToMe()) + "m";
                            View result = inflater.inflate(R.layout.result_item, null);
                            TextView resultText = (TextView) ((ViewGroup) result).getChildAt(0);
                            resultText.setText(text);
                            resultContainer.addView(result);
                            final int indexCafet = i;
                            ((ViewGroup) result).getChildAt(2).setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Cafeteria cafet = cafetList[indexCafet];
                                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                            intent.putExtra("cafet", cafet);
                                            startActivity(intent);
                                        }
                                    }
                            );
                        }
                    }
                } else {
                    Toast.makeText(this, "Position not found", Toast.LENGTH_SHORT).show();
                }
            }


        };

    /*

            if (ActivityCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
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
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 50, locationListener);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (myLocation != null) {
                updateOrderList(myLocation);
                ViewGroup resultContainer = findViewById(R.id.result_container);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                int childCount = resultContainer.getChildCount();
                if ( childCount != 0) {
                    for (int i = 0; i < childCount; i++) {
                        resultContainer.removeViewAt(i);
                    }
                }
                for (int i=0; i< cafetList.length; i++){
                    if (cafetList[i].getDistanceToMe() < maxDistance) {
                        String text =  String.valueOf(i+1) + ". " + cafetList[i].getName() + " is at " + String.valueOf(cafetList[i].getDistanceToMe()) + "m";
                        View result = inflater.inflate(R.layout.result_item, null);
                        TextView resultText = (TextView) ((ViewGroup)result).getChildAt(0);
                        resultText.setText(text);
                        resultContainer.addView(result);
                        final int indexCafet = i;
                        ((ViewGroup)result).getChildAt(2).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Cafeteria cafet = cafetList[indexCafet];
                                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                        intent.putExtra("cafet", cafet);
                                        startActivity(intent);
                                    }
                                }
                        );
                    }
                }
            } else  {
                Toast.makeText(MainActivity.this, "Position not found", Toast.LENGTH_SHORT).show();
            }

        }
    });
}*/

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
            updateOrderList(loc);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };
}
