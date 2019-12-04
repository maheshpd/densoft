package com.densoftinfotech.densoftpaysmart.location_utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapRetrofitClient {
    private static Retrofit retrofit;
    private static Gson gson;

    public static Retrofit getRetrofit(){
        if(retrofit == null){
            gson = new GsonBuilder().serializeNulls().create();
           retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/").addConverterFactory(GsonConverterFactory.create(gson)).build();

        }
        return retrofit;
    }
}
