package com.gobble.gobble_up;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.viewpagerindicator.CirclePageIndicator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class CategoryFragment extends Fragment {

    ArrayList<categoryBean> list;
    private RecyclerView grid;
    private ViewPager slide;
    private ProgressBar progressBar;
    private TextView bannerText;
    private CirclePageIndicator indi;
    private AppBarLayout appBarLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category2,
                container, false);

        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 2);

        indi = (CirclePageIndicator)view.findViewById(R.id.circle2);
        grid = (RecyclerView) view.findViewById(R.id.gridView);
        bannerText = (TextView)view.findViewById(R.id.bannerText);

        appBarLayout = (AppBarLayout)view.findViewById(R.id.htab_appbar);


        progressBar = (ProgressBar)view.findViewById(R.id.progress);
        slide = (ViewPager)view.findViewById(R.id.slide);

        grid.setHasFixedSize(true);
        grid.setLayoutManager(lLayout);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        slide.setAdapter(mSectionsPagerAdapter);
        indi.setViewPager(slide);

        list = new ArrayList<>();




        progressBar.setVisibility(View.VISIBLE);

        refresh();






        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {


        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GetStartActivity.FirstPage1();
                case 1:
                    return new GetStartActivity.SecondPage();
                case 2:
                    return new GetStartActivity.ThirdPage();
                case 3:
                    return new GetStartActivity.FourthPage();

            }
            return null;
        }
    }





    private void refresh()
    {
        list.clear();
        String GET_CATEGORY = "http://nationproducts.in/global/api/categories";
        new connect(GET_CATEGORY).execute();

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
            URL u = null;
            try {
                u = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;

            try {


                if (u != null) {
                    connection = (HttpURLConnection)u.openConnection();
                }


                if (connection != null && connection.getResponseCode() == 200) {
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

            } catch (IOException|NullPointerException e) {
                e.printStackTrace();

                refresh();

            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
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


                    list.add(bean);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            CatGridAdapter adapter = new CatGridAdapter(getActivity(), list);
            grid.setAdapter(adapter);


            progressBar.setVisibility(View.GONE);
            bannerText.setVisibility(View.VISIBLE);
            slide.setVisibility(View.VISIBLE);
            indi.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.VISIBLE);
        }
    }





}
