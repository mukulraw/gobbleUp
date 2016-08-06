package com.gobble.gobble_up;

import com.gobble.gobble_up.POJO.CompareModel;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;



public interface CompareAPI {

    @GET("global/api/product/id/{id}")
    Call<ArrayList<CompareModel>> getBooks(
            @Path("id") String id
    );

}
