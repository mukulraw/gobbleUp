package com.gobble.gobble_up;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface subCat2API {


    @GET("global/api/products/id/{id}")
    Call<ArrayList<ProductBean>> getBooks(
            @Path("id") String id
    );


}
