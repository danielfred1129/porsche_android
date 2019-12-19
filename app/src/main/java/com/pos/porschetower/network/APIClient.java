package com.pos.porschetower.network;

import com.pos.porschetower.utils.Utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    private static API REST_CLIENT;
    private static String baseUrl = "http://pdtowerapp.com";


    static {
        getClient();
    }

    private APIClient() {
    }

    public static API get() {
        return REST_CLIENT;
    }

    static void getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl )
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        REST_CLIENT = retrofit.create(API.class);
    }

}