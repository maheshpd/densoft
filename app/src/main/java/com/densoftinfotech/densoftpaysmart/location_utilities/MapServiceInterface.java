package com.densoftinfotech.densoftpaysmart.location_utilities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MapServiceInterface {

    @GET
    Call<String> getAddress(@Url String url);

    @GET
    Call<String> getLocationfromAddress(@Url String url);

    @GET("maps/api/directions/json")
    Call<String> getDirections(@Query("origin") String latlng, @Query("destination") String destination);

    //https://maps.googleapis.com/maps/api/geocode/json?latlng=19.0175681,72.8302762&key=AIzaSyBSUEeOWYgIGTE3REw9EritqxduqUsB5ZA
}
