package com.client.thegrocers.EventBus;

import com.yuvraj.thegroceryapp.Model.Order;

public class OnlinePaymentSuccessFull {
    boolean success;
    Order order;

    public OnlinePaymentSuccessFull(boolean success, Order order) {
        this.success = success;
        this.order = order;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}