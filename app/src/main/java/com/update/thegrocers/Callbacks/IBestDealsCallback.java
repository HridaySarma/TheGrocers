package com.update.thegrocers.Callbacks;


import com.update.thegrocers.Model.BestDealModel;

import java.util.List;

public interface IBestDealsCallback {
    void onBestDealLoadSucccess(List<BestDealModel> bestDealModelList);

}
