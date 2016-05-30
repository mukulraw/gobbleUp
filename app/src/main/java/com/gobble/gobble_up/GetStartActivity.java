package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GetStartActivity extends AppCompatActivity {


    Button start;
    ViewPager slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);
        slider = (ViewPager)findViewById(R.id.slider);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 4);
        slider.setAdapter(mSectionsPagerAdapter);

        start = (Button)findViewById(R.id.bt_getstart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetStartActivity.this , FirstPage.class);
                startActivity(i);
            }
        });

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
