package com.client.thegrocers.EventBus;

public class LoginInFromAcount {
    boolean success;

    public LoginInFromAcount(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
