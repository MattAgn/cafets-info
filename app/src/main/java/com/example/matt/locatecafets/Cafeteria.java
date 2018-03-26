package com.example.matt.locatecafets;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matt on 26/03/18.
 */

public class Cafeteria {
    private String name;
    private Bitmap image;
    private LatLng coordinates;
    private String website; //todo : change to URI&
    private String address;

    //Constructors
    public Cafeteria(String name, LatLng coordinates, String address){
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
    }

    //Getters
    public LatLng getCoordinates(){
        return this.coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Bitmap getImage() {
        return image;
    }
}


