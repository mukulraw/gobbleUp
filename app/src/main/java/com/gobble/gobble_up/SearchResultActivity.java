package com.gobble.gobble_up;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

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

public class SearchResultActivity extends AppCompatActivity {

    private String GET_ALL = "http://nationproducts.in/global/api/allproduct";

    ArrayList<searchBean> original;
    ArrayList<searchBean> result;
    searchAdapter adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        handleIntent(getIntent());

        new connect(GET_ALL).execute();

        lv = (ListView)findViewById(R.id.search_list);

        original = new ArrayList<>();
        result = new ArrayList<>();

        adapter = new searchAdapter(this , R.layout.search_model , result);

        lv.setAdapter(adapter);

        Log.d("asdasdasd" , "entered search");


    }



    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search

            int textLength = query.length();
            result.clear();

            for(int i=0;i<original.size();i++)
            {
                String playerName=original.get(i).getName().toString();
                if(textLength <= playerName.length()){
                    //compare the String in EditText with Names in the ArrayList
                    if(query.equalsIgnoreCase(playerName.substring(0,textLength)))
                        result.add(original.get(i));
                }
            }
            adapter.setGridData(result);



        }
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
                    searchBean bean = new searchBean();
                    bean.setName(obj.getString("name"));
                    bean.setPrice(obj.getString("price"));
                    bean.setImage(obj.getString("image"));
                    bean.setCatId(obj.getString("cat_id"));
                    bean.setId(obj.getString("id"));
                    bean.setNutrition(obj.getString("nutration"));
                    bean.setSubCatId(obj.getString("sub_cat_id"));







                    original.add(bean);
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

            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }


}
