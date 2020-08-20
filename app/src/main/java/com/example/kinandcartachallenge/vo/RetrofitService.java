package com.example.kinandcartachallenge.vo;

import com.example.kinandcartachallenge.domain.service.WebService;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.kinandcartachallenge.utils.ApiConstants.BASE_URL;

public class RetrofitService {

    public static WebService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        return retrofit.create(WebService.class);
    }
}
