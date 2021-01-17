package com.client.thegrocers.EventBus;

import com.yuvraj.thegroceryapp.Model.AddressModel;

public class ProceedToCheckoutClicked {
    boolean success;
    AddressModel addressModel;

    public ProceedToCheckoutClicked(boolean success, AddressModel addressModel) {
        this.success = success;
        this.addressModel = addressModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }
}
