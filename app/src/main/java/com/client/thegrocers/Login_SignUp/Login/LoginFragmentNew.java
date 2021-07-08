package com.client.thegrocers.Login_SignUp.Login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.client.thegrocers.R;
import com.client.thegrocers.UpdatedPackages.EventBus.RegisterNowClicked;
import com.client.thegrocers.databinding.FragmentLoginNewBinding;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

public class LoginFragmentNew extends Fragment {

    private FragmentLoginNewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginNewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        initButtons();
    }

    private void initButtons() {
        binding.registerNowTv.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new RegisterNowClicked(true));
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}