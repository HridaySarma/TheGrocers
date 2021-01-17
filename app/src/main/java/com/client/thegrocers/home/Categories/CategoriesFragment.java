package com.client.thegrocers.home.Categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yuvraj.thegroceryapp.Adapters.MainCategoryAdapter;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.R;
import com.yuvraj.thegroceryapp.home.HomeViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.category_main_recycler_view)
    RecyclerView categoryRecyclerView;
    HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        Common.CurrentFragment = "Categories";
        unbinder = ButterKnife.bind(this,view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        homeViewModel.getMutableLiveDataCategories().observe(getViewLifecycleOwner(), categoryModelList -> {
            MainCategoryAdapter adapter = new MainCategoryAdapter(getContext(),categoryModelList);
            categoryRecyclerView.setAdapter(adapter);
        });
    }
}