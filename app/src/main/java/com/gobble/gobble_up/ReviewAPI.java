package com.gobble.gobble_up;

import com.gobble.gobble_up.POJO.Model;
import com.gobble.gobble_up.POJO.ReviewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


interface ReviewAPI {

    @GET("global/api/productreviews/id/{id}")
    Call<ArrayList<ReviewModel>> getBooks(
            @Path("id") String id
    );

}
