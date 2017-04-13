package com.waterfairy.tool.rxjava.retrofit;

import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.BaseResponseObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.waterfairy.tool.rxjava.HttpDef.BASE_URL;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class Http {

    private Retrofit retrofit;
    private  static  RetrofitService retrofitService;

    private Http() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public static RetrofitService getInstance() {
        return retrofitService;
    }

}
