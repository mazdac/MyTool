package com.waterfairy.tool.alipay;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.waterfairy.tool.R;

import java.util.Map;

public class AliPayActivity extends AppCompatActivity {

//
//    private static final int SDK_PAY_FLAG = 1;
//
//    @SuppressLint("HandlerLeak")
//    private Handler mHandler = new Handler() {
//        @SuppressWarnings("unused")
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case SDK_PAY_FLAG: {
//////                    @SuppressWarnings("unchecked")
//////                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//////                    /**
//////                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
//////                     */
//////                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//////                    String resultStatus = payResult.getResultStatus();
////                    // 判断resultStatus 为9000则代表支付成功
////                    if (TextUtils.equals(resultStatus, "9000")) {
////                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
////                        Toast.makeText(AliPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
////                    } else {
////                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
////                        Toast.makeText(AliPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
////                    }
////                    break;
////                }
////                default:
////                    break;
//            }
//        };
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ali_pay);
//    }
//
//    public void onClick(View view){
//
//
//
//    }
//
//    public void payV2(final String orderInfo){
//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(AliPayActivity.this);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//                Log.i("msp", result.toString());
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }

}
