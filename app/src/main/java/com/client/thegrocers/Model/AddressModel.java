package com.client.thegrocers.Model;

public class AddressModel {

    private String name,address,streetAddress,landmark,state,pincode,city;

    public AddressModel(String name, String address, String streetAddress, String landmark, String state, String pincode, String city) {
        this.name = name;
        this.address = address;
        this.streetAddress = streetAddress;
        this.landmark = landmark;
        this.state = state;
        this.pincode = pincode;
        this.city = city;
    }

    public AddressModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
