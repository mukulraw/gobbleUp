package com.gobble.gobble_up;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import java.util.ArrayList;
import java.util.List;


public class comparebean extends MultiDexApplication {
    ArrayList<ProductBean> list = new ArrayList<>();
    ArrayList<ProductBean> tempList = new ArrayList<>();
    int tempListCount = 0;
    List<Bitmap> bitmaps = new ArrayList<>();
    String url = null;
    String n = null;
    int comparecount = 0;
    String user_id;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
