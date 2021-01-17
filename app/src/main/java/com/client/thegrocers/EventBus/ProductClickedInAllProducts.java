package com.client.thegrocers.EventBus;

import com.yuvraj.thegroceryapp.Model.SingletonProductModel;

public class ProductClickedInAllProducts {
    boolean success;
    SingletonProductModel singletonProductModel;

    public ProductClickedInAllProducts(boolean success, SingletonProductModel singletonProductModel) {
        this.success = success;
        this.singletonProductModel = singletonProductModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SingletonProductModel getSingletonProductModel() {
        return singletonProductModel;
    }

    public void setSingletonProductModel(SingletonProductModel singletonProductModel) {
        this.singletonProductModel = singletonProductModel;
    }
}
