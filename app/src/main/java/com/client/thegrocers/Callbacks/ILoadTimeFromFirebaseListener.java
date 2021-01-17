package com.client.thegrocers.Callbacks;

import com.yuvraj.thegroceryapp.Model.Order;

public interface ILoadTimeFromFirebaseListener {
    void onLoadTImeSuccess(Order order, long estimateTimeInMs);
    void onLoadTimeFailed(String message);
}
