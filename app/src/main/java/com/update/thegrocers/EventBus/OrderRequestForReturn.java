package com.update.thegrocers.EventBus;


import com.update.thegrocers.Model.Order;

public class OrderRequestForReturn {
    boolean success;
    Order order;

    public OrderRequestForReturn(boolean success, Order order) {
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
