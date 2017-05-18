package com.waterfairy.tool.rxjava.retrofit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by water_fairy on 2017/5/18.
 * 995637517@qq.com
 */

public class RequestInterceptor implements Interceptor {
    HashMap<String, String> hashMap;

    public RequestInterceptor(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody body = request.body();
        if (hashMap != null && hashMap.size() > 0) {
            if (body instanceof FormBody) {
                //单类型
                body = addParamsToFormBody((FormBody) body);
            } else if (body instanceof MultipartBody) {
                //多类型

            }
        }

        return chain.proceed(request);
    }

    private FormBody addParamsToFormBody(FormBody formBody) {
        StringBuffer stringBuffer = new StringBuffer();
        FormBody.Builder builder = new FormBody.Builder();

        Set<String> strings = hashMap.keySet();
        for (String key : strings) {
            String value = hashMap.get(key);
            builder.add(key, value);
        }
        for (int i = 0; i < formBody.size(); i++) {
            builder.addEncoded(formBody.encodedName(i),
                    formBody.encodedValue(i));
        }
        return builder.build();

    }
}
