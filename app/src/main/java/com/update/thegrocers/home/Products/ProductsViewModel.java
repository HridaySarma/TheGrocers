package com.update.thegrocers.home.Products;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Model.ProductModel;

import java.util.List;

public class ProductsViewModel extends ViewModel {

    private MutableLiveData<List<ProductModel>> mutableLiveDataProducts;

    public ProductsViewModel() {
    }

    public MutableLiveData<List<ProductModel>> getMutableLiveDataProducts() {
        if (mutableLiveDataProducts == null){
            mutableLiveDataProducts = new MutableLiveData<>();
            mutableLiveDataProducts.setValue(Common.categorySelected.getProducts());
        }
        return mutableLiveDataProducts;
    }
}
