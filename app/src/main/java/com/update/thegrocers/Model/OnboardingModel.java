package com.update.thegrocers.Model;

public class OnboardingModel {

    private String title,image;

    public OnboardingModel(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public OnboardingModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
