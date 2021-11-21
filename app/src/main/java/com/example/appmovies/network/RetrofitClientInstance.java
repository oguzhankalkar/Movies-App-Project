package com.example.appmovies.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String API_KEY_V3 = "fa3a81f8f716f3e118542d3c0873274a";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getImgUrl(){
        return IMG_URL;
    }

    public static String getApiKey() {
        return API_KEY_V3;
    }
}
