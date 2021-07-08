package com.client.thegrocers.Callbacks;


import com.client.thegrocers.Model.CategoryModel;

import java.util.List;

public interface ICategoryCallback  {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
    void onCategoryLoadFailed(String message);
}
