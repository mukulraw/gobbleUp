package com.gobble.gobble_up;

import com.gobble.gobble_up.POJO.CompareModel;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;



public interface CompareAPI {

    @GET("global/api/product/id/{id}")
    Call<ArrayList<CompareModel>> getBooks(
            @Path("id") String id
    );


    @Multipart
    @POST("global/api/createlist")
    Call<CreateListPOJO> createList(@Part("userId") String userId, @Part("pno") String pno);


    @Multipart
    @POST("global/api/login")
    Call<loginBean> login(@Part("user_email") String userName, @Part("password") String password);


    @Multipart
    @POST("global/api/register")
    Call<registerBean> register(@Part("user_name") String userName, @Part("user_email") String email , @Part("password") String password , @Part("address") String address , @Part("country") String country , @Part("city") String city , @Part("state") String state);



}
