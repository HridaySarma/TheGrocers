package com.client.thegrocers.EventBus;

import com.client.thegrocers.Model.CategoryModel;

import java.util.List;

public class FilterCategoryAdded {
    private boolean success;
    private List<CategoryModel> categoryModels;

    public FilterCategoryAdded(boolean success, List<CategoryModel> categoryModels) {
        this.success = success;
        this.categoryModels = categoryModels;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CategoryModel> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(List<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }
}


