package com.update.thegrocers.EventBus;

public class ProductClicked {
   boolean success;

    public ProductClicked(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
