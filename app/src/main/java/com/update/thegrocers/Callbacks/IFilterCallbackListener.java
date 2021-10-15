package com.update.thegrocers.Callbacks;

import com.update.thegrocers.Model.CategoryModel;

import java.util.List;

public interface IFilterCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
}
