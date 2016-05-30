package com.gobble.gobble_up;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
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
import java.util.ArrayList;

public class Products extends AppCompatActivity {


    TextView title;
    ListView grid;
    ArrayList<ProductBean> list1;
    private ProdAdapter adapter;
    private String PROD_BY_CAT = "http://nationproducts.in/global/api/products/id/";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar3);



        title = (TextView)findViewById(R.id.titleprod);
        grid = (ListView) findViewById(R.id.grid_prod);


        Bundle b = getIntent().getExtras();

        String a = String.valueOf(b.get("id"));

        title.setText((CharSequence) b.get("name"));


        list1 = new ArrayList<>();
        adapter = new ProdAdapter(this , R.layout.prod_list_model , list1);
        grid.setAdapter(adapter);

        refresh(a);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProductBean item = (ProductBean) parent.getItemAtPosition(position);

                Intent i = new Intent(Products.this , SingleProduct.class);
                i.putExtra("id" , item.getId());
                i.putExtra("name" , item.getName());
                i.putExtra("price" , item.getPrice());
                i.putExtra("desc" , item.getDescription());
                i.putExtra("image" , item.getImage());
                startActivity(i);


            }
        });


    }


    public void refresh(String cat)
    {
        list1.clear();
        String url = PROD_BY_CAT + cat;
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
               // Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }




            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    ProductBean bean = new ProductBean();
                    bean.setId(obj.getInt("id"));
                    bean.setName(obj.getString("name"));
                    bean.setPrice(obj.getString("price"));
                    bean.setDescription(obj.getString("description"));
                    String image = obj.getString("image");
                    image = image.replaceAll(" " , "%20");
                    bean.setImage(image);
                    list1.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                   // Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
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
