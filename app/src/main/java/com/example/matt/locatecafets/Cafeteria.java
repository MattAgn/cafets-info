package com.example.matt.locatecafets;

import android.graphics.Bitmap;
import android.location.Location;

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
    private Location location;
    private int distanceToMe; //not clean

    //Constructors
    public Cafeteria(String name, double latitude, double longitude, String address){
        this.name = name;
        this.address = address;
        this.coordinates = new LatLng(latitude, longitude);
        this.location = new Location(name);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
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

    public Location getLocation() {
        return location;
    }

    public int getDistanceToMe() {
        return distanceToMe;
    }

    public void setDistanceToMe(int distanceToMe) {
        this.distanceToMe = distanceToMe;
    }
}


