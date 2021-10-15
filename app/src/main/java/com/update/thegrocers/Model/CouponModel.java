package com.update.thegrocers.Model;

import java.util.List;

public class CouponModel {
    private String id,code,description,image;
    private int percent;
    private List<CategoryModel> categories;
    private String imgSize;

    public CouponModel() {
    }

    public CouponModel(String id, String code, String description, String image, int percent, List<CategoryModel> categories, String imgSize) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.image = image;
        this.percent = percent;
        this.categories = categories;
        this.imgSize = imgSize;
    }

    public String getImgSize() {
        return imgSize;
    }

    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
