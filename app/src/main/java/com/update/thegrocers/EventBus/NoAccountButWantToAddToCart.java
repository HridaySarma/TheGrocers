package com.update.thegrocers.EventBus;

public class NoAccountButWantToAddToCart {
    boolean categorySuccess;
    boolean detailsSuccess;

    public NoAccountButWantToAddToCart(boolean categorySuccess, boolean detailsSuccess) {
        this.categorySuccess = categorySuccess;
        this.detailsSuccess = detailsSuccess;
    }

    public boolean isCategorySuccess() {
        return categorySuccess;
    }

    public void setCategorySuccess(boolean categorySuccess) {
        this.categorySuccess = categorySuccess;
    }

    public boolean isDetailsSuccess() {
        return detailsSuccess;
    }

    public void setDetailsSuccess(boolean detailsSuccess) {
        this.detailsSuccess = detailsSuccess;
    }
}
