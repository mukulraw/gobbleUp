package com.gobble.gobble_up;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

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

/**
 * Created by hi on 10-06-2016.
 */
public class SubCategoryFragment extends Fragment {
    TextView title;
    private String SUB_CATEGORY = "http://nationproducts.in/global/api/categories/id/";
    ArrayList<categoryBean> list1;
    //private ProgressBar mProgressBar;
    ImageView banner;
    TabLayout tab;
    FragStatePagerAdapter adapter1;
    ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_cat_loader , container , false);
        title = (TextView)view.findViewById(R.id.cat_title);
        banner = (ImageView)view.findViewById(R.id.cat_iamge);
        tab = (TabLayout)view.findViewById(R.id.cat_tabs);

        //Bundle b = getIntent().getExtras();



        String a = getArguments().getString("id");

        title.setText(getArguments().getString("name"));

        String u = getArguments().getString("image");

        new loadImage(banner , u).execute();

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

    public class loadImage extends AsyncTask<Void , Void , Void>
    {

        String url;
        ImageView iv;
        Bitmap d;

        public loadImage(ImageView iv , String url)
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
            Animation animation = null;
            if (getContext()!=null)
            {
                animation = AnimationUtils.loadAnimation(getContext() , R.anim.fade);
            }

            iv.startAnimation(animation);
            iv.setImageBitmap(d);
            title.setVisibility(View.VISIBLE);
        }
    }

    public void refresh(String cat)
    {
        list1.clear();
        String url = SUB_CATEGORY + cat;
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


            //Log.d("asdasdasd" , url);

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
              //  Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException e) {
                e.printStackTrace();
              //  Log.e("JSON Parser", "Error parsing data " + e.toString());
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
            tab.setVisibility(View.VISIBLE);
            //mProgressBar.setVisibility(View.GONE);

        }
    }


}
