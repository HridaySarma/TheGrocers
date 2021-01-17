package com.client.thegrocers.Callbacks;


import com.yuvraj.thegroceryapp.Model.Order;

import java.util.List;

public interface ILoadOrderCallbackListener {
    void onLoadOrderSucess(List<Order> orderList);
    void onLoadOrderFailed(String message);
}
