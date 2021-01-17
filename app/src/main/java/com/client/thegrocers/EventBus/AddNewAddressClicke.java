package com.client.thegrocers.EventBus;

public class AddNewAddressClicke {
    boolean success;

    public AddNewAddressClicke(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
