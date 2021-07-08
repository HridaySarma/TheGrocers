package com.client.thegrocers.Callbacks;

import com.client.thegrocers.Model.CategoryModel;

import java.util.Deque;
import java.util.List;
import java.util.TreeMap;

public interface IFilterProcessed {
    void onFilterSelected(List<CategoryModel> catList, TreeMap<String, Integer> priceRange);
}
