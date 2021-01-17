package com.client.thegrocers.EventBus;

public class LocationAddedNowBackToAddressList {
    boolean success;

    public LocationAddedNowBackToAddressList(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
