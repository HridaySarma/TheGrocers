package com.client.thegrocers.UpdatedPackages.EventBus;

public class RegisterNowClicked {
    private boolean success;

    public RegisterNowClicked(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
