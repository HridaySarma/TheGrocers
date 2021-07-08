package com.client.thegrocers.Callbacks;


import com.client.thegrocers.Model.PopularCategoriesModel;

import java.util.List;

public interface IPopularCategoriesCallback {
    void onPopularCategoriesLoadSuccess(List<PopularCategoriesModel> popularCategoriesModelList);
    void onPopularCategoriesLoadFailed(String message);
}
