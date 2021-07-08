package com.client.thegrocers.Callbacks;


import com.client.thegrocers.Model.OnboardingModel;

import java.util.List;

public interface IOnboardingCallback {
    void onOnboardingLoadSuccess(List<OnboardingModel> onboardingModelList);
    void onOnboardingLoadFailed(String message);
}
