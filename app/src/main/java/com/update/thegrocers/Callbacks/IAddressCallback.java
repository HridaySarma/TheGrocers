package com.update.thegrocers.Callbacks;



import com.update.thegrocers.Model.AddressModel;

import java.util.List;

public interface IAddressCallback {
    void onAddressLoadSuccess(List<AddressModel> addressModels);
    void onAddressLoadFailed(String message);
}
