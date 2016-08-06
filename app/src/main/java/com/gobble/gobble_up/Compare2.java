package com.gobble.gobble_up;

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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
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

/**
 * Created by hi on 6/16/2016.
 */
public class Compare2 extends AppCompatActivity {

    RecyclerView listview;
    ArrayList<CompareModel> list;

    GridLayoutManager lLayout;
    LinearLayoutManager layoutManager;
    ProgressBar bar;
    TextView empty;
compareAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_layout2);



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


        lLayout = new GridLayoutManager(this , 1);
        final comparebean b = (comparebean)this.getApplicationContext();

        if (b.list.size()>0)
        {
            Toast.makeText(getApplicationContext() , "Swipe up to remove item" , Toast.LENGTH_LONG).show();
        }

        refresh();








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





            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);

        itemTouchHelper.attachToRecyclerView(listview);





       // new connect(GET_PRODUCT+"11").execute();

        //compareAdapter adapter = new compareAdapter(this , list);

        //listview.setAdapter(adapter);
        //listview.setLayoutManager(lLayout);
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

        }else
        {

            Toast.makeText(getApplicationContext() , "Only one item to compare" , Toast.LENGTH_SHORT).show();
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

                bar.setVisibility(View.GONE);

                listview.setVisibility(View.VISIBLE);


            }

            @Override
            public void onFailure(Call<ArrayList<CompareModel>> call, Throwable t) {
                   //Log.d("Error",t.getMessage());
            }
        });






    }





    public class connect extends AsyncTask<Void , Void , Void>
    {

        List<String> nutrition = new ArrayList<>();



        InputStream is;
        String json;
        JSONObject object;
        String prie , desc , nae , ima;
        JSONArray mainArray , nutArray;
        String faat , pro , carb , idd;



        int length;
        String url;

        connect(String url)
        {
            this.url = url;
        }




        @Override
        protected Void doInBackground(Void... params) {


            try {
                // HttpClient client = new DefaultHttpClient();
                //  HttpGet get = new HttpGet(url);
                //  HttpResponse response = client.execute(get);
                //HttpEntity entity = response.getEntity();
                //is = entity.getContent();

                URL u = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)u.openConnection();
                if(connection.getResponseCode()==200)
                {
                    is = connection.getInputStream();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                mainArray = new JSONArray(json);
                //length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            try {
                object = mainArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                prie = object.getString("price");
                nae = object.getString("name");
                desc = object.getString("description");
                ima = object.getString("image");
                idd = object.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                nutArray = object.getJSONArray("nutration");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            length = nutArray.length();

            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = nutArray.getJSONObject(i);

                    if (obj.getString("unit").equals("per"))
                    {
                        nutrition.add(obj.getString("value") + "%");
                    }

                    else
                    {
                        nutrition.add(obj.getString("value") +  obj.getString("unit"));
                    }



                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            try {
                faat = nutArray.getJSONObject(1).getString("value");
                pro = nutArray.getJSONObject(3).getString("value");
                carb = nutArray.getJSONObject(2).getString("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);





/*
            comparelistBean bean = new comparelistBean();
            bean.setImage(ima);
            bean.setId(idd);
            bean.setPrice(prie);
            bean.setName(nae);
            bean.setCalories(nutrition.get(0));
            bean.setFat(nutrition.get(1));
            bean.setCarbs(nutrition.get(2));
            bean.setProtein(nutrition.get(3));
            bean.setSodium(nutrition.get(4));
            bean.setPotassium(nutrition.get(5));
            bean.setFiber(nutrition.get(6));
            bean.setSugar(nutrition.get(7));
            bean.setVita(nutrition.get(8));
            bean.setVitc(nutrition.get(9));
            bean.setCalcium(nutrition.get(10));
            bean.setIron(nutrition.get(11));

            list.add(bean);

            //compareAdapter adapter = new compareAdapter(getApplicationContext() , list);

            empty.setVisibility(View.GONE);
*/





        }
    }


    private class connect2 extends AsyncTask<Void , Void , Void>
    {

        List<String> nutrition = new ArrayList<>();



        InputStream is;
        String json;
        JSONObject object;
        String prie , desc , nae , ima;
        JSONArray mainArray , nutArray;
        String faat , pro , carb , idd;

        URL u = null;
        HttpURLConnection connection;


        int length;
        String url;

        connect2(String url)
        {
            this.url = url;
        }




        @Override
        protected Void doInBackground(Void... params) {


            try {
                // HttpClient client = new DefaultHttpClient();
                //  HttpGet get = new HttpGet(url);
                //  HttpResponse response = client.execute(get);
                //HttpEntity entity = response.getEntity();
                //is = entity.getContent();

                u = new URL(url);
                connection = (HttpURLConnection)u.openConnection();
                if(connection.getResponseCode()==200)
                {
                    is = connection.getInputStream();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }



            try {
                mainArray = new JSONArray(json);
                //length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            try {
                object = mainArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                prie = object.getString("price");
                nae = object.getString("name");
                desc = object.getString("description");
                ima = object.getString("image");
                idd = object.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                nutArray = object.getJSONArray("nutration");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            length = nutArray.length();

            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = nutArray.getJSONObject(i);

                    if (obj.getString("unit").equals("per"))
                    {
                        nutrition.add(obj.getString("value") + "%");
                    }

                    else
                    {
                        nutrition.add(obj.getString("value") +  obj.getString("unit"));
                    }



                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            try {
                faat = nutArray.getJSONObject(1).getString("value");
                pro = nutArray.getJSONObject(3).getString("value");
                carb = nutArray.getJSONObject(2).getString("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);






      /*      comparelistBean bean = new comparelistBean();
            bean.setImage(ima);
            bean.setId(idd);
            bean.setPrice(prie);
            bean.setName(nae);
            bean.setCalories(nutrition.get(0));
            bean.setFat(nutrition.get(1));
            bean.setCarbs(nutrition.get(2));
            bean.setProtein(nutrition.get(3));
            bean.setSodium(nutrition.get(4));
            bean.setPotassium(nutrition.get(5));
            bean.setFiber(nutrition.get(6));
            bean.setSugar(nutrition.get(7));
            bean.setVita(nutrition.get(8));
            bean.setVitc(nutrition.get(9));
            bean.setCalcium(nutrition.get(10));
            bean.setIron(nutrition.get(11));

            list.add(bean);

            bar.setVisibility(View.GONE);

            listview.setVisibility(View.VISIBLE);


            adapter.setGridData(list);
            adapter.notifyDataSetChanged();

*/




        }
    }


}
