package com.client.thegrocers.Model;

public class ReviewModel {

    private int day,month,year;
    private String description,phone;
    private float rating;
    private UserModel userModel;

    public ReviewModel(int day, int month, int year, String description, String phone, float rating, UserModel userModel) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.description = description;
        this.phone = phone;
        this.rating = rating;
        this.userModel = userModel;
    }

    public ReviewModel() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

}
