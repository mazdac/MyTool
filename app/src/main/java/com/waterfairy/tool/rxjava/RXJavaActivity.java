package com.waterfairy.tool.rxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.rxjava.img.activity.RXJavaViewImageActivity;
import com.waterfairy.tool.rxjava.retrofit.BaseCallback;
import com.waterfairy.tool.rxjava.retrofit.RetrofitHttp;
import com.waterfairy.tool.rxjava.retrofit.Response;
import com.waterfairy.tool.rxjava.retrofit.UserBean;
import com.waterfairy.tool.rxjava.rxjava_retrofit.RXHttpManger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RXJavaActivity extends AppCompatActivity {

    private static final String TAG = "rxJava";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        initView();
        test();
        Call<Response<UserBean>> login = RetrofitHttp.getInstance().login("", "");
        login.enqueue(new BaseCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {

            }

            @Override
            public void onFailed() {

            }
        });
        login.cancel();


    }

    private void initView() {
        textView = (TextView) findViewById(R.id.text);
    }

    private void test() {
        final List<User> userList = getData();

        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                subscriber.onNext(userList);
                subscriber.onStart();
            }
        }).flatMap(new Func1<List<User>, Observable<User>>() {
            @Override
            public Observable<User> call(List<User> users) {
                return Observable.from(users);
            }
        }).filter(new Func1<User, Boolean>() {
            @Override
            public Boolean call(User user) {
                return user.getName().equals("知世2");
            }
        }).map(new Func1<User, User>() {
            @Override
            public User call(User user) {
                return user;
            }
        })
                .subscribeOn(Schedulers.io())
// .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        Log.i(TAG, "call: " + user.getAge());
                        textView.setText(user.getAge());
                    }
                });
    }

    private List<User> getData() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setName("知世" + i);
            user.setAge(i + 14 + "");
            userList.add(user);
        }
        return userList;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.net_get:
                getNetData();
                break;
            case R.id.net_post:
                break;
            case R.id.img_blur:
                startActivity(new Intent(this, RXJavaViewImageActivity.class));
                break;

        }
    }

    private void getNetData() {
//        RetrofitHttp.getInstance().update("health1","1.4");
//        RetrofitHttp.getInstance().postMultipart();
        RXHttpManger instance = RXHttpManger.getInstance();
        long id = 111;
        String name = "小樱";
        instance.getUserDataFromId(id, name);

    }

}
