package com.client.thegrocers.Login_SignUp.OTP;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Model.UserModel;
import com.client.thegrocers.R;
import com.client.thegrocers.SMSApi.SmsApiClient;
import com.client.thegrocers.UpdatedPackages.CommonNew.NewUserMode;
import com.client.thegrocers.UpdatedPackages.EventBus.NewUserCreated;
import com.client.thegrocers.databinding.FragmentOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class OTPFragment extends Fragment {

    private FragmentOTPBinding binding;

    private NewUserMode userModel;
    boolean canSendCode = true;
    private String randomStr;


    public OTPFragment(NewUserMode newUserMode) {
        this.userModel = newUserMode;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (canSendCode){
            requestOtp(userModel.getPhone());
        }

    }

    private void requestOtp(String phone) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler  = new Handler(Looper.getMainLooper());
        executorService.execute(()->{
            Random random = new Random();
            int r = random.nextInt(999999);
            while (String.valueOf(r).length() != 6){
                r = random.nextInt(999999);
            }
            randomStr = String.valueOf(r);
            SmsApiClient.getClient(phone,randomStr);
        });

        handler.post(()->{
            canSendCode = false;
            binding.countdownText.setVisibility(View.GONE);
            startCountDown();
        });

    }

    private void startCountDown() {
        new CountDownTimer(60000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");

                long min = (millisUntilFinished / 60000) % 60;

                long sec = (millisUntilFinished / 1000) % 60;

                binding.countdownText.setText(f.format(min) + ":" + f.format(sec));
            }

            @Override
            public void onFinish() {
                canSendCode = true;
                binding.countdownText.setVisibility(View.GONE);
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOTPBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        binding.verifyAndCreateAccBtn.setOnClickListener(v->{
            if (binding.otpView.getText().toString().equals(randomStr)){
                createAccount(userModel);
            }else {
                Snackbar.make(v,"Incorrect OTP",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount(NewUserMode userModel) {
        FirebaseDatabase.getInstance().getReference(Common.NEW_USERS_REF)
                .child(userModel.getPhone())
                .setValue(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Common.newCurrentUser = userModel;
                            EventBus.getDefault().postSticky(new NewUserCreated(true));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }
}