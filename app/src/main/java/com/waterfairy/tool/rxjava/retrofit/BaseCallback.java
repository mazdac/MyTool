package com.waterfairy.tool.rxjava.retrofit;

import retrofit2.Call;

/**
 * Created by shui on 2017/4/9.
 */

public abstract class BaseCallback<T> implements retrofit2.Callback<Response<T>> {


    public abstract void onSuccess(T t);

    public abstract void onFailed();


    @Override
    public void onResponse(Call<Response<T>> call, retrofit2.Response<Response<T>> response) {
        handleData(call, response);
    }

    @Override
    public void onFailure(Call<Response<T>> call, Throwable throwable) {

    }


    private void handleData(Call<Response<T>> call, retrofit2.Response<Response<T>> response) {
        int code = response.code();//200;  400;401

        if (code == 200) {
            onSuccess(response.body().getData());
        } else {
            //
        }

    }
}
