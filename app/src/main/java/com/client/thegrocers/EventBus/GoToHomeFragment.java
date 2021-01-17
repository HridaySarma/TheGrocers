package com.client.thegrocers.EventBus;

public class GoToHomeFragment {
    boolean success;

    public GoToHomeFragment(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

