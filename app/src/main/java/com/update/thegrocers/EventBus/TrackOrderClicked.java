package com.update.thegrocers.EventBus;

import com.update.thegrocers.Model.OngoingOrdersModel;

public class TrackOrderClicked {
    private boolean success;
    private OngoingOrdersModel ongoingOrdersModel;

    public TrackOrderClicked(boolean success, OngoingOrdersModel ongoingOrdersModel) {
        this.success = success;
        this.ongoingOrdersModel = ongoingOrdersModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public OngoingOrdersModel getOngoingOrdersModel() {
        return ongoingOrdersModel;
    }

    public void setOngoingOrdersModel(OngoingOrdersModel ongoingOrdersModel) {
        this.ongoingOrdersModel = ongoingOrdersModel;
    }
}
