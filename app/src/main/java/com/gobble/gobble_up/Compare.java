package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Compare extends AppCompatActivity {

    ImageView iv1 , iv2;

    Button bt1 , bt2;

    ListView compareList;
    int flag = 1;
    Toolbar toolbar;
    ImageButton next , prev;


    int first , second;

    ArrayList<compareListViewBean> list1;

    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
            .cacheOnDisc(false).resetViewBeforeLoading(true).build();


    ImageLoader imageLoader = ImageLoader.getInstance();



    //imageLoader.displayImage(item.getImage() , holder.imageView , options);
    //Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
    //holder.imageView.startAnimation(animation);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ompare);

        toolbar = (Toolbar)findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    iv1 = (ImageView)findViewById(R.id.compareImage1);
    iv2 = (ImageView)findViewById(R.id.compareImage2);


        bt1 = (Button)findViewById(R.id.bt_add_left);
        bt2 = (Button)findViewById(R.id.bt_add_right);


        compareList = (ListView)findViewById(R.id.compareList3);


        comparebean b = (comparebean)this.getApplicationContext();

        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.previous);

        int count = b.list.size();

        final comparebean be = (comparebean)this.getApplicationContext();


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext() , AddtoList.class);


                int iidd = be.list.get(first).getId();

                i.putExtra("listId" , iidd);


                startActivityForResult(i , 1);

            }
        });


        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext() , AddtoList.class);


                int iidd = be.list.get(second).getId();

                i.putExtra("listId" , iidd);


                startActivityForResult(i , 1);

            }
        });




       // Log.d("fragment" , String.valueOf(count));


        if (count == 0)
        {
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            bt2.setVisibility(View.INVISIBLE);
            Toast.makeText(getBaseContext() , "Empty list" , Toast.LENGTH_SHORT).show();
        }

        if (count == 1)
        {

            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            bt2.setVisibility(View.INVISIBLE);
            //new loadImage(iv1 , b.list.get(0).getImage()).execute();

            Toast.makeText(getBaseContext() , "Only one item to compare" , Toast.LENGTH_SHORT).show();

        }

        if(count == 2)
        {

            list1 = new ArrayList<>();

            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
            set(0,1);

        }


        if(count == 3)
        {

            list1 = new ArrayList<>();

            prev.setVisibility(View.INVISIBLE);

            set(0,1);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    prev.setVisibility(View.VISIBLE);
                    set(1,2);

                    next.setVisibility(View.INVISIBLE);


                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    next.setVisibility(View.VISIBLE);

                    set(0,1);
                    prev.setVisibility(View.INVISIBLE);
                }
            });


        }



        if (count == 4)
        {

            set(0,1);

            prev.setVisibility(View.INVISIBLE);


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (flag == 2)
                    {
                        flag++;
                        set(2,3);
                        next.setVisibility(View.INVISIBLE);
                    }
                    if (flag == 1)
                    {
                        flag++;
                        set(1,2);
                        prev.setVisibility(View.VISIBLE);
                    }

                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == 2)
                    {
                        flag--;
                        set(0,1);
                        prev.setVisibility(View.INVISIBLE);
                    }
                    if (flag == 3)
                    {
                        flag--;
                        set(1,2);
                        next.setVisibility(View.VISIBLE);
                    }
                }
            });


        }



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        if (requestCode == 1)
        {

            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(getApplicationContext() , "Added to list" , Toast.LENGTH_SHORT).show();

            }

        }

    }


    public void set(int fir , int sec)
    {

        first = fir;
        second = sec;

        list1 = new ArrayList<>();

        comparebean b = (comparebean)this.getApplicationContext();



        iv1.setImageBitmap(null);
        iv2.setImageBitmap(null);


        imageLoader.displayImage(b.list.get(fir).getImage() , iv1 , options);
        imageLoader.displayImage(b.list.get(sec).getImage() , iv2 , options);

       // new loadImage(iv1 , b.list.get(fir).getImage()).execute();
       // new loadImage(iv2 , b.list.get(sec).getImage()).execute();
        compareListViewBean bean2 = new compareListViewBean();
        bean2.setHeader("Name");
        bean2.setFirst(b.list.get(fir).getName());
        bean2.setSecond(b.list.get(sec).getName());

        list1.add(bean2);


        compareListViewBean bean3 = new compareListViewBean();
        bean3.setHeader("Price");
        bean3.setFirst(b.list.get(fir).getPrice());
        bean3.setSecond(b.list.get(sec).getPrice());



        list1.add(bean3);


        compareListViewBean bean4 = new compareListViewBean();
        bean4.setHeader("Description");
        bean4.setFirst(b.list.get(fir).getDescription());
        bean4.setSecond(b.list.get(sec).getDescription());

        list1.add(bean4);


        compareListAdapter adapter = new compareListAdapter(getBaseContext() , R.layout.compare_list_model , list1);
        compareList.setAdapter(adapter);
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


            //Log.d("fragment" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            iv.setImageBitmap(d);

        }
    }






}
