package com.client.thegrocers.Callbacks;



import com.client.thegrocers.Model.SingletonProductModel;

import java.util.List;

public interface IAnyProductCallback {
    void onAnyProdLoadSuccess(List<SingletonProductModel> singletonProductModelList);
    void onAnyProdLoadFailed(String message);
}
