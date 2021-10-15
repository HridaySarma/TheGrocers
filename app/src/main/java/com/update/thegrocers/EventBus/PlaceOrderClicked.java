package com.update.thegrocers.EventBus;

public class PlaceOrderClicked {
    boolean success;

    public PlaceOrderClicked(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
