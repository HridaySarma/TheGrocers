package com.client.thegrocers.EventBus;

public class ViewAllCatsClicked {
    private boolean success;

    public ViewAllCatsClicked(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
