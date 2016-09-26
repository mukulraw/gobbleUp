package com.gobble.gobble_up;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;



interface subCatAPI {

    @GET("global/api/categories/id/{id}")
    Call<ArrayList<categoryBean>> getBooks(
            @Path("id") String id
    );

}
