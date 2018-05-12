package com.example.matt.locatecafets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by matt on 26/03/18.
 */

public class Cafeteria implements Parcelable {

    //Attributes
    private static int lastId = -1;
    private int id;
    private String name;
    private Bitmap image;
    private LatLng coordinates;
    private String website; //todo : change to URI&
    private String address;
    private Location location;
    private String openingHours;
    private double price;
    private String priceString;
    final private double UNKNOWN_PRICE = -1;
    private Marker marker;
    private int distanceToMe; //not clean

    //Constructors
    public Cafeteria(String name, double latitude, double longitude, String address, String website, String openingHours, double price){
        this.id = lastId + 1;
        lastId ++;
        this.name = name;
        this.address = address;
        this.coordinates = new LatLng(latitude, longitude);
        this.location = new Location(name);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        this.website = website;
        this.openingHours = openingHours;
        this.price = price;
        if (price == UNKNOWN_PRICE) {
            this.priceString = "";
        }
        else {
            this.priceString = String.valueOf(price) + "€";
        }
    }

    protected Cafeteria(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        coordinates = in.readParcelable(LatLng.class.getClassLoader());
        website = in.readString();
        address = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        distanceToMe = in.readInt();
        openingHours = in.readString();
        price = in.readDouble();
        priceString = in.readString();
    }

    public static final Creator<Cafeteria> CREATOR = new Creator<Cafeteria>() {
        @Override
        public Cafeteria createFromParcel(Parcel in) {
            return new Cafeteria(in);
        }

        @Override
        public Cafeteria[] newArray(int size) {
            return new Cafeteria[size];
        }
    };

    //Getters
    public int getId() { return this.id; }

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

    public String getWebsite() { return website; }

    public String getOpeningHours() { return openingHours; }

    public int getDistanceToMe() {
        return distanceToMe;
    }

    public Marker getMarker() { return this.marker; }

    public double getPrice() { return price; }

    public String getPriceString() {return priceString;}

    public void setDistanceToMe(int distanceToMe) {
        this.distanceToMe = distanceToMe;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public void setMarker(Marker marker) { this.marker = marker; }

    public void setPriceString(String priceString) { this.priceString = priceString; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable(image, flags);
        dest.writeParcelable(coordinates, flags);
        dest.writeString(website);
        dest.writeString(address);
        dest.writeParcelable(location, flags);
        dest.writeInt(distanceToMe);
        dest.writeString(openingHours);
        dest.writeDouble(price);
        dest.writeString(priceString);
    }
}


