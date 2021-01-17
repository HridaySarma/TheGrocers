package com.client.thegrocers.Callbacks;

import com.yuvraj.thegroceryapp.Model.OrdersModel;

import java.util.List;

public interface IOrdersCallback {
    void onOrdersLoadSuccess(List<OrdersModel> ordersModelList);
    void onOrdersLoadFailed(String message);
}
