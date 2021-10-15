package com.update.thegrocers.UpdatedPackages.NewProductDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.update.thegrocers.Adapters.ProductDetailsImagesAdapter;
import com.update.thegrocers.Callbacks.ICurrentFragment;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Database.CartDataSource;
import com.update.thegrocers.Database.CartDatabase;
import com.update.thegrocers.Database.CartItem;
import com.update.thegrocers.Database.LocalCartDataSource;
import com.update.thegrocers.EventBus.CounterCartEvent;
import com.update.thegrocers.EventBus.NoAccountButWantToAddToCart;
import com.update.thegrocers.Model.ProductModel;
import com.update.thegrocers.Model.ReviewModel;
import com.update.thegrocers.NewAdapters.ReviewAdapter;
import com.update.thegrocers.NewAdapters.SimilarProductsAdapter;
import com.update.thegrocers.databinding.FragmentNewProductDetailsBinding;
import com.update.thegrocers.databinding.FragmentReviewBottomSheetDialogBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.main.zoomingrecyclerview.ZoomingRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewProductDetailsFragment extends Fragment {

    private FragmentNewProductDetailsBinding binding;

    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;
    int quanty = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewProductDetailsBinding.inflate(getLayoutInflater());
        compositeDisposable = new CompositeDisposable();
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("ProductDetails");
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        initPager();
        initOtherDetails();
        initDescription();
        initButtons();
        initReviews();
        initReviewDetail();
        initSimilarProducts();
    }

    private void initReviewDetail() {
        if (Common.selectedProduct.getTotalRating() != null){
            binding.ratingsAndReviewsLinearLayout.setVisibility(View.VISIBLE);
            binding.noRatingsAndReviewsTv.setVisibility(View.GONE);
            binding.mainRatingBarNewDet.setRating(Float.parseFloat(Common.selectedProduct.getTotalRating())/Float.parseFloat(Common.selectedProduct.getRatingCounter()));
            binding.mainRatingTv.setText(String.valueOf(Float.parseFloat(Common.selectedProduct.getTotalRating())/Float.parseFloat(Common.selectedProduct.getRatingCounter())));
            binding.mainRatingTotalReviews.setText(new StringBuilder(Common.selectedProduct.getRatingCounter()).append(" reviews"));
        }else {
            binding.ratingsAndReviewsLinearLayout.setVisibility(View.GONE);
            binding.noRatingsAndReviewsTv.setVisibility(View.VISIBLE);
        }

    }

    private void initSimilarProducts() {
        List<ProductModel> tempProducts = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                .child(Common.categorySelected.getName())
                .child("products")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot :snapshot.getChildren()){
                            ProductModel productModel = itemSnapshot.getValue(ProductModel.class);
                            if (!productModel.getName().equals(Common.selectedProduct.getName())){
                                tempProducts.add(productModel);
                            }
                            showSimilarProductList(tempProducts);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void showSimilarProductList(List<ProductModel> simProdsList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        binding.similarProdsRv.setLayoutManager(linearLayoutManager);
        SimilarProductsAdapter similarProductsAdapter = new SimilarProductsAdapter(getContext(),simProdsList);
        binding.similarProdsRv.setAdapter(similarProductsAdapter);
        binding.similarProdsRv.setVerticalFadingEdgeEnabled(true);
        binding.similarProdsRv.setVerticalScrollBarEnabled(true);
    }

    private void initReviews() {


        List<ReviewModel> reviewsList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                .child(Common.categorySelected.getName())
                .child("products")
                .child(String.valueOf(Common.selectedProduct.getKey()))
                .child("ratings")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                                ReviewModel reviewModel = itemSnapshot.getValue(ReviewModel.class);
                                reviewsList.add(reviewModel);
                            }
                            addDataToReviewList(reviewsList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void addDataToReviewList(List<ReviewModel> reviewsList) {
        LinearLayoutManager linearLayoutManager = new ZoomingRecyclerView(getActivity(), (float) 0.1, (float) 0.2);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.reviewsRvNew.setLayoutManager(linearLayoutManager);
        ReviewAdapter reviewAdapter = new ReviewAdapter(getContext(),reviewsList);
        binding.reviewsRvNew.setAdapter(reviewAdapter);
    }

    private void initButtons() {

        binding.incrementBtnNewProdD.setOnClickListener(v->{
            if (quanty <= 9){
                quanty++;
                binding.quantyTvNewProdD.setText(String.valueOf(quanty));
            }else {
                Snackbar.make(v,"Quantity cannot be more than 10",Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.decrementBtnNewProdD.setOnClickListener(v -> {
            if (quanty > 1){
                quanty--;
                binding.quantyTvNewProdD.setText(String.valueOf(quanty));
            }else {
                Snackbar.make(v,"Quantity cannot be 0",Snackbar.LENGTH_SHORT).show();
            }
        });


        binding.newAddToCartBtn.setOnClickListener(v -> {
            if (Common.currentUser == null){
                EventBus.getDefault().postSticky(new NoAccountButWantToAddToCart(true,false));
            }else {
                CartItem cartItem = new CartItem();
                cartItem.setUid(Common.currentUser.getUid());
                cartItem.setUserPhone(Common.currentUser.getPhone());
                cartItem.setProductId(Common.selectedProduct.getId());
                cartItem.setProductName(Common.selectedProduct.getName());
                cartItem.setProductImage(Common.selectedProduct.getImage());
                cartItem.setProductPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getPrice())));
                cartItem.setProductSellingPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getSellingPrice())));
                cartItem.setProductQuantity(quanty);

                cartDataSource.getItemWithAllOptionsInCart(Common.currentUser.getUid(),
                        cartItem.getProductId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<CartItem>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @SuppressLint("LogNotTimber")
                            @Override
                            public void onSuccess(CartItem cartItemFromDb) {
                                if (cartItemFromDb.equals(cartItem)){
                                    cartItemFromDb.setProductQuantity(cartItemFromDb.getProductQuantity() + cartItem.getProductQuantity());
                                    cartDataSource.updateCartItems(cartItemFromDb)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new SingleObserver<Integer>() {
                                                @Override
                                                public void onSubscribe(Disposable d) {

                                                }

                                                @Override
                                                public void onSuccess(Integer integer) {
                                                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                                                    EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.e("CART ERROR","[Update Cart] " + e.getMessage());
                                                }
                                            });
                                }else {
                                    compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() ->{
                                                        Toast.makeText(getContext(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                                                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                    },throwable ->{
                                                        Log.e("CART ERROR","[CART ERROR]" + throwable.getMessage());

                                                    }
                                            )
                                    );
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e.getMessage().contains("empty")){
                                    compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() ->{
                                                        Toast.makeText(getContext(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                                                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                    },throwable ->{
                                                        Log.e("CART ERROR","[CART ERROR]" + throwable.getMessage());
                                                    }

                                            )
                                    );
                                }
                                Log.e("CART ERROR","[GET CART] " + e.getMessage());
                            }
                        });
            }
        });

        binding.writeAReviewTvBtn.setOnClickListener(v ->{
            if (Common.currentUser != null){
                ReviewBottomSheetDialogFragment fragmentReviewBottomSheetDialogBinding = new ReviewBottomSheetDialogFragment();
                fragmentReviewBottomSheetDialogBinding.show(getChildFragmentManager(),"ReviewBottomSheet");
            }else {
                EventBus.getDefault().postSticky(new NoAccountButWantToAddToCart(true,true));
            }

        });
    }

    private void initOtherDetails() {
        binding.quantyTvNewProdD.setText(String.valueOf(quanty));
    }

    private void initPager() {
        ProductDetailsImagesAdapter adapter = new ProductDetailsImagesAdapter(getContext(),Common.selectedProduct.getImageModelList(),false);
        binding.newProdDetailsPager.setAdapter(adapter);
    }

    private void initDescription() {
        binding.prodDetailsName.setText(Common.selectedProduct.getName());
        binding.prodDetailsPrice.setText(new StringBuilder("Rs ").append(Common.selectedProduct.getPrice()));
        binding.prodDescTvNew.setText(Common.selectedProduct.getDescription());
        binding.productDetailsTvBtn.setOnClickListener(v ->{
            binding.productDescExpLayout.setExpanded(!binding.productDescExpLayout.isExpanded());
        });
    }
}