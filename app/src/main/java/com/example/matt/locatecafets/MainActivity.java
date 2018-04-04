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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.search_button);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location myLocation = aura.getLocation(); //to change, just for test purposes
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
                for (Cafeteria cafet : cafetList) {
                    Log.d("dist", String.valueOf(cafet.getDistanceToMe()));
                }

                TextView textResult[] = new TextView[5];
                textResult[0] = (TextView)findViewById(R.id.text_result_1);
                textResult[1] = (TextView)findViewById(R.id.text_result_2);
                textResult[2] = (TextView)findViewById(R.id.text_result_3);
                textResult[3] = (TextView)findViewById(R.id.text_result_4);
                textResult[4] = (TextView)findViewById(R.id.text_result_5);
                for (int i = 0; i<5; i++){
                    String text =  String.valueOf(i+1) + ". " + cafetList[i].getName() + " is at " + String.valueOf(cafetList[i].getDistanceToMe()) + "m";
                    textResult[i].setText(text);
                }

                /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Location location;
                try {
                    //location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    //Toast.makeText(this, myCoordinates.toString(), Toast.LENGTH_SHORT).show();
                } catch (SecurityException e) {
                    //Toast.makeText(this, "Erreur getLastKnowLocation GPS .", Toast.LENGTH_SHORT).show();
                };*/
            }
        });
    }

    //Initialising data
    private Cafeteria aura = new Cafeteria("Aura",
                                            62.6036007,29.7420141,
                                            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria carelia = new Cafeteria("Carelia",
                                            62.6048478,29.7422755,
                                            "Yliopistokatu 4, 80100 Joensuu");
    private Cafeteria futura = new Cafeteria("Futura, Natura and Metria",
                                            62.6036007,29.74201292,
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
                                            62.6036007,21.7420141,
                                            "Yliopistokatu 2 , 80100 Joensuu");
    private Cafeteria verola = new Cafeteria("Verola",
                                            62.6036007,22.7420141,
                                            "Yliopistokatu 2 , 80100 Joensuu");

    private Cafeteria[] cafetList = {aura, carelia, futura, pipetti, kuutti, pihlaja, metla, verola};


    /*mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });*/
}
