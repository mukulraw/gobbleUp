package com.gobble.gobble_up;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner gender;
    EditText phone , height , weight;
    TextView age;
    TextView bmi , save;

    ProgressBar loading;

    Toast toast;

    String gend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        toast = Toast.makeText(this , null , Toast.LENGTH_SHORT);

        loading = (ProgressBar)findViewById(R.id.update_loading);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        save = (TextView)findViewById(R.id.save_profile_button);
        bmi = (TextView)findViewById(R.id.bmi);
        gender = (Spinner)findViewById(R.id.gender);

        age = (TextView)findViewById(R.id.age);
        phone = (EditText)findViewById(R.id.phone);
        height = (EditText)findViewById(R.id.height);
        weight = (EditText)findViewById(R.id.weight);

        List<String> categories = new ArrayList<String>();
        categories.add("MALE");
        categories.add("FEMALE");


        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(UpdateProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dob_dialog);
                dialog.show();
                final DatePicker dob = (DatePicker)dialog.findViewById(R.id.dob);
                TextView ok = (TextView)dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        age.setText(String.valueOf(getAge(dob.getYear() , dob.getMonth() + 1 , dob.getDayOfMonth())));
                        dialog.dismiss();

                    }
                });




            }
        });



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);

        gender.setOnItemSelectedListener(this);



        loading.setVisibility(View.VISIBLE);

        String SUB_CATEGORY = "http://nationproducts.in/";
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


                    age.setText(response.body().getAge());
                    phone.setText(response.body().getPhone());

                    height.setText(response.body().getHeight());
                    weight.setText(response.body().getWeight());

                    Float bm = Float.parseFloat(response.body().getBmi());
                    String s = String.format("%.2f", bm);
                    bmi.setText(s);

                    ImageLoader loader = ImageLoader.getInstance();

                    loading.setVisibility(View.GONE);


                }catch (Exception e)
                {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<profileBean> call, Throwable t) {

                loading.setVisibility(View.GONE);

            }
        });






        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (height.getText().length()>0)
                {
                    if (weight.getText().toString().trim().length()>0)
                    {

                        Float wei = Float.parseFloat(weight.getText().toString());
                        Float hei = Float.parseFloat(height.getText().toString());
                        Float bm = wei / ( (hei/100) * (hei/100) );

                        String s = String.format("%.2f", bm);


                        bmi.setText(s);

                    }

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

                if (weight.getText().length()>0)
                {
                    if (height.getText().toString().trim().length()>0)
                    {

                        Float wei = Float.parseFloat(weight.getText().toString());
                        Float hei = Float.parseFloat(height.getText().toString());
                        Float bm = wei / ( (hei/100) * (hei/100) );


                        String s = String.format("%.2f", bm);



                        bmi.setText(s);

                    }

                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {










                if (isValidMobile(phone.getText().toString()))
                {







                    if (Integer.parseInt(String.valueOf(weight.getText().toString())) < 201)
                    {







                        if (Integer.parseInt(String.valueOf(height.getText().toString())) < 241)
                        {
                            loading.setVisibility(View.VISIBLE);
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

                                    loading.setVisibility(View.GONE);

                                    finish();


                                }

                                @Override
                                public void onFailure(Call<updateBean> call, Throwable t) {
                                    loading.setVisibility(View.GONE);
                                }
                            });


                        }
                        else
                        {
                            //height.setError("Invalid Height");
                            toast.setText("Invalid Height");
                            toast.show();
                        }









                    }
                    else
                    {
                        //weight.setError("Invalid Weight");
                        toast.setText("Invalid Weight");
                        toast.show();
                    }









                }


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





    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            //Toast.makeText(getApplicationContext() , "Not a valid D.O.B." , Toast.LENGTH_SHORT).show();
        toast.setText("Not a valid D.O.B.");
        toast.show();
        return a;
    }

    private boolean isValidMobile(String phone2)
    {
        boolean check;
        if(phone2.length() < 6 || phone2.length() > 13)
        {
            check = false;
            //phone.setError("Not Valid Number");
            toast.setText("Not a valid number");
            toast.show();
        }
        else
        {
            check = true;
        }
        return check;
    }




}
