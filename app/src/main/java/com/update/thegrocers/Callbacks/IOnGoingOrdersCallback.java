package com.update.thegrocers.Callbacks;

import com.update.thegrocers.Model.OngoingOrdersModel;

import java.util.List;

public interface IOnGoingOrdersCallback {
    void onOnGoingOrdersLoadSuccess(List<OngoingOrdersModel> ordersModelList);
}
