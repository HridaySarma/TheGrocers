package com.client.thegrocers.EventBus;


import com.client.thegrocers.Model.PopularCategoriesModel;

public class PopularCategoryClick {
    boolean success;
    PopularCategoriesModel popularCategoriesModel;

    public PopularCategoryClick(boolean success, PopularCategoriesModel popularCategoriesModel) {
        this.success = success;
        this.popularCategoriesModel = popularCategoriesModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PopularCategoriesModel getPopularCategoriesModel() {
        return popularCategoriesModel;
    }

    public void setPopularCategoriesModel(PopularCategoriesModel popularCategoriesModel) {
        this.popularCategoriesModel = popularCategoriesModel;
    }
}
