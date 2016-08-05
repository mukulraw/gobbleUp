package com.gobble.gobble_up;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by hi on 10-06-2016.
 */
public class SubCategoryFragment extends Fragment {
    private TextView title;

    private ArrayList<categoryBean> list1;
    //private ProgressBar mProgressBar;
   private ImageView banner;
    private TabLayout tab;
    private FragStatePagerAdapter adapter1;
    private AppBarLayout bar;
    private ViewPager pager;
    static String a;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_cat_loader , container , false);
        title = (TextView)view.findViewById(R.id.cat_title);
        banner = (ImageView)view.findViewById(R.id.cat_iamge);
        tab = (TabLayout)view.findViewById(R.id.cat_tabs);

        //Bundle b = getIntent().getExtras();


        bar = (AppBarLayout)view.findViewById(R.id.htab_appbar1);

        a = getArguments().getString("id");

        title.setText(getArguments().getString("name"));

        String u = getArguments().getString("image");

        //new loadImage(banner , u).execute();

        progressBar = (ProgressBar)view.findViewById(R.id.progress_sub_cat);


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(false).build();


        ImageLoader imageLoader = ImageLoader.getInstance();


        imageLoader.displayImage(u , banner , options);


        progressBar.setVisibility(View.VISIBLE);

        pager = (ViewPager)view.findViewById(R.id.pagerId);




        list1 = new ArrayList<>();





        refresh(a);


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



    public void fetch()
    {
        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final subCatAPI request = retrofit.create(subCatAPI.class);
        Call<ArrayList<categoryBean>> call = request.getBooks(a);
        call.enqueue(new Callback<ArrayList<categoryBean>>() {
            @Override
            public void onResponse(Call<ArrayList<categoryBean>> call, Response<ArrayList<categoryBean>> response) {





                for(int i = 0 ; i<response.body().size() ;i++)
                {
                    String n = response.body().get(i).getName();
                    //  Log.d("asdasdasd" , n);
                    tab.addTab(tab.newTab().setText(n));
                }
                adapter1 = new FragStatePagerAdapter(getChildFragmentManager() , response.body() , tab.getTabCount());
                try
                {
                    pager.setAdapter(adapter1);
                    pager.setOffscreenPageLimit(response.body().size() - 1);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

                pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                tab.setTabGravity(TabLayout.GRAVITY_CENTER);
                tab.setTabMode(TabLayout.MODE_SCROLLABLE);
                tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab1) {
                        pager.setCurrentItem(tab1.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab1) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab1) {

                    }
                });

//            adapter.setGridData(list1);
                //list.clear();

                progressBar.setVisibility(View.GONE);
                tab.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                banner.setVisibility(View.VISIBLE);
                bar.setVisibility(View.VISIBLE);





            }

            @Override
            public void onFailure(Call<ArrayList<categoryBean>> call, Throwable t) {
              //  Log.d("Error",t.getMessage());
            }
        });



    }



    public void refresh(String cat)
    {
        list1.clear();
        //String url = SUB_CATEGORY + cat;
       // new connect(url).execute();
        fetch();
    }


    @Override
    public void onResume() {
        super.onResume();

        comparebean compareb = (comparebean)getActivity().getApplicationContext();
        TextView tv = (TextView)getActivity().findViewById(R.id.textView5);
        tv.setText(String.valueOf(compareb.tempList.size()));


    }






    public class connect extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;

        int length;
        String url;
        URL u = null;
        HttpURLConnection connection;


        connect(String url)
        {
            this.url = url;
        }





        @Override
        protected Void doInBackground(Void... params) {



            try {

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
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();

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
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            for(int i = 0 ; i<length ;i++)
            {
                String n = list1.get(i).getName();
              //  Log.d("asdasdasd" , n);
                tab.addTab(tab.newTab().setText(n));
            }

            adapter1 = new FragStatePagerAdapter(getChildFragmentManager() , list1 , tab.getTabCount());
            try
            {
                pager.setAdapter(adapter1);
                pager.setOffscreenPageLimit(list1.size() - 1);
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
            tab.setTabGravity(TabLayout.GRAVITY_CENTER);
            tab.setTabMode(TabLayout.MODE_SCROLLABLE);
            tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab1) {
                    pager.setCurrentItem(tab1.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab1) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab1) {

                }
            });

//            adapter.setGridData(list1);
            //list.clear();

            progressBar.setVisibility(View.GONE);
            tab.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            banner.setVisibility(View.VISIBLE);
            bar.setVisibility(View.VISIBLE);
            //mProgressBar.setVisibility(View.GONE);

        }
    }


}
