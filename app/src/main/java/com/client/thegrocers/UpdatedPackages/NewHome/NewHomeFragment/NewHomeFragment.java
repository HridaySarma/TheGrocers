package com.client.thegrocers.UpdatedPackages.NewHome.NewHomeFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.client.thegrocers.Adapters.BannerPagerAdapter;
import com.client.thegrocers.Adapters.BestDealsAdapter;
import com.client.thegrocers.Adapters.PopularCategoriesAdapter;
import com.client.thegrocers.Callbacks.ICurrentFragment;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Common.PrefsUtills;
import com.client.thegrocers.EventBus.NewSearchClicked;
import com.client.thegrocers.EventBus.ViewAllCatsClicked;
import com.client.thegrocers.Model.BannerModel;
import com.client.thegrocers.Model.BestDealModel;
import com.client.thegrocers.Model.CategoryModel;
import com.client.thegrocers.Model.CouponModel;
import com.client.thegrocers.Model.PopularCategoriesModel;
import com.client.thegrocers.NewAdapters.CouponCodesAdapter;
import com.client.thegrocers.NewAdapters.NewSmallCatAdapter;
import com.client.thegrocers.NewAdapters.NewTopProductsAdapter;
import com.client.thegrocers.R;
import com.client.thegrocers.databinding.FragmentNewHomeBinding;
import com.client.thegrocers.home.CouponCode.CouponCodeViewModel;
import com.client.thegrocers.home.HomeViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.main.zoomingrecyclerview.ZoomingRecyclerView;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.samlss.broccoli.Broccoli;
import me.samlss.broccoli.BroccoliGradientDrawable;
import me.samlss.broccoli.PlaceholderParameter;

import static android.content.Context.LOCATION_SERVICE;

