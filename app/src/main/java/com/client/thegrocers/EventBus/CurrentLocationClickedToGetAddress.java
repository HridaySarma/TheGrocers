package com.client.thegrocers.EventBus;

public class CurrentLocationClickedToGetAddress {
    boolean success;

    public CurrentLocationClickedToGetAddress(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
