package com.example.matt.locatecafets;

public class InfoWindowData {

    private String openingHours;
    private int id;
    private String address;
    private String website;

    public int getId() { return id;}

    public String getOpeningHours() {return openingHours; }

    public String getAddress() {return address; }

    public String getWebsite() { return website; }

    public void setId(int id) { this.id = id; }

    public void setWebsite(String website) { this.website = website; }

    public void setAddress(String address) { this.address = address; }

    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

}
