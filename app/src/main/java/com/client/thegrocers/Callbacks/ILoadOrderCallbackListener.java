package com.client.thegrocers.Callbacks;



import com.client.thegrocers.Model.Order;

import java.util.List;

public interface ILoadOrderCallbackListener {
    void onLoadOrderSucess(List<Order> orderList);
    void onLoadOrderFailed(String message);
}
