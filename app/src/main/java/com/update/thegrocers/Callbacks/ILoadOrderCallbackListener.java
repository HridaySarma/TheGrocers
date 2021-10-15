package com.update.thegrocers.Callbacks;



import com.update.thegrocers.Model.Order;

import java.util.List;

public interface ILoadOrderCallbackListener {
    void onLoadOrderSucess(List<Order> orderList);
    void onLoadOrderFailed(String message);
}
