package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gobble.gobble_up.POJO.CompareModel;
import com.gobble.gobble_up.POJO.Model;
import com.gobble.gobble_up.POJO.ReviewModel;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class ProdAdapter2 extends RecyclerView.Adapter<ProdAdapter2.RecycleViewHolder>{
    private Context context;
    private String GET_REVIEWS = "http://nationproducts.in/global/api/productreviews/id/";
    private ArrayList<Model> list = new ArrayList<>();
    Typeface tf;
    BottomSheetBehavior bar;
    Toast toast;

    float rat = 0;

    ProdAdapter2(Context context, ArrayList<Model> list, BottomSheetBehavior bar)
    {

        this.context = context;
        this.list = list;
        this.bar = bar;
    }


    public void setGridData(ArrayList<Model> list)
    {
        this.list = list;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_list_model, null);

        return new RecycleViewHolder(layoutView);
    }






    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {
        final comparebean b = (comparebean)context.getApplicationContext();
        final Model item = list.get(position);
     //   Log.d("asdasdasd" , "adapter2");

        toast = Toast.makeText(context , null , Toast.LENGTH_SHORT);

        tf = Typeface.createFromAsset(context.getAssets(), "roboto.ttf");

        holder.setIsRecyclable(false);

        holder.titleTextView.setText(item.getName());
        holder.titleTextView.setTypeface(tf);
        String price = "Price: " + item.getPrice();



        //holder.bind(item);

        holder.priceTextView.setText(price);


        for (int i = 0 ; i<b.list.size() ; i++) {


            if (Objects.equals(item.getId(), b.list.get(i).getId())) {
                holder.switcher.setChecked(true);

            }
        }


        for (int i = 0 ; i<b.tempList.size() ; i++)
        {
            if (Objects.equals(item.getId(), b.tempList.get(i).getId()))
            {

                holder.addlist.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.minus , 0);
            }
        }









        holder.addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                int flag2 = 0;

                for (int i = 0 ; i<b.tempList.size() ; i++)
                {
                    if (Objects.equals(item.getId(), b.tempList.get(i).getId()))
                    {

                        flag2++;


                    }
                }




                if (flag2 == 0)
                {
                    b.tempList.add(item);
                    b.comparecount++;
                    if (bar.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    {

                        bar.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    TextView comp = (TextView)((MainActivity)context).findViewById(R.id.textView5);
                    comp.setText(String.valueOf(b.tempList.size()));


                    holder.addlist.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.minus , 0);
                    toast.setText("Added in Basket");
                    toast.show();
                }


                if (flag2>0)
                {

                    int index = 0;
                    int l = b.tempList.size();
                    for (int i = 0 ; i<l ; i++)
                    {
                        if (Objects.equals(item.getId(), b.tempList.get(i).getId()))
                        {
                            index = i;
                        }
                    }


                    b.tempList.remove(index);
                    b.comparecount--;

                    holder.addlist.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.plus3 , 0);
                    toast.setText("Removed from basket");
                    toast.show();

                    TextView comp = (TextView)((MainActivity)context).findViewById(R.id.textView5);
                    comp.setText(String.valueOf(b.tempList.size()));

                    if (l == 1)
                    {
                        TextView comp1 = (TextView)((MainActivity)context).findViewById(R.id.textView4);

                        if (comp1.getText().equals("0"))
                        {
                            if (bar.getState() == BottomSheetBehavior.STATE_EXPANDED)
                            {

                                bar.setState(BottomSheetBehavior.STATE_COLLAPSED);

                            }
                        }




                    }



                }



            }
        });

        String id = String.valueOf(item.getId());

        rat = 0;


        holder.ratingBar.setRating(Float.parseFloat(item.getRating()));


       // fetch(id , holder.ratingBar);

        //new connect2(GET_REVIEWS+id , holder.ratingBar).execute();


        holder.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                if (isChecked)
                {


                    if (b.list.size() < 4)
                    {

                        b.list.add(item);
                        b.comparecount++;
                        toast.setText("Added to Compare");
                        toast.show();

                        if (bar.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                        {
                            //TranslateAnimation animate = new TranslateAnimation(0,0,bar.getHeight(),0);
                            //animate.setDuration(500);
                            //animate.setFillAfter(true);
                            //bar.startAnimation(animate);
                            //b//ar.setVisibility(View.VISIBLE);
                            bar.setState(BottomSheetBehavior.STATE_EXPANDED);

                        }



                        TextView comp = (TextView)((MainActivity)context).findViewById(R.id.textView4);
                        comp.setText(String.valueOf(b.list.size()));

                        //bar.animate().alpha(1.0f);
                        // b.bitmaps.add(LoadImageFromURL(item.getImage()));
                        //Log.d("asdasdasd" , String.valueOf(b.list.size()));

                    }
                    else {
                        //Toast.makeText(context , "Max. limit reached" , Toast.LENGTH_SHORT).show();

                        toast.setText("Max. limit reached");
                        toast.show();

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
                            if (bar.getState() == BottomSheetBehavior.STATE_EXPANDED)
                            {
                               // TranslateAnimation animate = new TranslateAnimation(0,0,0,bar.getHeight());
                                //animate.setDuration(500);
                                //animate.setFillAfter(true);
                                //bar.startAnimation(animate);
                                //bar.setVisibility(View.GONE);
                                bar.setState(BottomSheetBehavior.STATE_COLLAPSED);

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

                    b.comparecount--;
                    toast.setText("Removed from Compare");
                    toast.show();
                    TextView comp = (TextView)((MainActivity)context).findViewById(R.id.textView4);
                    comp.setText(String.valueOf(b.list.size()));
                    //b.list.remove(item);

                }

            }
        });







        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(false).build();


        ImageLoader imageLoader = ImageLoader.getInstance();



        imageLoader.displayImage(item.getImage() , holder.imageView , options);
        Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
        holder.imageView.startAnimation(animation);








    }


    public void fetch(String url , final RatingBar rating)
    {
        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ReviewAPI request = retrofit.create(ReviewAPI.class);
        Call<ArrayList<ReviewModel>> call = request.getBooks(url);

        call.enqueue(new Callback<ArrayList<ReviewModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewModel>> call, Response<ArrayList<ReviewModel>> response) {


                for (int i = 0 ; i < response.body().size() ; i++)
                {
                    rat = rat + Float.parseFloat(response.body().get(i).getRating());
                }

                rating.setRating(rat/response.body().size());






            }

            @Override
            public void onFailure(Call<ArrayList<ReviewModel>> call, Throwable t) {
                //Log.d("Error",t.getMessage());
            }
        });



    }


    private class connect2 extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;
        comparebean b;
        int length;
        String url;
        URL u = null;
        HttpURLConnection connection;

        RatingBar rating;


        connect2(String url , RatingBar rating)
        {
            this.url = url;
            this.rating = rating;
        }




        @Override
        protected Void doInBackground(Void... params) {


            // Log.d("Sub category fragment" , url);

            try {
                //HttpClient client = new DefaultHttpClient();
                //HttpGet get = new HttpGet(url);
                //HttpResponse response = client.execute(get);
                //HttpEntity entity = response.getEntity();
                //is = entity.getContent();
                u = new URL(url);
                connection = (HttpURLConnection)u.openConnection();
                if(connection.getResponseCode()==200)
                {
                    is = connection.getInputStream();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }



            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                // Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            for (int i=0 ; i<length;i++)
            {

                try {
                    JSONObject obj = array.getJSONObject(i);


                    String r = obj.getString("rating");


                    rat = rat + Float.parseFloat(r);





                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            rating.setRating(rat/length);

            rat = 0;
        }
    }

    @Override
    public int getItemCount() {



        return list.size();
    }

    private class loadImage extends AsyncTask<Void, Void, Void> {

        String url;
        ImageView iv;
        Bitmap d;

        loadImage(ImageView iv, String url) {
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
        RatingBar ratingBar;


        RecycleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView)itemView.findViewById(R.id.prodName);
            priceTextView = (TextView) itemView.findViewById(R.id.prodPrice);
            imageView = (ImageView)itemView.findViewById(R.id.prodImage);
            switcher = (Switch) itemView.findViewById(R.id.switcher);
            addlist = (TextView)itemView.findViewById(R.id.addlist);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rraatteerr);


        }



        @Override
        public void onClick(View v) {

            Model item = list.get(getAdapterPosition());

            SingleProductFragment frag3 = new SingleProductFragment();
            Bundle b = new Bundle();

            b.putString("id" , String.valueOf(item.getId()));

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
