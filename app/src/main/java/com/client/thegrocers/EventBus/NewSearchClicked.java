package com.client.thegrocers.EventBus;

public class NewSearchClicked {
    private boolean success;

    public NewSearchClicked(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
