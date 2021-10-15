package com.update.thegrocers.Callbacks;

import com.update.thegrocers.Model.CategoryModel;

import java.util.List;
import java.util.TreeMap;

public interface IFilterProcessed {
    void onFilterSelected(List<CategoryModel> catList, TreeMap<String, Integer> priceRange);
}
