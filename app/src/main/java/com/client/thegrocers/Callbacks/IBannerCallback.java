package com.client.thegrocers.Callbacks;

import com.yuvraj.thegroceryapp.Model.BannerModel;

import java.util.List;

public interface IBannerCallback {
    void onBannerLoadSuccess(List<BannerModel> bannerModelList);
    void onBannerLoadFailed(String message);
}
