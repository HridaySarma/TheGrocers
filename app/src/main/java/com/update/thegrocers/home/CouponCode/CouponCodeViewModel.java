package com.update.thegrocers.home.CouponCode;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Model.CouponModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CouponCodeViewModel extends ViewModel implements ICouponCodesCallback {

    private MutableLiveData<List<CouponModel>> mutableLiveDataCouponCodes;
    private ICouponCodesCallback couponCodesCallback;

    public CouponCodeViewModel() {
        couponCodesCallback = this;
    }

    public MutableLiveData<List<CouponModel>> getMutableLiveDataCouponCodes() {
        if (mutableLiveDataCouponCodes == null){
            mutableLiveDataCouponCodes = new MutableLiveData<>();
            LoadCouponCodes();
        }
        return mutableLiveDataCouponCodes;
    }

    private void LoadCouponCodes() {
        List<CouponModel> couponModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.COUPON_CODES_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                            CouponModel couponModel = itemSnapshot.getValue(CouponModel.class);
                            couponModelList.add(couponModel);
                        }
                        couponCodesCallback.onCouponCodesLoadSuccess(couponModelList);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onCouponCodesLoadSuccess(List<CouponModel> couponModelList) {
        mutableLiveDataCouponCodes.setValue(couponModelList);
    }
}
