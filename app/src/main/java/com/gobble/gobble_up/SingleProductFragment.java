package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hi on 10-06-2016.
 */
public class SingleProductFragment extends Fragment implements View.OnClickListener {
    ArrayList<bean> list;
    RecyclerView lv;
    ImageView iv;
    TextView title;
    private GridLayoutManager lLayout;
    Button add;
    String iidd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.single_prod_main , container , false);

        add = (Button)view.findViewById(R.id.addtolist);
        title = (TextView)view.findViewById(R.id.title_single);

        lLayout = new GridLayoutManager(getContext() , 1);

        iidd = getArguments().getString("id");
        String a = getArguments().getString("name");
        String image = getArguments().getString("image");
        String price = getArguments().getString("price");
        String desc = getArguments().getString("desc");
        title.setText(a);
        iv = (ImageView)view.findViewById(R.id.prodImage1);
        lv = (RecyclerView)view. findViewById(R.id.prodList1);

        new loadImage(iv , image).execute();


        list = new ArrayList<>();
        list.add(new bean("price" , price));
        list.add(new bean("description" , desc));

        singleAdapter2 adapter = new singleAdapter2(getContext() ,list);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(lLayout);
        lv.setAdapter(adapter);


        add.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        if (v == add)
        {
            Intent i = new Intent(getContext() , AddtoList.class);

            i.putExtra("listId" , iidd);


            startActivityForResult(i , 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        if (requestCode == 1)
        {

            if (resultCode == Activity.RESULT_OK)
            {

                final Animation animation = new AlphaAnimation(1,0); // Change alpha from fully visible to invisible
                animation.setDuration(200); // duration - half a second
                animation.setInterpolator(new LinearInterpolator());
                animation.setBackgroundColor(getResources().getColor(R.color.yellow));
                // do not alter animation rate
                animation.setRepeatCount(2); // Repeat animation infinitely
                animation.setRepeatMode(Animation.REVERSE);

                add.startAnimation(animation);

                RelativeLayout bar = (RelativeLayout)((MainActivity)getContext()).findViewById(R.id.bottombar);
                if (bar.getVisibility() == View.INVISIBLE)
                {
                    TranslateAnimation animate = new TranslateAnimation(0,0,bar.getHeight(),0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    bar.startAnimation(animate);
                    bar.setVisibility(View.VISIBLE);
                }

            }

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


            Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext() , R.anim.fade);
            iv.startAnimation(animation);
            iv.setImageBitmap(d);

        }
    }

}
