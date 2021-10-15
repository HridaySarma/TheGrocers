package com.update.thegrocers.Model;

public class SingletonProductModel {

    private String name,image;
    private Long sellingPrice;
    private String category_id;
    private String product_id;

    public SingletonProductModel(String name, String image, Long sellingPrice, String category_id, String product_id) {
        this.name = name;
        this.image = image;
        this.sellingPrice = sellingPrice;
        this.category_id = category_id;
        this.product_id = product_id;
    }

    public SingletonProductModel() {
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

    public Long getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Long sellingPrice) {
        this.sellingPrice = sellingPrice;
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
}
