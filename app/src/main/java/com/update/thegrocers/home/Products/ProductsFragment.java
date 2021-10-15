package com.update.thegrocers.home.Products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.update.thegrocers.Adapters.ProductsAdapter;
import com.update.thegrocers.Callbacks.ICurrentFragment;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Model.ProductModel;
import com.update.thegrocers.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ProductsFragment extends Fragment {

    Unbinder unbinder ;
    ProductsViewModel productsViewModel;
    @BindView(R.id.products_recycler_view)
    RecyclerView productsRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        Common.CurrentFragment = "Products";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Other");
        unbinder = ButterKnife.bind(this,view);
        initRecyclerViews();
        return view;
    }

    private void initRecyclerViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        productsRecyclerView.setLayoutManager(linearLayoutManager);
        productsViewModel.getMutableLiveDataProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                ProductsAdapter productsAdapter = new ProductsAdapter(getContext(),productModels);
                productsRecyclerView.setAdapter(productsAdapter);
            }
        });
    }
}