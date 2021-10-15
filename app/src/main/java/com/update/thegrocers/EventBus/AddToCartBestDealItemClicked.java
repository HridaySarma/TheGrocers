package com.update.thegrocers.EventBus;

import com.update.thegrocers.Model.BestDealModel;

public class AddToCartBestDealItemClicked {
    private boolean success;
    private BestDealModel bestDealModel;


    public AddToCartBestDealItemClicked(boolean success, BestDealModel bestDealModel) {
        this.success = success;
        this.bestDealModel = bestDealModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BestDealModel getBestDealModel() {
        return bestDealModel;
    }

    public void setBestDealModel(BestDealModel bestDealModel) {
        this.bestDealModel = bestDealModel;
    }
}
