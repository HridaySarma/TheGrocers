package com.update.thegrocers.home.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.update.thegrocers.Adapters.AnyProdAdapter;
import com.update.thegrocers.Callbacks.ICurrentFragment;
import com.update.thegrocers.Callbacks.IFilterProcessed;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Filter.FilterFragment;
import com.update.thegrocers.Model.CategoryModel;
import com.update.thegrocers.Model.SingletonProductModel;
import com.update.thegrocers.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment implements IFilterProcessed {


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
    @BindView(R.id.filter_cv)
    CardView filterCv;
    private static List<SingletonProductModel> singoModel;
    private List<CategoryModel> catStorage ;
    private TreeMap<String, Integer> priceStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchAnyProductViewModel = ViewModelProviders.of(this).get(SearchAnyProductViewModel.class);
        View view  =inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this,view);
        Common.CurrentFragment = "Search";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Search");
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
                if (singletonProductModelList == null){
                    searchAnim.setVisibility(View.VISIBLE);
                }else {
                    singoModel = singletonProductModelList;
                    anyProdAdapter = new AnyProdAdapter(getContext(),singletonProductModelList);
                    allProdRecyclerView.setAdapter(anyProdAdapter);
                    searchAnim.setVisibility(View.GONE);
                }
            }
        });

        filterCv.setOnClickListener(v -> {

            FilterFragment filterFragment = new FilterFragment(catStorage,priceStorage);
            filterFragment.show(getChildFragmentManager(),"BottomFragment");
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

    @Override
    public void onFilterSelected(List<CategoryModel> catList, TreeMap<String, Integer> priceRange) {
       if (catList != null && catList.size() != 0 && !priceRange.isEmpty() ){
           List<SingletonProductModel> filteredList = new ArrayList<>();
           catStorage = catList;
           priceStorage = priceRange;
           for (int i = 0;i<catList.size();i++){
               for (int j =0;j<singoModel.size();j++){
                   if (catList.get(i).getName().equals(singoModel.get(j).getCategory_id())){
                       if (singoModel.get(j).getSellingPrice() >= priceRange.get("Initial") && singoModel.get(j).getSellingPrice() <= (priceRange.get("End"))){
                           filteredList.add(singoModel.get(j));
                       }
                   }
               }
           }
           searchAnyProductViewModel.getListMutableLiveDataAllProds().setValue(filteredList);
       }else if (priceRange != null){
           List<SingletonProductModel> filteredList = new ArrayList<>();
           priceStorage= priceRange;
               for (int j =0;j<singoModel.size();j++){
                       if (singoModel.get(j).getSellingPrice() >= priceRange.get("Initial") && singoModel.get(j).getSellingPrice() <= (priceRange.get("End"))){
                           filteredList.add(singoModel.get(j));
                       }
           }
           searchAnyProductViewModel.getListMutableLiveDataAllProds().setValue(filteredList);
       }else {
           priceStorage = null;
           catStorage = null;
           searchAnyProductViewModel.LoadTheProducts();
       }
    }
}