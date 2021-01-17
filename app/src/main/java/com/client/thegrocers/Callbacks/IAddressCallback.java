package com.client.thegrocers.Callbacks;


import com.yuvraj.thegroceryapp.Model.AddressModel;

import java.util.List;

public interface IAddressCallback {
    void onAddressLoadSuccess(List<AddressModel> addressModels);
    void onAddressLoadFailed(String message);
}
