package com.update.thegrocers.UpdatedPackages.NewOrders;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.update.thegrocers.Callbacks.IOnGoingOrdersCallback;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Model.OngoingOrdersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OngoingOrdersViewModel extends ViewModel implements IOnGoingOrdersCallback {
    private MutableLiveData<List<OngoingOrdersModel>> mutableLiveDataOngoingOrders;
    private IOnGoingOrdersCallback onGoingOrdersCallbackListener;

    public OngoingOrdersViewModel() {
        onGoingOrdersCallbackListener = this;
    }

    public MutableLiveData<List<OngoingOrdersModel>> getMutableLiveDataOngoingOrders() {
        if (mutableLiveDataOngoingOrders == null){
            mutableLiveDataOngoingOrders = new MutableLiveData<>();
            LoadOngoingOrders();
        }
        return mutableLiveDataOngoingOrders;
    }

    private void LoadOngoingOrders() {
        List<OngoingOrdersModel> tempList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.ONGOING_ORDERS_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapShot : snapshot.getChildren()){
                            OngoingOrdersModel ongoingOrdersModel = itemSnapShot.getValue(OngoingOrdersModel.class);
                            tempList.add(ongoingOrdersModel);
                        }
                        onGoingOrdersCallbackListener.onOnGoingOrdersLoadSuccess(tempList);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onOnGoingOrdersLoadSuccess(List<OngoingOrdersModel> ordersModelList) {
        mutableLiveDataOngoingOrders.setValue(ordersModelList);
    }
}
