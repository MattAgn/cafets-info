package com.example.matt.locatecafets;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matt on 26/03/18.
 */

public class Cafeteria implements Parcelable {

    //Attributes
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

    protected Cafeteria(Parcel in) {
        name = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        coordinates = in.readParcelable(LatLng.class.getClassLoader());
        website = in.readString();
        address = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        distanceToMe = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(image, flags);
        dest.writeParcelable(coordinates, flags);
        dest.writeString(website);
        dest.writeString(address);
        dest.writeParcelable(location, flags);
        dest.writeInt(distanceToMe);
    }
}


