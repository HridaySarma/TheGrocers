package com.update.thegrocers.Callbacks;


import com.update.thegrocers.Model.Order;

public interface ILoadTimeFromFirebaseListener {
    void onLoadTImeSuccess(Order order, long estimateTimeInMs);
    void onLoadTimeFailed(String message);
}
