package com.client.thegrocers.home.AdressList;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.client.thegrocers.Callbacks.IAddressCallback;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Model.AddressModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AddressViewModel extends ViewModel implements IAddressCallback {

    private MutableLiveData<List<AddressModel>> mutableLiveDataAddressModel;
    private MutableLiveData<String> messageError;
    private IAddressCallback addressCallbackListener;

    public AddressViewModel() {
        addressCallbackListener = this;
    }

    public MutableLiveData<List<AddressModel>> getMutableLiveDataAddressModel() {
        if (mutableLiveDataAddressModel == null){
            mutableLiveDataAddressModel = new MutableLiveData<>();
            LoadAddresses();
        }
        return mutableLiveDataAddressModel;
    }

    public MutableLiveData<String> getMessageError() {
        if (messageError == null){
            messageError = new MutableLiveData<>();
        }
        return messageError;
    }

    private void LoadAddresses() {
        List<AddressModel> tempList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCE)
               .child(Common.currentUser.getUid())
                .child("Addresses")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                            AddressModel model = itemSnapshot.getValue(AddressModel.class);
                            tempList.add(model);
                        }
                        addressCallbackListener.onAddressLoadSuccess(tempList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        addressCallbackListener.onAddressLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public void onAddressLoadSuccess(List<AddressModel> addressModels) {
        mutableLiveDataAddressModel.setValue(addressModels);
    }

    @Override
    public void onAddressLoadFailed(String message) {
        messageError.setValue(message);
    }
}
