package com.densoftinfotech.densoftpaysmart.retrofit;

import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static Gson gson;

    public static Retrofit getRetrofit(){
        if(retrofit == null){
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(180, TimeUnit.SECONDS)
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .build();
            gson = new GsonBuilder().serializeNulls().create();
           retrofit = new Retrofit.Builder().baseUrl(URLS.common_url_webroute()).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();

        }
        return retrofit;
    }
}
