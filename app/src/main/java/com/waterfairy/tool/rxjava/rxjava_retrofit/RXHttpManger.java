package com.waterfairy.tool.rxjava.rxjava_retrofit;

import android.util.Log;

import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.UserBean;
import com.waterfairy.tool.rxjava.rxjava_retrofit.exception.ApiException;
import com.waterfairy.tool.rxjava.rxjava_retrofit.func.HttpResponseFunc;
import com.waterfairy.tool.rxjava.rxjava_retrofit.manger.PartManger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.waterfairy.tool.rxjava.HttpDef.MY_BASE_URL;

/**
 * Created by water_fairy on 2017/3/2.
 */

public class RXHttpManger {

    private static final String TAG = "RXHttpManger";
    private Retrofit retrofit;//retrofit
    private RxJava_RetrofitService retrofitService;//网络请求
    private static final RXHttpManger RX_HTTP_MANGER = new RXHttpManger();

    public static RXHttpManger getInstance() {
        return RX_HTTP_MANGER;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private RXHttpManger() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MY_BASE_URL)
                .build();
        retrofitService = retrofit.create(RxJava_RetrofitService.class);
    }

    public void getUserDataFromId(long id, String name) {
        retrofitService
                .getUserData(id,name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .onErrorResumeNext(new HttpResponseFunc<UserBean>())
                .doOnNext(new Action1<UserBean>() {
                    @Override
                    public void call(UserBean userBean) {

                    }
                })
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.i(TAG, "onError: " + apiException.code + "--" + apiException.message);
                        } else {
                            Log.i(TAG, "onError: " + e.toString());
                        }
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        Log.i(TAG, "onNext: " + userBean.toString());
                    }
                })
        ;
    }


}
