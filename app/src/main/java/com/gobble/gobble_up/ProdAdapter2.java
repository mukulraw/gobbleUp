package com.gobble.gobble_up;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by hi on 08-06-2016.
 */
public class ProdAdapter2 extends RecyclerView.Adapter<ProdAdapter2.RecycleViewHolder>{
    private Context context;
    //private final LayoutInflater mInflater;
    private ArrayList<ProductBean> list = new ArrayList<>();

    RelativeLayout bar;



    public ProdAdapter2(Context context , ArrayList<ProductBean> list , RelativeLayout bar)
    {
        //mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.bar = bar;
    }


    @Override
    public ProdAdapter2.RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_list_model, null);

        RecycleViewHolder viewHolder = new RecycleViewHolder(layoutView);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {
        final comparebean b = (comparebean)context.getApplicationContext();
        final ProductBean item = list.get(position);
     //   Log.d("asdasdasd" , "adapter2");

        holder.setIsRecyclable(false);

        holder.titleTextView.setText(item.getName());
        String price = "Price: " + item.getPrice();



        //holder.bind(item);

        holder.priceTextView.setText(price);


        for (int i = 0 ; i<list.size() ; i++) {


            if (item.getSet()) {
                holder.switcher.setChecked(true);

            }
        }


        for (int i = 0 ; i<b.tempList.size() ; i++)
        {
            if (item.getId() == b.tempList.get(i).getId())
            {
                holder.addlist.setVisibility(View.GONE);
            }
        }








        holder.addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.tempList.add(item);
                b.comparecount++;
                item.setSetlist(true);
                if (bar.getVisibility() == View.GONE)
                {
                    TranslateAnimation animate = new TranslateAnimation(0,0,bar.getHeight(),0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    bar.startAnimation(animate);
                    bar.setVisibility(View.VISIBLE);
                }
                TextView comp = (TextView)((MainActivity)context).findViewById(R.id.textView5);
                comp.setText(String.valueOf(b.tempList.size()));
                Toast.makeText(context , "Added to List" , Toast.LENGTH_SHORT).show();

                holder.addlist.setVisibility(View.GONE);

            }
        });




        holder.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                if (isChecked)
                {


                    if (b.list.size() < 4)
                    {

                        b.list.add(item);
                        b.comparecount++;
                        item.setSet(true);

                        if (bar.getVisibility() == View.GONE)
                        {
                            TranslateAnimation animate = new TranslateAnimation(0,0,bar.getHeight(),0);
                            animate.setDuration(500);
                            animate.setFillAfter(true);
                            bar.startAnimation(animate);
                            bar.setVisibility(View.VISIBLE);
                        }



                        TextView comp = (TextView)((MainActivity)context).findViewById(R.id.textView4);
                        comp.setText(String.valueOf(b.list.size()));

                        //bar.animate().alpha(1.0f);
                        // b.bitmaps.add(LoadImageFromURL(item.getImage()));
                        //Log.d("asdasdasd" , String.valueOf(b.list.size()));
                        Toast.makeText(context , "Added to Compare" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context , "Max. limit reached" , Toast.LENGTH_SHORT).show();

                        holder.switcher.setChecked(false);
                    }


                }
                else
                {
                    int index = 0;
                   // Log.d("asdasdasd" , item.getName());
                    int l = b.list.size();


                    if (l == 1)
                    {
                        TextView comp1 = (TextView)((MainActivity)context).findViewById(R.id.textView5);

                        if (comp1.getText().equals("0"))
                        {
                            if (bar.getVisibility() == View.VISIBLE)
                            {
                                TranslateAnimation animate = new TranslateAnimation(0,0,0,bar.getHeight());
                                animate.setDuration(500);
                                animate.setFillAfter(true);
                                bar.startAnimation(animate);
                                bar.setVisibility(View.GONE);
                            }
                        }




                    }

                    for (int i = 0 ; i<l ; i++)
                    {
                        if (item.getId() == b.list.get(i).getId())
                        {
                            index = i;
                        }
                    }


                    b.list.remove(index);
                    item.setSet(false);
                    b.comparecount--;
                    TextView comp = (TextView)((MainActivity)context).findViewById(R.id.textView4);
                    comp.setText(String.valueOf(b.list.size()));
                    //b.list.remove(item);
                    Toast.makeText(context , "Item removed" ,Toast.LENGTH_SHORT ).show();
                }

            }
        });

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(false).resetViewBeforeLoading(true).build();


        ImageLoader imageLoader = ImageLoader.getInstance();



        imageLoader.displayImage(item.getImage() , holder.imageView , options);
        Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
        holder.imageView.startAnimation(animation);

            //new loadImage(holder.imageView, item.getImage()).execute();





    }



    @Override
    public int getItemCount() {



        return list.size();
    }

    public class loadImage extends AsyncTask<Void, Void, Void> {

        String url;
        ImageView iv;
        Bitmap d;

        public loadImage(ImageView iv, String url) {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {


           // Log.d("asdasdasd", url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
            iv.startAnimation(animation);
            iv.setImageBitmap(d);

        }
    }

    private Bitmap LoadImageFromURL(String url)

    {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            return d;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }




    class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        TextView priceTextView;
        ImageView imageView;
        TextView addlist;
        Switch switcher;


        public RecycleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView)itemView.findViewById(R.id.prodName);
            priceTextView = (TextView) itemView.findViewById(R.id.prodPrice);
            imageView = (ImageView)itemView.findViewById(R.id.prodImage);
            switcher = (Switch) itemView.findViewById(R.id.switcher);
            addlist = (TextView)itemView.findViewById(R.id.addlist);


        }

        public void bind(ProductBean item) {

            titleTextView.setText(item.getName());
            priceTextView.setText(item.getPrice());
            new loadImage(imageView , item.getImage()).execute();

        }

        @Override
        public void onClick(View v) {

            ProductBean item = (ProductBean) list.get(getPosition());

            SingleProductFragment frag3 = new SingleProductFragment();
            Bundle b = new Bundle();

            b.putString("id" , String.valueOf(item.getId()));
            //b.putString("name" , item.getName());
            //b.putString("price" , item.getPrice());
            //b.putString("desc" , item.getDescription());
            b.putString("image" , item.getImage());

            frag3.setArguments(b);
            FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.layoutToReplace , frag3);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.addToBackStack(null);
            ft.commit();
        }




    }
}
