package com.client.thegrocers.Callbacks;


import com.client.thegrocers.Model.BestDealModel;

import java.util.List;

public interface IBestDealsCallback {
    void onBestDealLoadSucccess(List<BestDealModel> bestDealModelList);

}
