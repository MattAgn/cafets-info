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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
            62.6036007, 29.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria carelia = new Cafeteria("Carelia",
            62.6048478, 29.7422755,
            "Yliopistokatu 4, 80100 Joensuu");
    private Cafeteria futura = new Cafeteria("Futura, Natura and Metria",
            62.6036007, 29.73201292,
            "Yliopistokatu 7 , 80100 Joensuu");
    private Cafeteria pipetti = new Cafeteria("Pipetti",
            62.6036007, 29.730141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria kuutti = new Cafeteria("Kuutti",
            62.6036007, 26.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria pihlaja = new Cafeteria("Pihlaja",
            62.6036007, 20.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria metla = new Cafeteria("Metla",
            62.6136007, 29.8270141,
            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria verola = new Cafeteria("Verola",
            62.6036007, 22.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");

    private Cafeteria[] cafetList = {aura, carelia, futura, pipetti, kuutti, pihlaja, metla, verola};

    private int maxDistance = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                ArrayList<Cafeteria> cafetArrList = new ArrayList<>(Arrays.asList(cafetList));
                intent.putParcelableArrayListExtra("cafets", cafetArrList);
                startActivity(intent);
            }
        });
    }

}
