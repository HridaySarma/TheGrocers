package com.update.thegrocers.Callbacks;


import com.update.thegrocers.Model.Order;

import java.util.List;

public interface IOrdersCallback {
    void onOrdersLoadSuccess(List<Order> ordersModelList);
    void onOrdersLoadFailed(String message);
}
