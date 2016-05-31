package com.gobble.gobble_up;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

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


public class SubCatFragment extends Fragment {

    private String PROD_BY_CAT = "http://nationproducts.in/global/api/products/id/";
    ArrayList<ProductBean> list1;
    private ProdAdapter adapter;

    int page;
    String id;
    String name;

    public static SubCatFragment newInstance(int page , String id) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("id" , id);
        SubCatFragment fragment = new SubCatFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.page = getArguments().getInt("page");
        this.id = getArguments().getString("id");
        this.name = getArguments().getString("name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_model, container, false);


        ListView grid = (ListView) view.findViewById(R.id.sub_cat_grid);
        list1 = new ArrayList<>();
        adapter = new ProdAdapter(getActivity() , R.layout.prod_list_model , list1);
        grid.setAdapter(adapter);



        refresh(getArguments().getString("id"));


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProductBean item = (ProductBean) parent.getItemAtPosition(position);

                Intent i = new Intent(getContext() , SingleProduct.class);
                i.putExtra("id" , item.getId());
                i.putExtra("name" , item.getName());
                i.putExtra("price" , item.getPrice());
                i.putExtra("desc" , item.getDescription());
                i.putExtra("image" , item.getImage());
                startActivity(i);


            }
        });


        return view;
    }

    public void refresh(String cat)
    {
        list1.clear();
        String url = PROD_BY_CAT + cat;
        new connect(url).execute();
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


            Log.d("Sub category fragment" , url);

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
            //mProgressBar.setVisibility(View.GONE);

        }
    }


}
