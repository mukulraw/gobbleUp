package com.gobble.gobble_up;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gobble.gobble_up.POJO.CompareModel;

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

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TempList extends AppCompatActivity {

    RecyclerView listview;
    private String GET_PRODUCT = "http://nationproducts.in/global/api/product/id/";
    ArrayList<CompareModel> list;
    LinearLayoutManager layoutManager;
    TextView saveList;
    ProgressBar progressBar;
    tempAdapter adapter;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_list);

        listview = (RecyclerView)findViewById(R.id.temp_list);


        list = new ArrayList<>();

        empty = (TextView)findViewById(R.id.tempEmptyMessage);
        saveList = (TextView)findViewById(R.id.saveList);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        progressBar = (ProgressBar)findViewById(R.id.baarr);

        //lLayout = new GridLayoutManager(this , 1);

        final comparebean b = (comparebean)this.getApplicationContext();

        if (b.tempList.size()>0)
        {
            Toast.makeText(this , "Swipe to remove item" , Toast.LENGTH_LONG).show();
        }


        adapter = new tempAdapter(getApplicationContext() , list);

        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);


        SlideInRightAnimator animator1 = new SlideInRightAnimator();
        animator1.setRemoveDuration(500);


        listview.setItemAnimator(animator1);

        listview.setAdapter(adapter);
        listview.setLayoutManager(layoutManager);


        refresh();

        listview.setVisibility(View.GONE);
        saveList.setVisibility(View.GONE);






        saveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(getBaseContext() , AddtoList.class);
                startActivityForResult(i , 1111);


            }
        });




        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;

               // Drawable d = ContextCompat.getDrawable(getBaseContext() , R.drawable.black_shade);
                //d.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                //d.draw(c);



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





        //ItemTouchHelper.Callback callback =
        //        new SimpleItemTouchHelperCallback(adapter);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //touchHelper.attachToRecyclerView(listview);










    }


    private void refresh()
    {


        final comparebean b = (comparebean)this.getApplicationContext();


        list.clear();
        progressBar.setVisibility(View.VISIBLE);
        int length = b.tempList.size();
        if (length == 0)
        {
            progressBar.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            saveList.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);
        }
        for (int i = 0 ; i<length ; i++)
        {
            String id = String.valueOf(b.tempList.get(i).getId());
            Log.d("asdasdasd" , id);


            fetch(id);



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        comparebean b = (comparebean)this.getApplicationContext();

        if (requestCode == 1111)
        {
            if (resultCode == RESULT_OK)
            {
                Toast.makeText(getBaseContext() , "Added to list" , Toast.LENGTH_SHORT).show();
                b.tempList.clear();
                b.tempListCount = 0;
                list.clear();

                adapter.setGridData(list);
                adapter.notifyDataSetChanged();

                checkList();




            }
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

                Log.d("asdasdasd" , response.body().toString());


                list.add(response.body().get(0));
                adapter.notifyItemInserted(list.size()-1);


                listview.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);



                saveList.setVisibility(View.VISIBLE);
                listview.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ArrayList<CompareModel>> call, Throwable t) {

            }
        });






    }


    private void checkList()
    {
        if (list.size() < 1)
        {
            finish();
        }
    }




}
