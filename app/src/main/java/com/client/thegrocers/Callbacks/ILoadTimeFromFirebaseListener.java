package com.client.thegrocers.Callbacks;


import com.client.thegrocers.Model.Order;

public interface ILoadTimeFromFirebaseListener {
    void onLoadTImeSuccess(Order order, long estimateTimeInMs);
    void onLoadTimeFailed(String message);
}
