package com.gobble.gobble_up;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;



interface singleProdAPI {

    @GET("global/api/product/id/{id}")
    Call<Example> getBooks(
            @Path("id") String id
    );

}
