package com.example.matt.locatecafets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

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

        ImageButton refreshButton = findViewById(R.id.refresh_image_button);
        Spinner displaySpinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.display_range, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        displaySpinner.setAdapter(adapter);
        displaySpinner.setOnItemSelectedListener(spinnerListener);

        refreshButton.setOnClickListener(refreshClickListener);

        handleLocation();
    }

    //Helper functions
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
        ViewGroup wrapper = findViewById(R.id.wrapper);
        LinearLayout resultContainer = new LinearLayout(this);
        resultContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        resultContainer.setLayoutParams(params);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int childCount = wrapper.getChildCount();
        // Cleaning the interface
        Log.d("childCount", String.valueOf(childCount));
        if (childCount != 0) {
            wrapper.removeViewAt(0);
        }
        // Showing the results
        for (int i = 0; i < cafetList.size(); i++) {
            final Cafeteria cafet = cafetList.get(i);
            int distance = cafet.getDistanceToMe();
            if ( distance < maxDistance || maxDistance == -1) {
                String text = String.valueOf(i + 1) + ". " + cafet.getName() + " - ";
                if (distance < 1000) {
                    text += String.valueOf(distance) + "m";
                } else {
                    text += String.valueOf(Math.floor(distance / 100)/10 ) + "km";
                }
                View result = inflater.inflate(R.layout.result_item, null);
                TextView resultText = (TextView) ((ViewGroup) result).getChildAt(0);
                resultText.setText(text);
                resultContainer.addView(result);
                ((ViewGroup) result).getChildAt(2).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListActivity.this, MapsActivity.class);
                                intent.putExtra("cafet", cafet);
                                startActivity(intent);
                            }
                        }
                );
                // TODO: include warning message, about to leave app
                ((ViewGroup) result).getChildAt(1).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(cafet.getWebsite()));
                                startActivity(intent);
                            }
                        }
                );
            }
            if (resultContainer.getChildCount() == 0) {
                TextView textView = new TextView(this);
                textView.setText("No cafeteria found in this range");
                resultContainer.addView(textView);
            }
        }
        wrapper.addView(resultContainer);
    }

    // Main function
    public void handleLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location

            // Getting Current Location
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 50, locationListener);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation != null) {
                updateOrderList(myLocation);
                updateInterface();
            } else {
                Toast.makeText(this, "Please activate your GPS", Toast.LENGTH_LONG).show();
            }

            // Ask the user to activate GPS
            if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                Intent intent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS) ;
                startActivityForResult(intent, 1);
            }

        }

    }

    //Listeners
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            handleLocation();
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (provider == LocationManager.GPS_PROVIDER) {
                handleLocation();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderDisabled(String provider) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) { //Checks the permission to use Location

                Toast.makeText(getApplicationContext(), "Please activate your GPS", Toast.LENGTH_LONG).show();

                // Ask the user to activate GPS
                LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    Intent intent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS) ;
                    startActivityForResult(intent, 1);
                }

            }        }
    };

    View.OnClickListener refreshClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleLocation();
        }
    };

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int newMaxDistance = distanceValues.get((int)id);
            if (maxDistance != newMaxDistance ) {
                maxDistance = newMaxDistance;
                handleLocation();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };
}
