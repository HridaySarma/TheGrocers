package com.client.thegrocers.home.HomeFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.bumptech.glide.Glide;
import com.client.thegrocers.Adapters.BannerPagerAdapter;
import com.client.thegrocers.Adapters.BestDealsAdapter;
import com.client.thegrocers.Adapters.CategoryAdapter;
import com.client.thegrocers.Adapters.PopularCategoriesAdapter;
import com.client.thegrocers.Callbacks.ICurrentFragment;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.CreditsActivity;
import com.client.thegrocers.Model.BannerModel;
import com.client.thegrocers.Model.BestDealModel;
import com.client.thegrocers.Model.CategoryModel;
import com.client.thegrocers.Model.PopularCategoriesModel;
import com.client.thegrocers.R;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.main.zoomingrecyclerview.ZoomingRecyclerView;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment implements PermissionListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Unbinder unbinder;
    @BindView(R.id.best_deals_recycler_view)
    RecyclerView bestDealsRecyclerView;
    private HomeViewModel homeViewModel;
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.banner_pager)
    LoopingViewPager bannerPager;
    @BindView(R.id.singleBannerImageView)
    ImageView singleBannerImg;
    @BindView(R.id.popular_categories_recycler_view)
    RecyclerView popularCategoriesRv;
    private LinearLayoutManager linearLayoutManager ;
    private PopularCategoriesAdapter horizontalAdapter;
    private SnapHelper snapHelper = new LinearSnapHelper();
    @BindView(R.id.location_tv)
    TextView locationTextView;

    AlertDialog loadingDialog;

    /// Location ///
    private double latitude,longitude;
    private String address,pincode,state;
    protected  static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleApiClient googleApiClient;
    public LocationManager locationManager;
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    @BindView(R.id.view_credits_btn)
    Button view_credits_btn;
    @OnClick(R.id.view_credits_btn)
    public void onViewCreditsClicked(){
        startActivity(new Intent(getContext(), CreditsActivity.class));
    }
    /// Location ///



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Common.CurrentFragment = "Home";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Home");
        unbinder = ButterKnife.bind(this,view);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        loadingDialog.setMessage("Loading items.... Please wait");
        loadingDialog.show();
        initCategories();
        initBestDeals();
        initPopularCategories();
        initBanners();
        initSingleBanner();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        settingsRequest();
    }

    private void initPopularCategories() {
        homeViewModel.getMutableLiveDataPopularCategories().observe(getViewLifecycleOwner(), new Observer<List<PopularCategoriesModel>>() {
            @Override
            public void onChanged(List<PopularCategoriesModel> popularCategoriesModelList) {
                horizontalAdapter = new PopularCategoriesAdapter(getContext(),popularCategoriesModelList);

                linearLayoutManager = new ZoomingRecyclerView(getActivity(), (float) 0.1, (float) 0.2);
                popularCategoriesRv.setLayoutManager(linearLayoutManager);
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                popularCategoriesRv.setAdapter(horizontalAdapter);
                linearLayoutManager.setStackFromEnd(true);
                linearLayoutManager.setReverseLayout(true);
                popularCategoriesRv.smoothScrollToPosition(popularCategoriesModelList.size()/2);
                snapHelper.attachToRecyclerView(popularCategoriesRv);
                popularCategoriesRv.setNestedScrollingEnabled(false);

            }
        });
    }

    private void initSingleBanner() {
        FirebaseDatabase.getInstance().getReference("SingleBanner")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            loadingDialog.dismiss();
                            String imgUrl = snapshot.getValue(String.class);
                            Glide.with(getContext()).load(imgUrl).into(singleBannerImg);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initBanners() {
        homeViewModel.getMutableLiveDataBanners().observe(getViewLifecycleOwner(), new Observer<List<BannerModel>>() {
            @Override
            public void onChanged(List<BannerModel> bannerModelList) {
                BannerPagerAdapter bannerPagerAdapter  = new BannerPagerAdapter(getContext(),bannerModelList,false);
                bannerPager.setAdapter(bannerPagerAdapter);
            }
        });
    }

    private void initCategories() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        categoriesRecyclerView.setLayoutManager(gridLayoutManager);
        homeViewModel.getMutableLiveDataCategories().observe(getViewLifecycleOwner(), categoryModelList -> {
            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),categoryModelList);
            categoriesRecyclerView.setAdapter(categoryAdapter);
        });
    }

    private void initBestDeals() {
        homeViewModel.getMutableLiveDataBestDeals().observe(getViewLifecycleOwner(), new Observer<List<BestDealModel>>() {
            @Override
            public void onChanged(List<BestDealModel> bestDealModelList) {
                BestDealsAdapter adapter = new BestDealsAdapter(getContext(),bestDealModelList);
                LinearLayoutManager layoutManager =  new ZoomingRecyclerView(getActivity(), (float) 0.1, (float) 0.2);
                SnapHelper snapHelper2 = new LinearSnapHelper();
                bestDealsRecyclerView.setLayoutManager(layoutManager);
                bestDealsRecyclerView.setAdapter(adapter);
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                layoutManager.setStackFromEnd(false);
                layoutManager.setReverseLayout(false);
                bestDealsRecyclerView.smoothScrollToPosition(bestDealModelList.size()/2);
                snapHelper2.attachToRecyclerView(bestDealsRecyclerView);
                bestDealsRecyclerView.setNestedScrollingEnabled(false);

            }
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
            locationTextView.setText(address);
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
    @Override
    public void onDestroy() {
       super.onDestroy();
    }


    /// LOCATION ///


}