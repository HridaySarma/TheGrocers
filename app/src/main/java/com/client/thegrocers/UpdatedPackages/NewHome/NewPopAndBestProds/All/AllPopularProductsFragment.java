package com.client.thegrocers.UpdatedPackages.NewHome.NewPopAndBestProds.All;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.client.thegrocers.R;
import com.client.thegrocers.databinding.FragmentAllPopularProductsBinding;

public class AllPopularProductsFragment extends Fragment {

    private FragmentAllPopularProductsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAllPopularProductsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}