package com.example.licenta_stroescumarius.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkClient {
    private static Retrofit retrofit;
    private static final String BASE_URL="http://10.0.2.2:5000/";

    public static Retrofit getRetrofit(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        if(retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
