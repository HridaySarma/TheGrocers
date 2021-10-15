package com.update.thegrocers.Filter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.update.thegrocers.Adapters.FilterCatAdapter;
import com.update.thegrocers.Callbacks.IFilterProcessed;
import com.update.thegrocers.EventBus.FilterCategoryAdded;
import com.update.thegrocers.Model.CategoryModel;
import com.update.thegrocers.databinding.FragmentFilterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;


public class FilterFragment extends BottomSheetDialogFragment {

    FragmentFilterBinding binding;
    private FilterViewModel filterViewModel;
    private List<CategoryModel> filteredOptions = new ArrayList<>();
    private IFilterProcessed filterProcessedListener;
    private TreeMap priceList = new TreeMap<>();

    private List<CategoryModel> catStorage;
    private TreeMap<String, Integer> priceStorage;

    public FilterFragment(List<CategoryModel> catStorage, TreeMap<String, Integer> priceStorage) {
        this.catStorage = catStorage;
        this.priceStorage = priceStorage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(getLayoutInflater());
        filterViewModel = ViewModelProviders.of(this).get(FilterViewModel.class);
        filterProcessedListener = (IFilterProcessed) getParentFragment();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3 );
        binding.filterCatRv.setLayoutManager(gridLayoutManager);
        filterViewModel.getMutableLiveDataCategories().observe(getViewLifecycleOwner(), categoryModelList -> {
            FilterCatAdapter adapter = new FilterCatAdapter(categoryModelList,getContext(),catStorage);
            binding.filterCatRv.setAdapter(adapter);
        });

        if (priceStorage != null){
            if (!priceStorage.isEmpty()){
                binding.rangeSeekBarFilter.setProgress(priceStorage.get("Initial"),priceStorage.get("End"));
                binding.rangeTv.setText(new StringBuilder("Rs ").append(binding.rangeSeekBarFilter.getProgressStart()).append(" - ").append("Rs ").append(binding.rangeSeekBarFilter.getProgressEnd()));
                priceList.put("Initial",binding.rangeSeekBarFilter.getProgressStart());
                priceList.put("End",binding.rangeSeekBarFilter.getProgressEnd());
            }
        }else {
            binding.rangeTv.setText(new StringBuilder("Rs ").append(binding.rangeSeekBarFilter.getProgressStart()).append(" - ").append("Rs ").append(binding.rangeSeekBarFilter.getProgressEnd()));
            priceList.put("Initial",binding.rangeSeekBarFilter.getProgressStart());
            priceList.put("End",binding.rangeSeekBarFilter.getProgressEnd());
        }


        binding.rangeSeekBarFilter.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RangeSeekBar rangeSeekBar, int i, int i1, boolean b) {
                binding.rangeTv.setText(new StringBuilder("Rs ").append(rangeSeekBar.getProgressStart()).append(" - ").append("Rs ").append(rangeSeekBar.getProgressEnd()));
                priceList.put("Initial",rangeSeekBar.getProgressStart());
                priceList.put("End",rangeSeekBar.getProgressEnd());
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar rangeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar rangeSeekBar) {

            }
        });

        binding.clearFilterDontBtn.setOnClickListener(v -> {
            catStorage = null;
            priceStorage = null;
            filterProcessedListener.onFilterSelected(null,null);
            dismiss();
        });

        binding.filterDontBtn.setOnClickListener(v -> {
            dismiss();
            filterProcessedListener.onFilterSelected(filteredOptions,priceList);
        });

    }



    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onFilterCategoryUpdated(FilterCategoryAdded event){
        if (event.isSuccess()){
           filteredOptions = event.getCategoryModels();
        }
        EventBus.getDefault().removeStickyEvent(FilterCategoryAdded.class);
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }
}