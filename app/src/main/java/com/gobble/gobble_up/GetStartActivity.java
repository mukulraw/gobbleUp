package com.gobble.gobble_up;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

public class GetStartActivity extends AppCompatActivity {


    Button start;
    ViewPager slider;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String url , n;
    CirclePageIndicator indi;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);













        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        indi = (CirclePageIndicator)findViewById(R.id.circle);


        pref = getSharedPreferences("MySignin", Context.MODE_PRIVATE);
        edit = pref.edit();


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        slider = (ViewPager)findViewById(R.id.slider);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 4);
        slider.setAdapter(mSectionsPagerAdapter);



        indi.setViewPager(slider);
        final comparebean be = (comparebean)this.getApplicationContext();

        Boolean bool = pref.getBoolean("get" , false);
        if (bool)
        {
            Bundle b = getIntent().getExtras();

            be.user_id = b.getString("id");

            Intent i = new Intent(GetStartActivity.this , MainActivity.class);
            if(b!=null) {

                url = b.getString("url");
                n = b.getString("name");
                if (url!=null)
                {
                    i.putExtra("url" , url);

                }
                i.putExtra("name" , n);
            }

            startActivity(i);
            finish();
        }





        start = (Button)findViewById(R.id.bt_getstart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = getIntent().getExtras();

                be.user_id = b.getString("id");

                Intent i = new Intent(GetStartActivity.this , MainActivity.class);
                if(b!=null) {

                    url = b.getString("url");
                    n = b.getString("name");
                     if (url!=null)
                     {
                         i.putExtra("url" , url);

                     }
                    i.putExtra("name" , n);
                }

                edit.putBoolean("get" , true);
                edit.apply();
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {


                }
                else
                {

                    Toast.makeText(this , "Permission denied" , Toast.LENGTH_SHORT).show();

                    finish();
                }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



    }


    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        final int noOfTabs;

        SectionsPagerAdapter(FragmentManager fm, int noOfTabs) {
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
                    return new FirstPage1();
                case 1:
                    return new SecondPage();
                case 2:
                    return new ThirdPage();
                case 3:
                    return new FourthPage();

            }
            return null;
        }
    }

    public static class FirstPage1 extends Fragment {



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.page1, container, false);
        }
    }

    public static class SecondPage extends Fragment {



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.page2, container, false);
        }
    }
    public static class ThirdPage extends Fragment {



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.page3, container, false);
        }
    }
    public static class FourthPage extends Fragment {



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.page4, container, false);
        }
    }

}
