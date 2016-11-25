package com.gobble.gobble_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyProfile extends AppCompatActivity {

    TextView edit , name , age , phone , gender , height , weight , bmi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        edit = (TextView)findViewById(R.id.edit_profile_button);

        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        phone = (TextView)findViewById(R.id.phone);
        gender = (TextView)findViewById(R.id.gender);
        height = (TextView)findViewById(R.id.height);
        weight = (TextView)findViewById(R.id.weight);
        bmi = (TextView)findViewById(R.id.bmi);

        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CompareAPI request = retrofit.create(CompareAPI.class);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext() , UpdateProfile.class);
                startActivityForResult(i , 121);


            }
        });

        comparebean b = (comparebean)getApplicationContext();

        Call<profileBean> call = request.getProfile(b.user_id);

        call.enqueue(new Callback<profileBean>() {
            @Override
            public void onResponse(Call<profileBean> call, Response<profileBean> response) {



                try {

                    name.setText(response.body().getUserName());
                    age.setText(response.body().getAge());
                    phone.setText(response.body().getPhone());
                    gender.setText(response.body().getGender());
                    height.setText(response.body().getHeight() + "cm");
                    weight.setText(response.body().getWeight() + "kg");
                    bmi.setText(response.body().getBmi());


                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<profileBean> call, Throwable t) {

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 121)
        {String SUB_CATEGORY = "http://nationproducts.in/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SUB_CATEGORY)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final CompareAPI request = retrofit.create(CompareAPI.class);
            comparebean b = (comparebean)getApplicationContext();

            Call<profileBean> call = request.getProfile(b.user_id);

            call.enqueue(new Callback<profileBean>() {
                @Override
                public void onResponse(Call<profileBean> call, Response<profileBean> response) {



                    try {

                        name.setText(response.body().getUserName());
                        age.setText(response.body().getAge());
                        phone.setText(response.body().getPhone());
                        gender.setText(response.body().getGender());
                        height.setText(response.body().getHeight() + "cm");
                        weight.setText(response.body().getWeight() + "kg");
                        bmi.setText(response.body().getBmi());


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<profileBean> call, Throwable t) {

                }
            });


        }

    }
}
