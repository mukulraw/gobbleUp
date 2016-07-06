package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.viewpagerindicator.CirclePageIndicator;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.util.List;
import java.util.zip.Inflater;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirstPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String GET_CATEGORY = "http://nationproducts.in/global/api/categories";
    //private ProgressBar mProgressBar;
    private CatGridAdapter adapter;
    ArrayList<categoryBean> list;
    private RecyclerView grid;
    ViewPager slide;
    CircleImageView profile;
    TextView head_name;
    TextView bannerText;
    String url , n;
    private SharedPreferences pref;
    CirclePageIndicator indi;
    private SharedPreferences.Editor edit;
    GoogleApiClient mGoogleApiClient;
    private GridLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.category_page);
        setContentView(R.layout.category_main);
        //setContentView(R.layout.category3);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lLayout = new GridLayoutManager(this , 2);

        indi = (CirclePageIndicator)findViewById(R.id.circle2);

        buildGoogleApiClient();


        //mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        grid = (RecyclerView) findViewById(R.id.gridView);
        bannerText = (TextView)findViewById(R.id.bannerText);

        grid.setHasFixedSize(true);
        grid.setLayoutManager(lLayout);








        Bundle b = getIntent().getExtras();


        //new loadImage(profile , url);



        //head_name.setText(n);

        NavigationView nav = (NavigationView)findViewById(R.id.navId);

        nav.setNavigationItemSelectedListener(this);

        @SuppressLint("InflateParams") View header = LayoutInflater.from(this).inflate(R.layout.nav_header , null);
        if (nav != null) {
            nav.addHeaderView(header);
        }

        if(b!=null) {
            url = String.valueOf(b.get("url"));
            n = String.valueOf(b.get("name"));

            comparebean be = (comparebean)this.getApplicationContext();

            be.url = url;
            be.n = n;

            //Log.d("asdasdasd" , n);
            profile = (CircleImageView)header.findViewById(R.id.headerProfile);
            head_name = (TextView)header.findViewById(R.id.headertitle);
            if (url!=null)
            {
                new loadImage(profile , url).execute();
            }

            head_name.setText(n);


        }




        //new loadImage(profile , url);
        //head_name.setText(n);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
       // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       // drawer.setDrawerListener(toggle);
      //  toggle.syncState();

        slide = (ViewPager)findViewById(R.id.slide);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 4);
        slide.setAdapter(mSectionsPagerAdapter);



        indi.setViewPager(slide);

        list = new ArrayList<>();



        refresh();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu , menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_bar);
        {

            Intent i = new Intent(getApplicationContext() , SearchResultActivity.class);
            startActivity(i);
            overridePendingTransition(0,0);
        }


        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
        //mGoogleApiClient.connect();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_gallery) {

            Intent i = new Intent(getApplicationContext() , Compare.class);
            startActivity(i);

        }

        if (id == R.id.nav_camera)
        {
            Intent i = new Intent(getApplicationContext() , MainList.class);
            startActivity(i);
        }

        if(id == R.id.nav_log_out)
        {
            //Log.d("asdasdasd" , "log out clicked" );
            if (mGoogleApiClient.isConnected())
            {
                signOut();
            }



        }
        return false;
    }

    private synchronized void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).enableAutoManage(this,this)
                .addApi(LocationServices.API).build();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {


                        if (status.isSuccess())
                        {
                            pref = getSharedPreferences("MySignin", Context.MODE_PRIVATE);
                            edit = pref.edit();
                            edit.remove("google");
                            edit.apply();
                            Intent i = new Intent(getApplicationContext() , LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(0,0);
                            finish();

                        }


                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public class loadImage extends AsyncTask<Void , Void , Void>
    {

        String url;
        CircleImageView iv;
        Bitmap d;

        public loadImage(CircleImageView iv , String url)
        {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {


            //Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            iv.setImageBitmap(d);

        }
    }

    public void refresh()
    {
        list.clear();
        new connect(GET_CATEGORY).execute();
//        mProgressBar.setVisibility(View.VISIBLE);
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
                //Log.e("Buffer Error", "Error converting result " + e.toString());
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
                    categoryBean bean = new categoryBean();
                    bean.setId(obj.getInt("id"));
                    bean.setName(obj.getString("name"));



                    String image = obj.getString("image");
                    image = image.replaceAll(" " , "%20");
                    bean.setImage(image);


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

            adapter = new CatGridAdapter(getBaseContext(),list);
            grid.setAdapter(adapter);

            //adapter.setGridData(list);
            //list.clear();
            // mProgressBar.setVisibility(View.GONE);
            bannerText.setVisibility(View.VISIBLE);
            slide.setVisibility(View.VISIBLE);
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        final int noOfTabs;

        public SectionsPagerAdapter(FragmentManager fm , int noOfTabs) {
            super(fm);
            this.noOfTabs = noOfTabs;
        }



        @Override
        public int getCount() {
            // Show 3 total pages.
            return noOfTabs;
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

}
