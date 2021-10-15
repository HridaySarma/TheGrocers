package com.update.thegrocers.EventBus;

public class LoginStatus {

    boolean success;

    public LoginStatus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
