package com.client.thegrocers.Callbacks;


import com.client.thegrocers.Model.BannerModel;

import java.util.List;

public interface IBannerCallback {
    void onBannerLoadSuccess(List<BannerModel> bannerModelList);
    void onBannerLoadFailed(String message);
}
