package com.gobble.gobble_up;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;



interface catAPI {
    @GET("global/api/categories")
    Call<ArrayList<categoryBean>> getBooks();


}
