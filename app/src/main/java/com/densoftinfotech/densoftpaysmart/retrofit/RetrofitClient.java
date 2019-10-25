package com.densoftinfotech.densoftpaysmart.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String url = "";

    public static Retrofit getRetrofit(){
        if(retrofit == null){
           retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
