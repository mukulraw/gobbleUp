package com.gobble.gobble_up;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class MainList extends AppCompatActivity {

    ListView lview;
    ArrayList<addListBean> list;
    String iidd;

    addToListAdapter adapter;
    private String GET_ALL_LIST = "http://nationproducts.in/global/api/alllists/userId/";
    private String DELETE_LIST = "http://nationproducts.in/global/api/deletelist/listId/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar11);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);






        lview = (ListView)findViewById(R.id.main_list_listview);
        final comparebean be = (comparebean)this.getApplicationContext();

        iidd = be.user_id;



        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addListBean item = (addListBean)parent.getItemAtPosition(position);
                String idd = item.getListId();

                Intent i = new Intent(getBaseContext() , SubList.class);
                i.putExtra("id" , idd);
                startActivity(i);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        list = new ArrayList<>();

        adapter = new addToListAdapter(this , R.layout.add_list_model , list);
        lview.setAdapter(adapter);


        refresh();
    }

    public void refresh()
    {

        list.clear();
        new connect(GET_ALL_LIST + iidd).execute();
        //mProgressBar.setVisibility(View.VISIBLE);
    }

    public class connect extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;




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
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }




            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    addListBean bean = new addListBean();
                    bean.setListName(obj.getString("listName"));
                    bean.setListId(obj.getString("listId"));
                    bean.setCreatedTime(obj.getString("createdTime"));
                    bean.setTotalItem(obj.getString("totalItem"));







                    list.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.setGridData(list);
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }

    public class delete extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;




        int length;
        String url;

        delete(String url)
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
                Log.d("asdasdasdurl" , url);
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
                Log.d("asdasdasddelete" , json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }








            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            refresh();

        }
    }

}
