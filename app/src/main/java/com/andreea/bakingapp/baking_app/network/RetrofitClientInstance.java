package com.andreea.bakingapp.baking_app.network;

import android.support.annotation.VisibleForTesting;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    public static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static Retrofit sRetrofit;

    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            sRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    @VisibleForTesting
    public static void clearInstance() {
        sRetrofit = null;
    }
}
