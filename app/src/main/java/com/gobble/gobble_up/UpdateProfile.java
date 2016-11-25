package com.gobble.gobble_up;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner gender;
    EditText age , phone , height , weight;
    TextView bmi , save;

    String gend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        save = (TextView)findViewById(R.id.save_profile_button);
        bmi = (TextView)findViewById(R.id.bmi);
        gender = (Spinner)findViewById(R.id.gender);

        age = (EditText)findViewById(R.id.age);
        phone = (EditText)findViewById(R.id.phone);
        height = (EditText)findViewById(R.id.height);
        weight = (EditText)findViewById(R.id.weight);

        List<String> categories = new ArrayList<String>();
        categories.add("MALE");
        categories.add("FEMALE");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);

        gender.setOnItemSelectedListener(this);



        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                if (weight.getText().toString().trim().length()>0)
                {

                    Float wei = Float.parseFloat(weight.getText().toString());
                    Float hei = Float.parseFloat(height.getText().toString());
                    Float bm = wei / ( (hei/100) * (hei/100) );

                    String s = String.format("%.2f", bm);


                    bmi.setText(s);

                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (height.getText().toString().trim().length()>0)
                {

                    Float wei = Float.parseFloat(weight.getText().toString());
                    Float hei = Float.parseFloat(height.getText().toString());
                    Float bm = wei / ( (hei/100) * (hei/100) );


                    String s = String.format("%.2f", bm);



                    bmi.setText(s);

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String SUB_CATEGORY = "http://nationproducts.in/";
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SUB_CATEGORY)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final CompareAPI request = retrofit.create(CompareAPI.class);


                comparebean b = (comparebean)getApplicationContext();

                Call<updateBean> call = request.updateDetails(b.user_id , age.getText().toString() , phone.getText().toString() , gend, height.getText().toString() , weight.getText().toString() , bmi.getText().toString());

                call.enqueue(new Callback<updateBean>() {
                    @Override
                    public void onResponse(Call<updateBean> call, Response<updateBean> response) {


                        Toast.makeText(getApplicationContext() , response.body().getMsg() , Toast.LENGTH_SHORT).show();
                        finish();


                    }

                    @Override
                    public void onFailure(Call<updateBean> call, Throwable t) {

                    }
                });


            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        gend = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }







}
