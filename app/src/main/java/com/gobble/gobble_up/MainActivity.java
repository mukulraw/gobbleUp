package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private BottomSheetBehavior bar;
    private TextView countt , countsa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)this.findViewById(R.id.coordinate);



        View botto = coordinatorLayout.findViewById(R.id.bottombar);

        bar = BottomSheetBehavior.from(botto);


       // bar.setState(BottomSheetBehavior.STATE_COLLAPSED);

        ImageButton list = (ImageButton) findViewById(R.id.imageButton3);

        countt = (TextView)findViewById(R.id.textView5);
        countsa = (TextView)findViewById(R.id.textView4);
        ImageButton compare = (ImageButton) findViewById(R.id.imageButton2);


        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , Compare2.class);

                startActivityForResult(i , 12);
            }
        });


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , TempList.class);
                startActivityForResult(i , 112);
            }
        });

        buildGoogleApiClient();

        NavigationView nav = (NavigationView)findViewById(R.id.navId);

        nav.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        @SuppressLint("InflateParams") View header = LayoutInflater.from(this).inflate(R.layout.nav_header , null);
        nav.addHeaderView(header);

        Bundle b = getIntent().getExtras();

        if(b!=null) {
            String url = String.valueOf(b.get("url"));
            String n = String.valueOf(b.get("name"));

            comparebean be = (comparebean)this.getApplicationContext();

            be.url = url;
            be.n = n;

            //Log.d("asdasdasd" , n);
            CircleImageView profile = (CircleImageView) header.findViewById(R.id.headerProfile);
            TextView head_name = (TextView) header.findViewById(R.id.headertitle);
            if (url !=null)
            {
                new loadImage(profile, url).execute();
            }

            head_name.setText(n);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        CategoryFragment frag1 = new CategoryFragment();



        //ft.remove(frag1);
        ft.add(R.id.layoutToReplace , frag1);
        ft.commit();



    }
    private Bitmap LoadImageFromURL(String url)

    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            return BitmapFactory.decodeStream(is);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu , menu);
        return true;
    }









    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_gallery) {

            Intent i = new Intent(getApplicationContext() , Compare2.class);

            startActivity(i);


        }

        if (id == R.id.nav_camera)
        {


            Intent i = new Intent(getApplicationContext() , MainList.class);

            startActivity(i);

        }

        if(id == R.id.nav_log_out)
        {
            pref = getSharedPreferences("MySignin", Context.MODE_PRIVATE);
            edit = pref.edit();


            if (pref.getBoolean("google" , false))
            {
                if (mGoogleApiClient.isConnected())
                {
                    signOut();
                }
            }
            else if (pref.getBoolean("fb" , false))
            {
                LoginManager.getInstance().logOut();


                edit.remove("fb");
                edit.apply();
                Intent i = new Intent(getApplicationContext() , LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }

            else
            {
                Intent i = new Intent(getApplicationContext() , LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
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
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onResult(@NonNull Status status) {


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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_bar)
        {

            Intent i = new Intent(getApplicationContext() , SearchResultActivity.class);
            startActivityForResult(i , 123);
            overridePendingTransition(0,0);
        }


        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123)
        {

            if (resultCode == RESULT_OK)
            {

                Bundle bundle = data.getExtras();
                String id = String.valueOf(bundle.get("result"));
                FragmentManager fm = getSupportFragmentManager();

                Bundle b = new Bundle();
                b.putString("id" , id);
                b.putString("image" , String.valueOf(bundle.get("image")));



                FragmentTransaction ft = fm.beginTransaction();
                SingleProductFragment frag = new SingleProductFragment();

                frag.setArguments(b);

                ft.replace(R.id.layoutToReplace , frag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.addToBackStack(null);
                ft.commit();

            }

        }
        if (requestCode == 112)
        { comparebean be = (comparebean)this.getApplicationContext();


            countt.setText(String.valueOf(be.tempList.size()));



            if (be.tempList.size() == 0  &&  be.list.size() == 0)
            {

                bar.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        if (requestCode == 12)
        {
            comparebean be = (comparebean)this.getApplicationContext();
            countsa.setText(String.valueOf(be.list.size()));
            if (be.tempList.size() == 0  &&  be.list.size() == 0)
            {

                bar.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }


    }

    private class loadImage extends AsyncTask<Void , Void , Void>
    {

        String url;
        CircleImageView iv;
        Bitmap d;

        loadImage(CircleImageView iv, String url)
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
}
