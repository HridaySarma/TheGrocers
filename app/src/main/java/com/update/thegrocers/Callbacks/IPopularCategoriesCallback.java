package com.update.thegrocers.Callbacks;


import com.update.thegrocers.Model.PopularCategoriesModel;

import java.util.List;

public interface IPopularCategoriesCallback {
    void onPopularCategoriesLoadSuccess(List<PopularCategoriesModel> popularCategoriesModelList);
    void onPopularCategoriesLoadFailed(String message);
}
