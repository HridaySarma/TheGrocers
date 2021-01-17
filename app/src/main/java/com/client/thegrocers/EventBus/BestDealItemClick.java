package com.client.thegrocers.EventBus;

import com.yuvraj.thegroceryapp.Model.BestDealModel;

public class BestDealItemClick {
   boolean success;
   BestDealModel bestDealModel;

    public BestDealItemClick(boolean success, BestDealModel bestDealModel) {
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