public class NewHomeFragment extends Fragment implements PermissionListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private FragmentNewHomeBinding binding;
    HomeViewModel homeViewModel ;
    private CouponCodeViewModel couponCodeViewModel;
    private PopularCategoriesAdapter horizontalAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SnapHelper snapHelper = new LinearSnapHelper();
    private Broccoli broccoli;

    AlertDialog loadingDialog;

    /// Location ///
    private double latitude,longitude;
    private String address,pincode,state;
    protected  static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleApiClient googleApiClient;
    public LocationManager locationManager;
    private KProgressHUD progressHUD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewHomeBinding.inflate(getLayoutInflater());
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Home");
        homeViewModel= ViewModelProviders.of(this).get(HomeViewModel.class);
        couponCodeViewModel = ViewModelProviders.of(this).get(CouponCodeViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
       progressHUD  = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
	            .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
       progressHUD.show();
        initSearch();
        initWelcomeMessage();
        initCategories();
        initTopProducts();
        initCoupons();
        initBottomBanner();
        initBestDeals();
        initPopularCategories();
    }

    private void initSearch() {
        binding.searchProdCvNew.setOnClickListener(v ->{
            EventBus.getDefault().postSticky(new NewSearchClicked(true));
        });
    }


    private void initWelcomeMessage() {
        if (Common.currentUser != null){
            binding.homeUserName.setText(new StringBuilder("Hi, ").append(Common.currentUser.getName()));
        }else {
            binding.homeUserName.setText("Hi, ");
        }
    }

    private void initPopularCategories() {
        homeViewModel.getMutableLiveDataPopularCategories().observe(getViewLifecycleOwner(), new Observer<List<PopularCategoriesModel>>() {
            @Override
            public void onChanged(List<PopularCategoriesModel> popularCategoriesModelList) {
                horizontalAdapter = new PopularCategoriesAdapter(getContext(),popularCategoriesModelList);

                linearLayoutManager = new ZoomingRecyclerView(getActivity(), (float) 0.1, (float) 0.2);
                binding.newPopCategoriesRv.setLayoutManager(linearLayoutManager);
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                binding.newPopCategoriesRv.setAdapter(horizontalAdapter);
                linearLayoutManager.setStackFromEnd(true);
                linearLayoutManager.setReverseLayout(true);
                binding.newPopCategoriesRv.smoothScrollToPosition(popularCategoriesModelList.size()/2);
                snapHelper.attachToRecyclerView(binding.newPopCategoriesRv);
                binding.newPopCategoriesRv.setNestedScrollingEnabled(false);
                progressHUD.dismiss();

            }
        });
    }

    private void initBestDeals() {
        homeViewModel.getMutableLiveDataBestDeals().observe(getViewLifecycleOwner(), bestDealModelList -> {
            NewTopProductsAdapter adapter = new NewTopProductsAdapter(getContext(),bestDealModelList);
            LinearLayoutManager layoutManager =  new ZoomingRecyclerView(getActivity(), (float) 0.1, (float) 0.2);
            binding.newPopProductsRv.setLayoutManager(layoutManager);
            binding.newPopProductsRv.setAdapter(adapter);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        });
    }

    private void initBottomBanner() {
        homeViewModel.getMutableLiveDataBanners().observe(getViewLifecycleOwner(), new Observer<List<BannerModel>>() {
            @Override
            public void onChanged(List<BannerModel> bannerModelList) {
                BannerPagerAdapter bannerPagerAdapter  = new BannerPagerAdapter(getContext(),bannerModelList,false);
                binding.newTopBannersVp.setAdapter(bannerPagerAdapter);
            }
        });
    }

    private void initCoupons() {
        List<CouponModel> tempBgC = new ArrayList<>();
        List<CouponModel> tempSmC = new ArrayList<>();
        couponCodeViewModel.getMutableLiveDataCouponCodes().observe(getViewLifecycleOwner(), couponModelList -> {
            for (int i=0;i<couponModelList.size();i++){
                if (couponModelList.get(i).getImgSize().equals("B")){
                    tempBgC.add(couponModelList.get(i));
                }else {
                    tempSmC.add(couponModelList.get(i));
                }
            }
        binding.newSmallCouponVp.setAdapter(new CouponCodesAdapter(getContext(),tempSmC,false));
            binding.newBigCouponVp.setAdapter(new CouponCodesAdapter(getContext(),tempBgC,false));


        });
    }

    private void initTopProducts() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        binding.newPopProductsRv.setLayoutManager(linearLayoutManager);
        homeViewModel.getMutableLiveDataBestDeals().observe(getViewLifecycleOwner(), bestDealModels -> {
            NewTopProductsAdapter adapter = new NewTopProductsAdapter(getContext(),bestDealModels);
            binding.newPopProductsRv.setAdapter(adapter);
        });
    }

    private void initCategories() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        binding.newCatsSmallRv.setLayoutManager(linearLayoutManager);
        homeViewModel.getMutableLiveDataCategories().observe(getViewLifecycleOwner(), categoryModelList -> {
            NewSmallCatAdapter adapter = new NewSmallCatAdapter(getContext(),categoryModelList);
            binding.newCatsSmallRv.setAdapter(adapter);
        });

        binding.viewAllCatsNew.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new ViewAllCatsClicked(true));
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }

    /// LOCATION ///

    private void settingsRequest() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient


        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // GPS location can be null if GPS is switched off
                    if (location != null) {
                        double lat = location.getLatitude();
                        double longi = location.getLongitude();
                        latitude = lat;
                        longitude = longi;
                        com.mapbox.mapboxsdk.geometry.LatLng latLng = new com.mapbox.mapboxsdk.geometry.LatLng(latitude,longitude);
                        AddPlaceLocationToView(latLng);
                    }else {
                        Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });



//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,
//                500.0f, locationListener);
//        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (locationGPS != null) {
//                double lat = locationGPS.getLatitude();
//                double longi = locationGPS.getLongitude();
//                latitude = lat;
//                longitude = longi;
//                LatLng latLng = new LatLng(latitude,longitude);
//                realTimeLocation = latLng;
//                AddPlaceLocationToView(latLng);
//            } else {
//                Toast.makeText(getContext(), "Unable to find location.", Toast.LENGTH_SHORT).show();
//            }

    }

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double longi = location.getLongitude();
            latitude = lat;
            longitude = longi;
            com.mapbox.mapboxsdk.geometry.LatLng latLng = new LatLng(latitude,longitude);
            AddPlaceLocationToView(latLng);
        }

        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {}

        public void onStatusChanged(String provider,int status,Bundle extras){}
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsRequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }



    private void AddPlaceLocationToView(com.mapbox.mapboxsdk.geometry.LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.getLatitude(),latLng.getLongitude(),1);
            pincode = addresses.get(0).getPostalCode();
            state = addresses.get(0).getAdminArea();
            address = addresses.get(0).getAddressLine(0);
            latitude = latLng.getLatitude();
            longitude = latLng.getLongitude();
            Toast.makeText(getContext(), ""+address, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), " Failed to get location "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        com.mapbox.mapboxsdk.geometry.LatLng latLng = new com.mapbox.mapboxsdk.geometry.LatLng(latitude,longitude);
        AddPlaceLocationToView(latLng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}