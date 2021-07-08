package com.client.thegrocers.Callbacks;

import com.client.thegrocers.Model.OngoingOrdersModel;

import java.util.List;

public interface IOnGoingOrdersCallback {
    void onOnGoingOrdersLoadSuccess(List<OngoingOrdersModel> ordersModelList);
}
