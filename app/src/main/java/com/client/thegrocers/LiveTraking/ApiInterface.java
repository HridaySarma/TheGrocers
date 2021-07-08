package com.client.thegrocers.LiveTraking;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("maps/api/directions/json")
    Single<Result> getDirections(@Query("mode") String mode,
                                 @Query("transit_routing_preference") String preferance,
                                 @Query("origin") String origin,
                                 @Query("destination")String destination,
                                 @Query("key")String key);

}
