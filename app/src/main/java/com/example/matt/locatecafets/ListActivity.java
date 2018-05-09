package com.example.matt.locatecafets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends Activity {
    private List<Integer> distanceValues = Database.getDistanceValues();
    private int maxDistance = 30000; //means default value is city size
    private List<Cafeteria> cafetList = Database.getCafeterias();
    private GoogleApiClient googleApiClient;

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

        showSettingsAlert(false);
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
            if ( distance < maxDistance ) {
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
                                showLeavingAppAlert(cafet);
                            }
                        }
                );
            }
            if (resultContainer.getChildCount() == 0) {
                TextView textView = new TextView(this);
                textView.setText(R.string.no_cafet);
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

            // Ask the user to activate GPS if not done
            if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                //showSettingsAlert();
            } else {
                if (myLocation != null) {
                    updateOrderList(myLocation);
                    updateInterface();
                } else {
                    Toast.makeText(this, R.string.waiting_gps, Toast.LENGTH_LONG).show();
                }
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
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                handleLocation();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderDisabled(String provider) {
            showSettingsAlert((provider.equals("gps")));
        }
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


    public void showLeavingAppAlert(Cafeteria cafet){
        final Cafeteria cafeteria = cafet;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(R.string.leaving_app);
        alertDialog.setMessage(R.string.redirection);

        alertDialog.setPositiveButton(R.string.continuee, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(cafeteria.getWebsite()));
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

    // TODO:  to try out
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
                                status.startResolutionForResult(ListActivity.this, 1000);
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
}
