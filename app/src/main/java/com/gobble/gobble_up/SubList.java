package com.gobble.gobble_up;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class SubList extends AppCompatActivity {

    ArrayList<subListBean> list;
    private String GET_LIST_ITEMS = "http://nationproducts.in/global/api/listitems/listId/";
    ListView lv;
    SubListAdapter adapter;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list);


        Bundle b = getIntent().getExtras();

        id = String.valueOf(b.get("id"));



        lv = (ListView)findViewById(R.id.sub_list);



    }


    @Override
    protected void onResume() {
        super.onResume();

        list = new ArrayList<>();

        adapter = new SubListAdapter(this , R.layout.sub_list_model , list);
        lv.setAdapter(adapter);


        refresh();

    }

    public void refresh()
    {

        list.clear();
        new connect(GET_LIST_ITEMS + id).execute();
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
                e.printStackTrace();
               // Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }




            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    subListBean bean = new subListBean();
                    bean.setName(obj.getString("name"));
                    bean.setListId(obj.getString("listId"));
                    bean.setImage(obj.getString("image"));
                    bean.setQuantity(obj.getString("quantity"));
                    bean.setPrice(obj.getString("price"));
                    bean.setProductId(obj.getString("productId"));
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

}
