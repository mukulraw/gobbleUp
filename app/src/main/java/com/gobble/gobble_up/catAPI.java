package com.gobble.gobble_up;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;



interface catAPI {
    @GET("global/api/categories")
    Call<ArrayList<categoryBean>> getBooks();


    @GET("api/v2.0/android/apps/lookup.json?p=com.gobble.gobble_up&access_token=87092fd90e71bbebf14de7f5aac59618158e2c7e")
    Call<updateNotificationBean> getUpdateNotofied();


}
