package com.client.thegrocers.EventBus;

import com.yuvraj.thegroceryapp.Model.CategoryModel;

public class CategoryClicked {
    boolean success;
    CategoryModel categoryModel;

    public CategoryClicked(boolean success, CategoryModel categoryModel) {
        this.success = success;
        this.categoryModel = categoryModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }
}
