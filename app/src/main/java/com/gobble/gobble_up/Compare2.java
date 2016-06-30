package com.gobble.gobble_up;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

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

/**
 * Created by hi on 6/16/2016.
 */
public class Compare2 extends AppCompatActivity {

    RecyclerView listview;
    private String GET_PRODUCT = "http://nationproducts.in/global/api/product/id/";
    List<comparelistBean> list;

    GridLayoutManager lLayout;
    LinearLayoutManager layoutManager;
    ProgressBar bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_layout2);

        listview = (RecyclerView)findViewById(R.id.compare_layout_list);


        bar = (ProgressBar)findViewById(R.id.compareProgress);


               layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        lLayout = new GridLayoutManager(this , 1);

        comparebean b = (comparebean)this.getApplicationContext();



        list = new ArrayList<>();


        bar.setVisibility(View.VISIBLE);



     /*   comparelistBean bean = new comparelistBean();

        bean.setCalories("Calories");
        bean.setFat("Total Fat");
        bean.setCarbs("Carbohydrates");
        bean.setProtein("Protein");
        bean.setSodium("Sodium");
        bean.setPotassium("Potassium");
        bean.setFiber("Fiber");
        bean.setSugar("Sugar");
        bean.setVita("Vitamin A");
        bean.setVitc("Vitamin C");
        bean.setCalcium("Calcium");
        bean.setIron("Iron");
        list.add(bean);

*/

        int length = b.list.size();

        if (length == 0)
        {
            bar.setVisibility(View.GONE);
        }

        for (int i = 0 ; i<length ; i++)
        {
            if (length == 0)
            {
                bar.setVisibility(View.GONE);
            }

            String id = String.valueOf(b.list.get(i).getId());
            Log.d("asdasdasd" , id);


            if (i<length-1)
            {
                new connect(GET_PRODUCT+id).execute();
            }
            else {
                new connect2(GET_PRODUCT+id).execute();
            }


        }

       // new connect(GET_PRODUCT+"11").execute();

        //compareAdapter adapter = new compareAdapter(this , list);

        //listview.setAdapter(adapter);
        //listview.setLayoutManager(lLayout);
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

            compareAdapter adapter = new compareAdapter(getApplicationContext() , list);






        }
    }


    public class connect2 extends AsyncTask<Void , Void , Void>
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

            bar.setVisibility(View.GONE);

            compareAdapter adapter = new compareAdapter(getApplicationContext() , list);

            listview.setAdapter(adapter);
            listview.setLayoutManager(layoutManager);




        }
    }


}
