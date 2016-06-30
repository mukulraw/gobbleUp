package com.gobble.gobble_up;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SubCatFragment extends Fragment {

    private String PROD_BY_CAT = "http://nationproducts.in/global/api/products/id/";
    ArrayList<ProductBean> list1;
    private ProdAdapter2 adapter;
    private GridLayoutManager lLayout;
    RecyclerView grid;
    int page;
    String id;
    String name;
    //RelativeLayout bar;

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
        lLayout = new GridLayoutManager(view.getContext() , 1);


        comparebean b = (comparebean)getActivity().getApplicationContext();

        //bar = (RelativeLayout)getActivity().findViewById(R.id.bottombar);

        if (b.comparecount>0)
        {
            //bar.setVisibility(View.VISIBLE);
        }
        else
        {
            //bar.setVisibility(View.INVISIBLE);
        }

        grid = (RecyclerView)view.findViewById(R.id.sub_cat_grid);

        grid.setHasFixedSize(true);
        grid.setLayoutManager(lLayout);





       // refresh(getArguments().getString("id"));
/*

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

*/

        return view;
    }

    private Bitmap LoadImageFromURL(String url)

    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            return d;
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public class loadBitmap extends AsyncTask<Void , Void , Void>
    {
        comparebean b = (comparebean)getActivity().getApplicationContext();
        String url;
        ImageView iv;
        Bitmap d;


        public loadBitmap(String url)
        {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {


           // Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            b.bitmaps.add(d);

        }
    }


    public void refresh(String cat)
    { list1 = new ArrayList<>();
        list1.clear();
        String url = PROD_BY_CAT + cat;
        new connect(url).execute();
        //mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        refresh(getArguments().getString("id"));


      /*  TextView t1 = (TextView)((MainActivity)getContext()).findViewById(R.id.textView5);
        TextView t2 = (TextView)((MainActivity)getContext()).findViewById(R.id.textView4);

        View bar = (View)((MainActivity)getContext()).findViewById(R.id.bottombar);

        BottomSheetBehavior bott = BottomSheetBehavior.from(bar);

        if (t1.getText().toString().equals("0")  &&  t2.getText().toString().equals("0"))
        {
            bott.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
*/

    }

    public class connect extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;
        comparebean b;
        int length;
        String url;


        connect(String url)
        {
            this.url = url;
        }




        @Override
        protected Void doInBackground(Void... params) {


           // Log.d("Sub category fragment" , url);

            try {
                //HttpClient client = new DefaultHttpClient();
                //HttpGet get = new HttpGet(url);
                //HttpResponse response = client.execute(get);
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
                String line;
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
               // Log.e("JSON Parser", "Error parsing data " + e.toString());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                // Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }




            try {
                b = (comparebean) getActivity().getApplicationContext();
            }catch (NullPointerException e)
            {
                e.printStackTrace();
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
                    int l = b.list.size();
                    if (l >0) {
                        if (l == 1) {
                            if (obj.getInt("id") == b.list.get(0).getId()) {
                              //  Log.d("asdasdasd", "checked");
                    bean.setSet(true);
                            }
                        }
                        if (l == 2) {
                            if (obj.getInt("id") == b.list.get(0).getId()) {
                                bean.setSet(true);
                            }

                            if (obj.getInt("id") == b.list.get(1).getId()) {
                                bean.setSet(true);
                            }


                        }

                        if (l == 3) {
                            if (obj.getInt("id") == b.list.get(0).getId() || obj.getInt("id") == b.list.get(1).getId() || obj.getInt("id") == b.list.get(2).getId()) {
                                bean.setSet(true);
                            }
                        }
                        if (l == 4) {
                            if (obj.getInt("id") == b.list.get(0).getId() || obj.getInt("id") == b.list.get(1).getId() || obj.getInt("id") == b.list.get(2).getId() || obj.getInt("id") == b.list.get(3).getId()) {
                                bean.setSet(true);
                            }
                        }
                    }

                    int l2 = b.tempList.size();
                    if (l2 >0) {

                        for (int j = 0 ; j<l2 ; j++)
                        {
                            if (obj.getInt("id") == b.tempList.get(j).getId())
                            {
                                bean.setSetlist(true);
                            }
                        }

                    }


                        list1.add(bean);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

           // adapter.setGridData(list1);



            try
            {
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout)((MainActivity)getContext()).findViewById(R.id.coordinate);
                View view = coordinatorLayout.findViewById(R.id.bottombar);
                BottomSheetBehavior bot = BottomSheetBehavior.from(view);
                adapter = new ProdAdapter2(getContext() ,  list1 , bot);
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            //adapter.setGridData(list1);
            grid.setAdapter(adapter);
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);

        }
    }


}