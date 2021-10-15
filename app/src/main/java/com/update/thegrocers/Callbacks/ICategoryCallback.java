package com.update.thegrocers.Callbacks;


import com.update.thegrocers.Model.CategoryModel;

import java.util.List;

public interface ICategoryCallback  {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
    void onCategoryLoadFailed(String message);
}
