package com.update.thegrocers.home.Search;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.update.thegrocers.Callbacks.IAnyProductCallback;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Model.SingletonProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchAnyProductViewModel extends ViewModel implements IAnyProductCallback {

    private MutableLiveData<List<SingletonProductModel>> listMutableLiveDataAllProds;
    private MutableLiveData<String> messageError;
    private IAnyProductCallback anyProductCallbackListener;

    public SearchAnyProductViewModel() {
        anyProductCallbackListener = this;
    }

    public MutableLiveData<List<SingletonProductModel>> getListMutableLiveDataAllProds() {
        if (listMutableLiveDataAllProds == null){
            listMutableLiveDataAllProds = new MutableLiveData<>();
            LoadTheProducts();
        }
        return listMutableLiveDataAllProds;
    }

    public MutableLiveData<String> getMessageError() {
        if (messageError == null){
            messageError = new MutableLiveData<>();
        }
        return messageError;
    }

    public void LoadTheProducts() {
        List<SingletonProductModel> tempList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.ALL_PRODUCTS_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot:snapshot.getChildren()){
                            SingletonProductModel singletonProductModel = itemSnapshot.getValue(SingletonProductModel.class);
                            tempList.add(singletonProductModel);
                        }
                        anyProductCallbackListener.onAnyProdLoadSuccess(tempList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        anyProductCallbackListener.onAnyProdLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public void onAnyProdLoadSuccess(List<SingletonProductModel> singletonProductModelList) {
        listMutableLiveDataAllProds.setValue(singletonProductModelList);
    }

    @Override
    public void onAnyProdLoadFailed(String message) {
        messageError.setValue(message);
    }
}
