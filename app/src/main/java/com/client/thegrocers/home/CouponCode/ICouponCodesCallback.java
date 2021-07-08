package com.client.thegrocers.home.CouponCode;

import com.client.thegrocers.Model.CouponModel;

import java.util.List;

public interface ICouponCodesCallback {
    void onCouponCodesLoadSuccess(List<CouponModel> couponModelList);
}
