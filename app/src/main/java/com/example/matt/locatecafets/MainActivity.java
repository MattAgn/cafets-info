package com.example.matt.locatecafets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mapButton = findViewById(R.id.map_button);
        Button searchButton = findViewById(R.id.search_button);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Location location;
                try {
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d("coord", myCoordinates.toString());
                    //Toast.makeText(this, myCoordinates.toString(), Toast.LENGTH_SHORT).show();
                } catch (SecurityException e) {
                    //Toast.makeText(this, "Erreur getLastKnowLocation GPS .", Toast.LENGTH_SHORT).show();
                };
            }
        });
    }

    //Initialising data
    private Cafeteria aura = new Cafeteria("Aura",
                                            new LatLng(62.6036007,29.7420141),
                                            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria carelia = new Cafeteria("Carelia",
                                            new LatLng(62.6048478,29.7422755),
                                            "Yliopistokatu 4, 80100 Joensuu");
    private Cafeteria futura = new Cafeteria("Futura, Natura and Metria",
                                            new LatLng(62.6036007,29.7420141),
                                            "Yliopistokatu 7 , 80100 Joensuu");
    private Cafeteria pipetti = new Cafeteria("Pipetti",
                                            new LatLng(62.6036007,29.7420141),
                                            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria kuutti = new Cafeteria("Kuutti",
                                            new LatLng(62.6036007,29.7420141),
                                            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria pihlaja = new Cafeteria("Pihlaja",
                                            new LatLng(62.6036007,29.7420141),
                                            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria metla = new Cafeteria("Metla",
                                            new LatLng(62.6036007,29.7420141),
                                            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria verola = new Cafeteria("Verola",
                                            new LatLng(62.6036007,29.7420141),
                                            "Yliopistokatu 2 , 80100 Joensuu");

    );






}
