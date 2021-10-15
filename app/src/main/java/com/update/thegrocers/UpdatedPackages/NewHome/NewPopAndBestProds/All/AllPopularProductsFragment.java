package com.update.thegrocers.UpdatedPackages.NewHome.NewPopAndBestProds.All;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.update.thegrocers.databinding.FragmentAllPopularProductsBinding;

public class AllPopularProductsFragment extends Fragment {

    private FragmentAllPopularProductsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAllPopularProductsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}