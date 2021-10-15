package com.update.thegrocers.Model;

import java.util.List;

public class ProductModel {
    private String name,id,image,description;
    private List<ImageModel> imageModelList;
    private long price,sellingPrice;
    private int quantity;
    private int key;
    private String quantityType;
    private float packageSize;
    private String totalRating,ratingCounter;

    public ProductModel() {
    }

    public ProductModel(String name, String id, String image, String description, List<ImageModel> imageModelList, long price, long sellingPrice, int quantity, int key, String quantityType, float packageSize, String totalRating, String ratingCounter) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.description = description;
        this.imageModelList = imageModelList;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.key = key;
        this.quantityType = quantityType;
        this.packageSize = packageSize;
        this.totalRating = totalRating;
        this.ratingCounter = ratingCounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImageModel> getImageModelList() {
        return imageModelList;
    }

    public void setImageModelList(List<ImageModel> imageModelList) {
        this.imageModelList = imageModelList;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(long sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public float getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(float packageSize) {
        this.packageSize = packageSize;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getRatingCounter() {
        return ratingCounter;
    }

    public void setRatingCounter(String ratingCounter) {
        this.ratingCounter = ratingCounter;
    }
}
