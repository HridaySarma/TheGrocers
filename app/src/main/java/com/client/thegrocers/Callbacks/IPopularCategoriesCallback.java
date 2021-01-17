package com.client.thegrocers.Callbacks;

import com.yuvraj.thegroceryapp.Model.PopularCategoriesModel;

import java.util.List;

public interface IPopularCategoriesCallback {
    void onPopularCategoriesLoadSuccess(List<PopularCategoriesModel> popularCategoriesModelList);
    void onPopularCategoriesLoadFailed(String message);
}
