package com.gobble.gobble_up;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gobble.gobble_up.POJO.CompareModel;
import com.gobble.gobble_up.POJO.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Compare2 extends AppCompatActivity {

    RecyclerView listview;
    ArrayList<CompareModel> list;

    LinearLayout listHeader;

    GridLayoutManager lLayout;
    LinearLayoutManager layoutManager;
    ProgressBar bar;
    TextView empty;
    LinearLayout tutorial;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    compareAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_layout2);

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

        //listHeader = (LinearLayout)findViewById(R.id.header_list);

        preferences = getSharedPreferences("prefer" , MODE_PRIVATE);



        tutorial = (LinearLayout)findViewById(R.id.tutorial);

        if (preferences.getBoolean("toast2" , false))
        {
            tutorial.setVisibility(View.GONE);
        }

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor = preferences.edit();
                editor.putBoolean("toast2" , true);
                editor.apply();
                tutorial.setVisibility(View.GONE);

            }
        });





        listview = (RecyclerView)findViewById(R.id.compare_layout_list);

        RecyclerView.ItemAnimator animator = listview.getItemAnimator();


        listview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        empty = (TextView)findViewById(R.id.emptymessage);

        bar = (ProgressBar)findViewById(R.id.compareProgress);


               layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        list = new ArrayList<>();
        adapter = new compareAdapter(getApplicationContext() , list);
        listview.setAdapter(adapter);
        listview.setLayoutManager(layoutManager);


        RecyclerView.ItemDecoration itemDecoration = new
                SimpleDividerItemDecoration(this, SimpleDividerItemDecoration.HORIZONTAL_LIST);


        listview.addItemDecoration(itemDecoration);


        lLayout = new GridLayoutManager(this , 1);
        final comparebean b = (comparebean)this.getApplicationContext();

       /* LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.compare_model , null);

        TextView price = (TextView)view.findViewById(R.id.compare_model_price);
        TextView name = (TextView)view.findViewById(R.id.compare_model_name);
        TextView calories = (TextView)view.findViewById(R.id.compare_calories);
        TextView fat = (TextView)view.findViewById(R.id.compare_fat);
        TextView carbs = (TextView)view.findViewById(R.id.compare_carbs);
        TextView protein = (TextView)view.findViewById(R.id.compare_protein);
        TextView sodium = (TextView)view.findViewById(R.id.compare_sodium);
        TextView potassium = (TextView)view.findViewById(R.id.compare_potassium);
        TextView fiber = (TextView)view.findViewById(R.id.compare_fiber);
        TextView sugar = (TextView)view.findViewById(R.id.compare_sugar);
        TextView vita = (TextView)view.findViewById(R.id.compare_vita);
        TextView vitc = (TextView)view.findViewById(R.id.compare_vitc);
        TextView calcium = (TextView)view.findViewById(R.id.compare_calcium);
        TextView iron = (TextView)view.findViewById(R.id.compare_iron);
        TextView add = (TextView)view.findViewById(R.id.compare_model_add);

        price.setText("Price");
        name.setText("Name");
        calories.setText("Calories");
        fat.setText("Fat");
        carbs.setText("Carbohydrates");
        protein.setText("Protein");
        sodium.setText("Sodium");
        potassium.setText("Potassium");
        fiber.setText("Fiber");
        sugar.setText("Sugar");
        vita.setText("Vitamin A");
        vitc.setText("Vitamin C");
        calcium.setText("Calcium");
        iron.setText("Iron");
        add.setVisibility(View.GONE);

        listHeader.addView(view);
*/


        if (b.list.size() > 1)
        {
            Toast.makeText(this , "Swipe Up to Remove Items" , Toast.LENGTH_LONG).show();



        }



        //refresh();








        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;

                Drawable d = ContextCompat.getDrawable(getBaseContext() , R.drawable.black_shade);
                d.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                d.draw(c);



            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {



                //b.list.remove(viewHolder.getAdapterPosition());
                //adapter.notifyDataSetChanged();
                //adapter.notifyItemRemoved(viewHolder.getAdapterPosition());


                adapter.onItemDismiss(viewHolder.getAdapterPosition());

                checkList();




            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);

        itemTouchHelper.attachToRecyclerView(listview);





       // new connect(GET_PRODUCT+"11").execute();

        //compareAdapter adapter = new compareAdapter(this , list);

        //listview.setAdapter(adapter);
        //listview.setLayoutManager(lLayout);
    }


    @Override
    protected void onResume() {
        super.onResume();



        list.clear();

        refresh();

    }

    private void checkList()
    {
        if (list.size() < 1)
        {
            finish();
        }
    }


    private void refresh()
    {

        comparebean b = (comparebean)this.getApplicationContext();



        listview.setVisibility(View.GONE);

        bar.setVisibility(View.VISIBLE);

        int length = b.list.size();

        if (length == 0)
        {
            bar.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);

        }



        if (length>1)
        {
            for (int i = 0 ; i<length ; i++)
        {
            String id = String.valueOf(b.list.get(i).getId());



            String GET_PRODUCT = "http://nationproducts.in/global/api/product/id/";

            fetch(id);


        }

        }else if (length ==1)
        {

            Toast.makeText(getApplicationContext() , "Only one item to compare" , Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (length == 0)
        {
            Toast.makeText(getApplicationContext() , "No item to compare" , Toast.LENGTH_SHORT).show();
            finish();
        }



    }




    public void fetch(String iid)
    {
        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CompareAPI request = retrofit.create(CompareAPI.class);
        Call<ArrayList<CompareModel>> call = request.getBooks(iid);

        call.enqueue(new Callback<ArrayList<CompareModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CompareModel>> call, Response<ArrayList<CompareModel>> response) {

               // Log.d("asdasdasd" , response.body().toString());


                list.add(response.body().get(0));
                adapter.notifyItemInserted(list.size()-1);
                adapter.notifyDataSetChanged();

                bar.setVisibility(View.GONE);

                listview.setVisibility(View.VISIBLE);


            }

            @Override
            public void onFailure(Call<ArrayList<CompareModel>> call, Throwable t) {
                   //Log.d("Error",t.getMessage());
            }
        });






    }











}
