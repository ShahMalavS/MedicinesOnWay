package com.malav.medicinesontheway.model;

/**
 * Created by shahmalav on 21/06/17.
 */

public class Address {
    private String streetAddress;
    private String landMark;
    private String address;
    private String city;
    private String area;
    private String pincode;

    public Address(String streetAddress, String landMark, String address, String city, String area, String pincode) {
        this.streetAddress = streetAddress;
        this.landMark = landMark;
        this.address = address;
        this.city = city;
        this.area = area;
        this.pincode = pincode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
