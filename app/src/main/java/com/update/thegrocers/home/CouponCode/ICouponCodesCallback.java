package com.update.thegrocers.home.CouponCode;

import com.update.thegrocers.Model.CouponModel;

import java.util.List;

public interface ICouponCodesCallback {
    void onCouponCodesLoadSuccess(List<CouponModel> couponModelList);
}
