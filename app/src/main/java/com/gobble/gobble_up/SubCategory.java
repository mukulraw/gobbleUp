package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView title;
    GridView grid;
    private String SUB_CATEGORY = "http://nationproducts.in/global/api/categories/id/";
    private CatGridAdapter adapter;
    ArrayList<categoryBean> list1;
    //private ProgressBar mProgressBar;
    ImageView banner;
    TextView ban_title;
    //private CollapsingToolbarLayout collapsingToolbar;
    TabLayout tab;
    private Toolbar toolbar;
    FragStatePagerAdapter adapter1;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    ViewPager pager;
    CircleImageView profile;
    TextView head_name;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sub_category);
        //setContentView(R.layout.sub_cat);
        setContentView(R.layout.sub_cat_main);
        title = (TextView)findViewById(R.id.cat_title);

        //mProgressBar = (ProgressBar)findViewById(R.id.progressbar2);
        //collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar= (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buildGoogleApiClient();

        NavigationView nav = (NavigationView)findViewById(R.id.nav_drawer_sub_cat);
        nav.setNavigationItemSelectedListener(this);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header , null);
        if (nav != null) {
            nav.addHeaderView(header);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_sub_cat);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        banner = (ImageView)findViewById(R.id.cat_iamge);

        tab = (TabLayout)findViewById(R.id.cat_tabs);


        Bundle b = getIntent().getExtras();
        comparebean be = (comparebean)this.getApplicationContext();

        String url = be.url;
        String n = be.n;

        profile = (CircleImageView)header.findViewById(R.id.headerProfile);
        head_name = (TextView)header.findViewById(R.id.headertitle);
        if (url!=null)
        {
            new loadImage(profile , url).execute();
        }

        head_name.setText(n);


        String a = String.valueOf(b.get("id"));

        title.setText((CharSequence) b.get("name"));

        String u = String.valueOf(b.get("image"));

        new loadImage(banner , u).execute();

        pager = (ViewPager)findViewById(R.id.pagerId);

        list1 = new ArrayList<>();
        //adapter = new CatGridAdapter(this , R.layout.category_model , list1);
        //grid.setAdapter(adapter);




       refresh(a);


     /*   grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryBean item = (categoryBean) parent.getItemAtPosition(position);

                Intent i = new Intent(SubCategory.this , Products.class);
                i.putExtra("id" , item.getId());
                i.putExtra("name" , item.getName());
                startActivity(i);
            }
        });
*/

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
            Log.d("asdasdasd" , "log out clicked" );
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
        ImageView iv;
        Bitmap d;

        public loadImage(ImageView iv , String url)
        {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {


            Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.fade);
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


            Log.d("asdasdasd" , url);

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


            for(int i = 0 ; i<length ;i++)
            {
                String n = list1.get(i).getName();
                Log.d("asdasdasd" , n);
                tab.addTab(tab.newTab().setText(n));
            }

            adapter1 = new FragStatePagerAdapter(getSupportFragmentManager() , list1 , tab.getTabCount());
            pager.setAdapter(adapter1);
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
