package com.update.thegrocers.EventBus;

public class CartClearedInPlaceOrder {
    boolean success;

    public CartClearedInPlaceOrder(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
