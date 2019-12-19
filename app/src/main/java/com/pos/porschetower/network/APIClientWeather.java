package com.pos.porschetower.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ashwani kumar on 19-12-2019.
 */
public class APIClientWeather {
    private static Retrofit retrofit = null;
    private static API REST_CLIENT;
    private static String baseUrl = "http://api.wunderground.com";


    static {
        getClient();
    }

    private APIClientWeather() {
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
