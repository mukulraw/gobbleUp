package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;

public class SubCategory extends AppCompatActivity {

    TextView title;
    GridView grid;
    private String SUB_CATEGORY = "http://nationproducts.in/global/api/categories/id/";
    private CatGridAdapter adapter;
    ArrayList<categoryBean> list1;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        title = (TextView)findViewById(R.id.sub_cat_title);
        grid = (GridView)findViewById(R.id.sub_cat_grid);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar2);


        Bundle b = getIntent().getExtras();

        String a = String.valueOf(b.get("id"));

        title.setText((CharSequence) b.get("name"));


        list1 = new ArrayList<>();
        adapter = new CatGridAdapter(this , R.layout.category_model , list1);
        grid.setAdapter(adapter);

       refresh(a);


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryBean item = (categoryBean) parent.getItemAtPosition(position);

                Intent i = new Intent(SubCategory.this , Products.class);
                i.putExtra("id" , item.getId());
                i.putExtra("name" , item.getName());
                startActivity(i);
            }
        });


    }

    public void refresh(String cat)
    {
        list1.clear();
        String url = SUB_CATEGORY + cat;
        new connect(url).execute();
        mProgressBar.setVisibility(View.VISIBLE);
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


            Log.d("asdasdasd" , url);

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
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
                    categoryBean bean = new categoryBean();
                    bean.setId(obj.getInt("id"));
                    bean.setName(obj.getString("name"));

                    String image = obj.getString("image");
                    image = image.replaceAll(" " , "%20");
                    bean.setImage(image);
                    list1.add(bean);
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

            adapter.setGridData(list1);
            //list.clear();
            mProgressBar.setVisibility(View.GONE);

        }
    }


}
