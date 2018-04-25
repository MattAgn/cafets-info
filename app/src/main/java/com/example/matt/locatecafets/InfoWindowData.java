package com.example.matt.locatecafets;

/**
 * Created by matt on 10/04/18.
 */


public class InfoWindowData {

    private String openingHours;
    private int id;
    private String address;

    public int getId() { return id;}

    public String getOpeningHours() {return openingHours; }

    public String getAddress() {return address; }

    public void setId(int id) { this.id = id; }

    public void setAddress(String address) { this.address = address; }

    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

}
