package com.gobble.gobble_up;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;


public class CampareFrag1 extends Fragment {



    ImageView iv1 , iv2;


    public static CampareFrag1 newInstance() {
        CampareFrag1 fragment = new CampareFrag1();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.compare_view_pager_fragment1, container, false);

        iv1 = (ImageView)rootview.findViewById(R.id.compareImage1);
        iv2 = (ImageView)rootview.findViewById(R.id.compareImage2);
        comparebean b = (comparebean)getActivity().getApplicationContext();


        int count = b.list.size();

        Log.d("fragment" , String.valueOf(count));



        if (count == 0)
        {
            Toast.makeText(getActivity() , "Empty list" , Toast.LENGTH_SHORT).show();
        }

        if (count == 1)
        {
            new loadImage(iv1 , b.list.get(0).getImage()).execute();

        }

        if (count == 2)
        {
            new loadImage(iv2 , b.list.get(1).getImage()).execute();
        }


        return rootview;


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


            Log.d("fragment" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            iv.setImageBitmap(d);

        }
    }


}
