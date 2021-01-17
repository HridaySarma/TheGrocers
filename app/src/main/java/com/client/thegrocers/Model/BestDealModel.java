package com.client.thegrocers.Model;

public class BestDealModel {

    private String category_id,product_id,image,name;
    private long price;

    public BestDealModel(String category_id, String product_id, String image, String name, long price) {
        this.category_id = category_id;
        this.product_id = product_id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public BestDealModel() {
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
