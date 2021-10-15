package com.update.thegrocers.Callbacks;


import com.update.thegrocers.Model.OnboardingModel;

import java.util.List;

public interface IOnboardingCallback {
    void onOnboardingLoadSuccess(List<OnboardingModel> onboardingModelList);
    void onOnboardingLoadFailed(String message);
}
