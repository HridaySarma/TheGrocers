package com.update.thegrocers.EventBus;

public class AfterOrderPlaced {
    boolean success;

    public AfterOrderPlaced(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
