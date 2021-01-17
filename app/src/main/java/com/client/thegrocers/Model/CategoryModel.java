package com.client.thegrocers.Model;

import java.util.List;

public class CategoryModel {

    private String name,image,key;
    private List<ProductModel> products;

    public CategoryModel(String name, String image, String key, List<ProductModel> products) {
        this.name = name;
        this.image = image;
        this.key = key;
        this.products = products;
    }

    public CategoryModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
