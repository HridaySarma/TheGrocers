package com.client.thegrocers.LiveTraking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.client.thegrocers.Model.OngoingOrdersModel;
import com.client.thegrocers.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveTrackingActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private DatabaseReference ref;
    private GeoFire geoFire;
    private Location myLocation;

    private Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;
    private LatLng mOrigin;
    private LatLng mDestination;


    private ApiInterface apiInterface;
    private List<LatLng> polyLineList;
    private PolylineOptions polylineOptions;


    private OngoingOrdersModel orderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(null);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map_live_traking);
//        orderData = (OngoingOrdersModel) getIntent().getSerializableExtra("OrderData");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driverLocations");
        geoFire = new GeoFire(ref);

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("https://maps.googleapis.com/").build();
        apiInterface = retrofit.create(ApiInterface.class);
//        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.darkmap);
        googleMap.setMapStyle(mapStyleOptions);
        geoFire.getLocation("currentLocation", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    mOrigin = new LatLng(location.latitude,location.longitude );
                    if (orderData != null){
                        mDestination = new LatLng(orderData.getAddress().getLatitude(),orderData.getAddress().getLongitude());
                        getDirection(mOrigin.latitude +","+ mOrigin.longitude, mDestination.latitude +","+ mDestination.longitude);

                    }else {
                        Toast.makeText(LiveTrackingActivity.this, "Order Data is null", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(LiveTrackingActivity.this, "mOr"+mOrigin.latitude, Toast.LENGTH_SHORT).show();
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    Toast.makeText(LiveTrackingActivity.this, "Driver Location not found", Toast.LENGTH_SHORT).show();
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });



    }

    private void getDirection(String origin,String destination){
        apiInterface.getDirections("driving","less_driving",origin,destination,
                getString(R.string.google_maps_key)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull Result result) {
                        polyLineList = new ArrayList<>();
                        List<Route> routeList = result.getRoutes();
                        for (Route route :routeList){
                            String polyline =  route.getOverviewPolyline().getPoints();
                            polyLineList.addAll(decodePoly(polyline));
                        }
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        polylineOptions.width(15);
                        polylineOptions.addAll(polyLineList);
                        polylineOptions.startCap(new SquareCap());
                        polylineOptions.endCap(new SquareCap());
                        polylineOptions.jointType(JointType.BEVEL);
                        mMap.addPolyline(polylineOptions);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(mOrigin);
                        builder.include(mDestination);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),5));
//                        mMap.addMarker(new MarkerOptions().position(mOrigin).anchor(0.5f, 0.5f).icon(BitmapFromVector(getApplicationContext(), R.drawable.delivery_bike)));
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d("DIREC ERR",e.getMessage());
                    }
                });
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}