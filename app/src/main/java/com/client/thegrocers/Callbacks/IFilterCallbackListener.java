package com.client.thegrocers.Callbacks;

import com.client.thegrocers.Model.CategoryModel;

import java.util.List;

public interface IFilterCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
}
