package com.update.thegrocers.Model;

public class PopularCategoriesModel {

    private String name,image,desc,category_id;

    public PopularCategoriesModel(String name, String image, String desc, String category_id) {
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.category_id = category_id;
    }

    public PopularCategoriesModel() {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
