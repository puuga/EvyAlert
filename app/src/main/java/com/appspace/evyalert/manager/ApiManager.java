package com.appspace.evyalert.manager;

import android.content.Context;

import com.appspace.appspacelibrary.manager.Contextor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by siwaweswongcharoen on 7/4/2016 AD.
 */
public class ApiManager {
    private static ApiManager instance;
    private Context mContext;
    private APIService service;

    public static ApiManager getInstance() {
        if (instance == null)
            instance = new ApiManager();
        return instance;
    }

    private ApiManager() {
        mContext = Contextor.getInstance().getContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://evyalert.southeastasia.cloudapp.azure.com/evyalert-service/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIService.class);
    }

    public APIService getAPIService() {
        return service;
    }
}
