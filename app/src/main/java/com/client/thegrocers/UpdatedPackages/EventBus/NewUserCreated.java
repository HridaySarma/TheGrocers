package com.client.thegrocers.UpdatedPackages.EventBus;

public class NewUserCreated {
    private boolean success;

    public NewUserCreated(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
