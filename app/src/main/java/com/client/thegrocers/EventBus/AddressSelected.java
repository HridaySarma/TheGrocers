package com.client.thegrocers.EventBus;


import com.client.thegrocers.Model.AddressModel;

public class AddressSelected {
    boolean success;
    AddressModel addressModel;

    public AddressSelected(boolean success, AddressModel addressModel) {
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
