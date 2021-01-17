package com.client.thegrocers.Callbacks;


import com.yuvraj.thegroceryapp.Model.SingletonProductModel;

import java.util.List;

public interface IAnyProductCallback {
    void onAnyProdLoadSuccess(List<SingletonProductModel> singletonProductModelList);
    void onAnyProdLoadFailed(String message);
}
