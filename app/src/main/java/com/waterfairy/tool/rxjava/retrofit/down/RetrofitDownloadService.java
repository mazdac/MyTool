package com.waterfairy.tool.rxjava.retrofit.down;

import com.waterfairy.tool.rxjava.retrofit.Response;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by water_fairy on 2017/4/26.
 * 995637517@qq.com
 */

public interface RetrofitDownloadService {

//    http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg
//    @Streaming
//    @GET("20130116/84481_20130116142820494200_1.jpg")
//    Call<ResponseBody> downloadFile();
//当下载大文件时需要加上注解@Streaming 主要作用是下载多少字节就立马写入磁盘，而不用把整个文件读入内存
//    http://p.gdown.baidu.com/73d936ffb97eac68c6e2bd9202c70b3114eaa4b7533cb8cdb158bf40d22cf626632375d0fa1438aca6e6b47226f00b1383b0d7ee0e3b58abc746656d719deaa2ea511bca47193b0f4dd4e14dc8d79310b23349ca558e1468479d24d1768e7ffb4d4cfb5ddba1176f9592e0a477dbb13744211f18af7acf55856092cdd61872f31bfde0aef32f005d40ab78d42a3f2681417dbc84355fa6d9b42f60a68fdde18a9e4d7401fee711af
    @Streaming
    @GET("73d936ffb97eac68c6e2bd9202c70b3114eaa4b7533cb8cdb158bf40d22cf626632375d0fa1438aca6e6b47226f00b1383b0d7ee0e3b58abc746656d719deaa2ea511bca47193b0f4dd4e14dc8d79310b23349ca558e1468479d24d1768e7ffb4d4cfb5ddba1176f9592e0a477dbb13744211f18af7acf55856092cdd61872f31bfde0aef32f005d40ab78d42a3f2681417dbc84355fa6d9b42f60a68fdde18a9e4d7401fee711af")
    Call<ResponseBody> downloadFile();


}
