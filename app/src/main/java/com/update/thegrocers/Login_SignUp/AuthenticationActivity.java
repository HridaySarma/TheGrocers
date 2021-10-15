package com.update.thegrocers.Login_SignUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.update.thegrocers.Login_SignUp.OTP.OTPFragment;
import com.update.thegrocers.R;
import com.update.thegrocers.UpdatedPackages.EventBus.GoToOtpFrag;
import com.update.thegrocers.UpdatedPackages.EventBus.NewUserCreated;
import com.update.thegrocers.databinding.ActivityAuthenticationBinding;
import com.update.thegrocers.home.HomeActivity;
import com.update.thegrocers.home.Login.LoginFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AuthenticationActivity extends AppCompatActivity {

    private ActivityAuthenticationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginFragment loginFragment = new LoginFragment();
        changeFragment(loginFragment);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();

    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.auth_container,fragment);
        fragmentTransaction.commit();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onGoToOtpFrag(GoToOtpFrag event){
        if (event.isSuccess()){
            OTPFragment otpFragment = new OTPFragment(event.getNewUserMode());
            changeFragment(otpFragment);
            EventBus.getDefault().removeStickyEvent(GoToOtpFrag.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onNewUserCreated(NewUserCreated event){
        Intent intent = new Intent(AuthenticationActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}