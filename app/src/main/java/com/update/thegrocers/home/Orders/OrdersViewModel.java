package com.update.thegrocers.home.Orders;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.update.thegrocers.Model.Order;

import java.util.List;

public class OrdersViewModel extends ViewModel {

    private MutableLiveData<List<Order>> mutableLiveDataOrderList;

    public OrdersViewModel(){
        mutableLiveDataOrderList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Order>> getMutableLiveDataOrderList() {
        return mutableLiveDataOrderList;
    }

    public void setMutableLiveDataOrderList(List<Order> orderList){
        mutableLiveDataOrderList.setValue(orderList);
    }
}
