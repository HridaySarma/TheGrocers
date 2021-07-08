package com.client.thegrocers;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.client.thegrocers.Callbacks.IOnboardingCallback;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Model.OnboardingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OnboardingViewModel extends ViewModel implements IOnboardingCallback {

    MutableLiveData<List<OnboardingModel>> mutableLiveDataOnboarding;
    MutableLiveData<String> messageError;
    IOnboardingCallback onboardingCallbackListener;

    public OnboardingViewModel() {
        onboardingCallbackListener = this;
        messageError = new MutableLiveData<>();
    }

    public MutableLiveData<List<OnboardingModel>> getMutableLiveDataOnboarding() {
        if (mutableLiveDataOnboarding == null){
            mutableLiveDataOnboarding = new MutableLiveData<>();
            LoadOnboarding();
        }
        return mutableLiveDataOnboarding;
    }



    private void LoadOnboarding() {
        List<OnboardingModel> onboardingModelList =new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.ONBOARDING_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                            OnboardingModel onboardingModel = itemSnapshot.getValue(OnboardingModel.class);
                            onboardingModelList.add(onboardingModel);
                        }
                        onboardingCallbackListener.onOnboardingLoadSuccess(onboardingModelList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onboardingCallbackListener.onOnboardingLoadFailed(error.getMessage());
                    }
                });
    }

    public MutableLiveData<String> getMessageError() {

        return messageError;
    }

    @Override
    public void onOnboardingLoadSuccess(List<OnboardingModel> onboardingModelList) {
        mutableLiveDataOnboarding.setValue(onboardingModelList);
    }

    @Override
    public void onOnboardingLoadFailed(String message) {
        messageError.setValue(message);
    }
}
