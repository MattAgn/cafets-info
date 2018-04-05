package com.example.matt.locatecafets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


// TODO: 26/03/18  
/* To pass, you must create a mobile app showing a simple listing of the cafeterias
 according to distance from the user. You must create a database of restaurants (at least 5)
  and their locations. For a higher UpGrade consider the following features:
-          show the cafeterias on a map
-          show their address
-          show photo of the facility
-          highlight the nearest one on the map
-          give directions to selected cafeteria
-          have a link to the menu list from the cafeteria webpage */

public class MainActivity extends Activity {

    //Initialising data
    private Cafeteria aura = new Cafeteria("Aura",
            62.6036007,29.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria carelia = new Cafeteria("Carelia",
            62.6048478,29.7422755,
            "Yliopistokatu 4, 80100 Joensuu");
    private Cafeteria futura = new Cafeteria("Futura, Natura and Metria",
            62.6036007,29.73201292,
            "Yliopistokatu 7 , 80100 Joensuu");
    private Cafeteria pipetti = new Cafeteria("Pipetti",
            62.6036007,29.730141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria kuutti = new Cafeteria("Kuutti",
            62.6036007,26.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria pihlaja = new Cafeteria("Pihlaja",
            62.6036007,20.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria metla = new Cafeteria("Metla",
            62.6136007,29.8270141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria verola = new Cafeteria("Verola",
            62.6036007,22.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");

    private Cafeteria[] cafetList = {aura, carelia, futura, pipetti, kuutti, pihlaja, metla, verola};

    private int maxDistance = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.search_button);
        Button mapButton = findViewById(R.id.map_button);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                ArrayList<Cafeteria> cafetArrList = new ArrayList<>(Arrays.asList(cafetList));
                intent.putParcelableArrayListExtra("cafets", cafetArrList);
                startActivity(intent);
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        }
                    }
                } else  {
                    Toast.makeText(MainActivity.this, "Position not found", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void updateOrderList(Location myLocation) {
        for (Cafeteria cafet : cafetList) {
            float dist = cafet.getLocation().distanceTo(myLocation);
            cafet.setDistanceToMe((int)dist);
        }
        Arrays.sort(cafetList, new Comparator<Cafeteria>() {
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

    public void showLocation(View v){
        int indexCafet = ViewGroup.indexOfChild(v.getParent());
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        ArrayList<Cafeteria> cafetArrList = new ArrayList<>(Arrays.asList(cafetList));
        intent.putParcelableArrayListExtra("cafets", cafetArrList);
        startActivity(intent);
    }
}
