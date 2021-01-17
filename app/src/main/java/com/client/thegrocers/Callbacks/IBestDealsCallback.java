package com.client.thegrocers.Callbacks;

import com.yuvraj.thegroceryapp.Model.BestDealModel;

import java.util.List;

public interface IBestDealsCallback {
    void onBestDealLoadSucccess(List<BestDealModel> bestDealModelList);

}
