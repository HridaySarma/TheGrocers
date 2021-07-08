package com.client.thegrocers.EventBus;


import com.client.thegrocers.Model.Order;

public class OrderRequestForCancel {
    boolean success;
    Order order;

    public OrderRequestForCancel(boolean success, Order order) {
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
