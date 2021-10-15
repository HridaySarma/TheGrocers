package com.update.thegrocers.Callbacks;


import com.update.thegrocers.Model.BannerModel;

import java.util.List;

public interface IBannerCallback {
    void onBannerLoadSuccess(List<BannerModel> bannerModelList);
    void onBannerLoadFailed(String message);
}
