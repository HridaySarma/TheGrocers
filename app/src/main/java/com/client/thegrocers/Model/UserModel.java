package com.client.thegrocers.Model;

public class UserModel {

    private String uid, name, lastName, phone, profilePicture,email;


    public UserModel(String uid, String name, String lastName, String phone, String profilePicture, String email) {
        this.uid = uid;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.email = email;
    }

    public UserModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
