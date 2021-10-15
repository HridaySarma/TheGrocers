package com.update.thegrocers.EventBus;

public class BuyNowClicked {

    boolean success;

    public BuyNowClicked(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
