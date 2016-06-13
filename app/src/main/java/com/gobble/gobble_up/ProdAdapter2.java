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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hi on 08-06-2016.
 */
public class ProdAdapter2 extends RecyclerView.Adapter<ProdAdapter2.RecycleViewHolder>{
    private Context context;
    private ArrayList<ProductBean> list = new ArrayList<>();

    RelativeLayout bar;



    public ProdAdapter2(Context context , ArrayList<ProductBean> list , RelativeLayout bar)
    {
        this.context = context;
        this.list = list;
        this.bar = bar;
    }
    public void setGridData(ArrayList<ProductBean> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_list_model, null);

        return new RecycleViewHolder(layoutView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {
        final ProductBean item = list.get(position);
        Log.d("asdasdasd" , "adapter2");
        final comparebean b = (comparebean)context.getApplicationContext();
        holder.titleTextView.setText(item.getName());
        String price = "Price: " + item.getPrice();
        holder.priceTextView.setText(price);


        for (int i = 0 ; i<list.size() ; i++)
        {


            if(item.getSet()!=null)
            {
                if (item.getSet())
                {
                    holder.switcher.setChecked(true);

                }
            }
        }





        holder.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                if (isChecked)
                {


                    if (b.list.size() < 4)
                    {

                        b.list.add(item);
                        b.comparecount++;

                        if (bar.getVisibility() == View.INVISIBLE)
                        {
                            TranslateAnimation animate = new TranslateAnimation(0,0,bar.getHeight(),0);
                            animate.setDuration(500);
                            animate.setFillAfter(true);
                            bar.startAnimation(animate);
                            bar.setVisibility(View.VISIBLE);
                        }



                        TextView comp = (TextView)((MainActivity)context).findViewById(R.id.barcompare);
                        comp.setText("Compare " + String.valueOf(b.list.size()));

                        //bar.animate().alpha(1.0f);
                        // b.bitmaps.add(LoadImageFromURL(item.getImage()));
                        Log.d("asdasdasd" , String.valueOf(b.list.size()));
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
                    Log.d("asdasdasd" , item.getName());
                    int l = b.list.size();

                    if (l == 1)
                    {
                        if (bar.getVisibility() == View.VISIBLE)
                        {
                            TranslateAnimation animate = new TranslateAnimation(0,0,0,bar.getHeight());
                            animate.setDuration(500);
                            animate.setFillAfter(true);
                            bar.startAnimation(animate);
                            bar.setVisibility(View.INVISIBLE);
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
                    b.comparecount--;
                    TextView comp = (TextView)((MainActivity)context).findViewById(R.id.barcompare);
                    comp.setText("Compare " + String.valueOf(b.list.size()));
                    //b.list.remove(item);
                    Toast.makeText(context , "Item removed" ,Toast.LENGTH_SHORT ).show();
                }

            }
        });


        new loadImage(holder.imageView, item.getImage()).execute();


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


            Log.d("asdasdasd", url);
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
        Switch switcher;


        public RecycleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView)itemView.findViewById(R.id.prodName);
            priceTextView = (TextView) itemView.findViewById(R.id.prodPrice);
            imageView = (ImageView)itemView.findViewById(R.id.prodImage);
            switcher = (Switch) itemView.findViewById(R.id.switcher);



        }

        @Override
        public void onClick(View v) {


            ProductBean item = (ProductBean) list.get(getPosition());

            SingleProductFragment frag3 = new SingleProductFragment();
            Bundle b = new Bundle();

            b.putString("id" , String.valueOf(item.getId()));
            b.putString("name" , item.getName());
            b.putString("price" , item.getPrice());
            b.putString("desc" , item.getDescription());
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
