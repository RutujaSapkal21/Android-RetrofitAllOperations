package com.example.retrofitdemo2;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

public interface APIInterface {

    @Multipart
    @POST("InsertRetroo.php")
    Call<POJO> addProfile(@PartMap Map<String, RequestBody> param,
                          @Part MultipartBody.Part file);

    @POST("retroviewww.php")
    Call<POJOVIEW> viewAllusers();

    @Multipart
    @POST("retroupdate.php")
    Call<POJOVIEW> updateuser(@PartMap Map<String, RequestBody> param,
                             @Part MultipartBody.Part file);


    @POST("retrosingle.php")
    Call<POJOVIEW> getSingleUser(@QueryMap Map<String,String> param);

    @POST("Deleteretro.php")
    Call<POJO> userDelete(@QueryMap Map<String,String> map);
}
