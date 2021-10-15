package com.update.thegrocers.Callbacks;



import com.update.thegrocers.Model.SingletonProductModel;

import java.util.List;

public interface IAnyProductCallback {
    void onAnyProdLoadSuccess(List<SingletonProductModel> singletonProductModelList);
    void onAnyProdLoadFailed(String message);
}
