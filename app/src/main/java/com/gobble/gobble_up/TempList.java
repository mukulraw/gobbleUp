package com.gobble.gobble_up;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class TempList extends AppCompatActivity {

    RecyclerView listview;
    private String GET_PRODUCT = "http://nationproducts.in/global/api/product/id/";
    List<comparelistBean> list;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_list);
        listview = (RecyclerView)findViewById(R.id.temp_list);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        //lLayout = new GridLayoutManager(this , 1);

        comparebean b = (comparebean)this.getApplicationContext();



        list = new ArrayList<>();

        int length = b.tempList.size();
        for (int i = 0 ; i<length ; i++)
        {
            String id = String.valueOf(b.tempList.get(i).getId());
            Log.d("asdasdasd" , id);



            new connect(GET_PRODUCT+id).execute();
        }

    }
    public class connect extends AsyncTask<Void , Void , Void>
    {

        List<String> nutrition = new ArrayList<>();



        InputStream is;
        String json;
        JSONObject object;
        String prie , desc , nae , ima;
        JSONArray mainArray , nutArray;
        String faat , pro , carb;



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

            tempAdapter adapter = new tempAdapter(getApplicationContext() , list);

            listview.setAdapter(adapter);
            listview.setLayoutManager(layoutManager);




        }
    }


}