package com.client.thegrocers.Callbacks;


import com.client.thegrocers.Model.OrdersModel;

import java.util.List;

public interface IOrdersCallback {
    void onOrdersLoadSuccess(List<OrdersModel> ordersModelList);
    void onOrdersLoadFailed(String message);
}
