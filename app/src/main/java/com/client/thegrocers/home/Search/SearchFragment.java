package com.client.thegrocers.home.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.yuvraj.thegroceryapp.Adapters.AnyProdAdapter;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Model.SingletonProductModel;
import com.yuvraj.thegroceryapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.any_product_recycler_view)
    RecyclerView allProdRecyclerView;
    private AnyProdAdapter anyProdAdapter;
    @BindView(R.id.searchAnim)
    LottieAnimationView searchAnim;
    private SearchAnyProductViewModel searchAnyProductViewModel;
    @BindView(R.id.floating_search_view_any_prods)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.searchLay)
    LinearLayout searchLay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchAnyProductViewModel = ViewModelProviders.of(this).get(SearchAnyProductViewModel.class);
        View view  =inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this,view);
        Common.CurrentFragment = "Search";
        initViews();
        setTextWatchers();
        return view;
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        allProdRecyclerView.setLayoutManager(linearLayoutManager);
        searchAnyProductViewModel.getListMutableLiveDataAllProds().observe(getViewLifecycleOwner(), new Observer<List<SingletonProductModel>>() {
            @Override
            public void onChanged(List<SingletonProductModel> singletonProductModelList) {
                if (singletonProductModelList == null || singletonProductModelList.equals("")){
                    searchAnim.setVisibility(View.VISIBLE);
                }else {
                    anyProdAdapter = new AnyProdAdapter(getContext(),singletonProductModelList);
                    allProdRecyclerView.setAdapter(anyProdAdapter);
                    searchAnim.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setTextWatchers() {
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                if (currentQuery.equals("")){
                    searchAnyProductViewModel.LoadTheProducts();
                }else {
                    startSearch(currentQuery);
                }

            }
        });

        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (newQuery.contains("")){
                    searchAnyProductViewModel.LoadTheProducts();
                }
            }
        });
    }

    private void startSearch(String newQuery) {
        List<SingletonProductModel> resultList = new ArrayList<>();
        for (int i=0; i<anyProdAdapter.getListOfProducts().size();i++){
            SingletonProductModel singletonProductModel= new SingletonProductModel();
            singletonProductModel = anyProdAdapter.getListOfProducts().get(i);

            if ( singletonProductModel.getName().contains(newQuery) || singletonProductModel.getName().toUpperCase().contains(newQuery)  || singletonProductModel.getName().toLowerCase().contains(newQuery.toLowerCase()) || singletonProductModel.getName().toUpperCase().contains(newQuery.toUpperCase()))
                resultList.add(singletonProductModel);
        }
        searchAnyProductViewModel.getListMutableLiveDataAllProds().setValue(resultList);


    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (!EventBus.getDefault().isRegistered(this)){
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @Override
//    public void onStop() {
//         if (EventBus.getDefault().isRegistered(this)){
//             EventBus.getDefault().unregister(this);
//         }
//        super.onStop();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}