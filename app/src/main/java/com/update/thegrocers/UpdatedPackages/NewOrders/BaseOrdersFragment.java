package com.update.thegrocers.UpdatedPackages.NewOrders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.update.thegrocers.databinding.FragmentBaseOrdersBinding;

import org.jetbrains.annotations.NotNull;

public class BaseOrdersFragment extends Fragment {

    FragmentBaseOrdersBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBaseOrdersBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.baseOrdersPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        binding.baseOrdersTabLayout.setupWithViewPager(binding.baseOrdersPager);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
//                case 0:
//                    return new OnGoingOrdersFragment();
//                case 1:
//                    return new CompletedOrdersFragment();
//                default:
//                    return new OnGoingOrdersFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Ongoing Orders";
                case 1:
                    return "CompletedOrders";
                default:
                    return "Ongoing Orders";
            }
        }
    }

}