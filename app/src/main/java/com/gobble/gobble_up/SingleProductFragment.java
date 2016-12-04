package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gobble.gobble_up.POJO.CompareModel;
import com.gobble.gobble_up.POJO.Model;
import com.gobble.gobble_up.POJO.ReviewModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SingleProductFragment extends Fragment implements View.OnClickListener {
    //ArrayList<CompareModel> list;

    ImageView iv;
    private TextView title , siz;
    private Button add , compare;
    private LinearLayout clickRating;
    private String iidd;
    RatingBar ratrr;

    TextView margin;

    ShowcaseView showcaseView;



    Target target;



    private List<String> nutrition;

    private TextView brand , price_single , calories_single , description ;
    private String GET_REVIEWS = "http://nationproducts.in/global/api/productreviews/id/";
    private BarChart barChart;
    String pId;
    ProgressBar barr;


    ScrollView scroller;

    TextView rate;
    private BottomSheetBehavior bar;

    private TextView sliderName , sliderBelowText , calories , fat , carbs , protein , sodium , potassium , fiber , sugar , vita , vitc , calcium , iron;

    private BottomSheetBehavior mBottomSheetBehavior;

    LinearLayout paddingView;

    float rat;
    TextView head;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.single_prod_main , container , false);




        margin = (TextView)view.findViewById(R.id.margin);


        add = (Button)view.findViewById(R.id.addtolist);
        title = (TextView)view.findViewById(R.id.title_single);
        siz = (TextView)view.findViewById(R.id.title_size);

        paddingView = (LinearLayout)view.findViewById(R.id.padding_view);

        nutrition = new ArrayList<>();


        clickRating = (LinearLayout)view.findViewById(R.id.click_rating);

        ratrr = (RatingBar)view.findViewById(R.id.ratrrr);

        barr = (ProgressBar)view.findViewById(R.id.bbaarr);

        compare = (Button)view.findViewById(R.id.addtocompare);
        barChart = (BarChart)view.findViewById(R.id.bar_chart);



        scroller = (ScrollView)view.findViewById(R.id.scroller);


        brand = (TextView)view.findViewById(R.id.brand);
        price_single = (TextView)view.findViewById(R.id.price);
        calories_single = (TextView)view.findViewById(R.id.calories_single);
        description = (TextView)view.findViewById(R.id.description);

        head = (TextView)view.findViewById(R.id.bottom_bar_heading);



        sliderName = (TextView)view.findViewById(R.id.slider_name);
        sliderBelowText = (TextView)view.findViewById(R.id.below_text);
        calories = (TextView)view.findViewById(R.id.calories);
        fat = (TextView)view.findViewById(R.id.fat);
        carbs = (TextView)view.findViewById(R.id.carbs);
        protein = (TextView)view.findViewById(R.id.protein);
        sodium = (TextView)view.findViewById(R.id.sodium);
        potassium = (TextView)view.findViewById(R.id.potassium);
        fiber = (TextView)view.findViewById(R.id.fiber);
        sugar = (TextView)view.findViewById(R.id.sugar);
        vita = (TextView)view.findViewById(R.id.vita);
        vitc = (TextView)view.findViewById(R.id.vitc);
        calcium = (TextView)view.findViewById(R.id.calcium);
        iron = (TextView)view.findViewById(R.id.iron);


        rate = (TextView)view.findViewById(R.id.rating);


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)((MainActivity)getContext()).findViewById(R.id.coordinate);

   View view1 = coordinatorLayout.findViewById(R.id.bottombar);

        bar = BottomSheetBehavior.from(view1);

        View bottom = (View)view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottom);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);









        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
                else
                {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }


            }
        });


        ViewTreeObserver vto = head.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver vto2 = margin.getViewTreeObserver();
                vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mBottomSheetBehavior.setPeekHeight(head.getHeight() + margin.getHeight());
                        scroller.setPadding(0 , 0 , 0 , head.getHeight() + margin.getHeight());
                        head.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        margin.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });


            }
        });









        GridLayoutManager lLayout = new GridLayoutManager(getContext(), 1);

        iidd = getArguments().getString("id");





        final comparebean b = (comparebean)getContext().getApplicationContext();

        //title.setText(a);
        iv = (ImageView)view.findViewById(R.id.prodImage1);












        add.setText("ADD TO BASKET");
        add.setBackground(getResources().getDrawable(R.drawable.grey));
        for (int k = 0 ; k<b.tempList.size() ; k++)
        {
            if (Objects.equals(iidd, b.tempList.get(k).getId()))
            {
                add.setText("ADDED");
                add.setBackground(getResources().getDrawable(R.drawable.dark));
            }

        }


      //  new loadImage(iv , getArguments().getString("image")).execute();

        barr.setVisibility(View.VISIBLE);

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(false).build();


        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(getArguments().getString("image") , iv , options);


       // list = new ArrayList<>();

        String GET_PRODUCT = "http://nationproducts.in/global/api/product/id/";



        //new connect(GET_PRODUCT +iidd).execute();


        fetch(iidd);




       // list.add(new bean("price" , price));
       // list.add(new bean("description" , desc));




        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int flag = 0;
                for (int i = 0 ; i<b.list.size() ; i++)
                {
                    if (Objects.equals(b.list.get(i).getId(), iidd))
                    {
                       flag++;
                    }
                }

                if (flag > 0)
                {
                 //   Toast.makeText(getContext() , "Already added to compare" , Toast.LENGTH_SHORT).show();



                    int index = 0;
                    // Log.d("asdasdasd" , item.getName());
                    int l = b.list.size();


                    if (l == 1)
                    {
                        TextView comp1 = (TextView)((MainActivity)getContext()).findViewById(R.id.textView5);

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
                        if (Objects.equals(iidd, b.list.get(i).getId()))
                        {
                            index = i;
                        }
                    }


                    b.list.remove(index);

                    b.comparecount--;
                    TextView comp = (TextView)((MainActivity)getContext()).findViewById(R.id.textView4);
                    comp.setText(String.valueOf(b.list.size()));
                    //b.list.remove(item);


                    compare.setBackground(getResources().getDrawable(R.drawable.grey));



                    Toast.makeText(getContext() , "Removed from compare" , Toast.LENGTH_SHORT).show();





                }
                else
                {
                    Model item = new Model();
                    if (b.list.size() < 4)
                    {
                        item.setId(iidd);
                        b.list.add(item);
                        b.comparecount++;
                        compare.setBackground(getResources().getDrawable(R.drawable.dark));
                        //Toast.makeText(getContext() , "Added to compare" , Toast.LENGTH_SHORT).show();

                        if (bar.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                        {

                            bar.setState(BottomSheetBehavior.STATE_EXPANDED);

                        }



                        TextView comp = (TextView)((MainActivity)getContext()).findViewById(R.id.textView4);
                        if (comp != null) {
                            comp.setText(String.valueOf(b.list.size()));
                        }

                        Toast.makeText(getContext() , "Added to Compare" , Toast.LENGTH_SHORT).show();

                        //bar.animate().alpha(1.0f);
                        // b.bitmaps.add(LoadImageFromURL(item.getImage()));
                        //Log.d("asdasdasd" , String.valueOf(b.list.size()));
                    }
                    else {
                        Toast.makeText(getContext() , "Max. limit reached" , Toast.LENGTH_SHORT).show();

                    }








                }
            }
        });






        add.setOnClickListener(this);



        clickRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext() , Rate_Review.class);
                Bundle bundle = new Bundle();
                bundle.putString("iidd" , iidd);
                i.putExtras(bundle);
                startActivity(i);

            }
        });





        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext() , Rate_Review.class);
                Bundle bundle = new Bundle();
                bundle.putString("iidd" , iidd);
                i.putExtras(bundle);
                startActivity(i);

            }
        });



        //fetch();

        return view;
    }








    public void fetch(String iid)
    {
        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CompareAPI request = retrofit.create(CompareAPI.class);
        Call<ArrayList<CompareModel>> call = request.getBooks(iid);

        call.enqueue(new Callback<ArrayList<CompareModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CompareModel>> call, Response<ArrayList<CompareModel>> response) {


                String faat = response.body().get(0).getNutration().get(1).getValue();
                String pro = response.body().get(0).getNutration().get(3).getValue();
                String carb = response.body().get(0).getNutration().get(2).getValue();

                title.setText(response.body().get(0).getName());
                siz.setText(response.body().get(0).getSize());
                //list.add(new bean("price" , prie);
                //list.add(new bean("description" , desc));




                calories_single.setText(response.body().get(0).getNutration().get(0).getValue());
                brand.setText(response.body().get(0).getBrand());
                description.setText(response.body().get(0).getDescription());
                price_single.setText(response.body().get(0).getPrice());



                sliderName.setText(response.body().get(0).getName());
                sliderBelowText.setText("There are "+ response.body().get(0).getNutration().get(0).getValue()+ " in a 100g serving of "+ response.body().get(0).getName()+".\n"+"Calorie breakdown: "+response.body().get(0).getNutration().get(1).getValue() + " g" +" fat, "+response.body().get(0).getNutration().get(2).getValue()+" g carbs, "+response.body().get(0).getNutration().get(3).getValue()+" g protein.");

                calories.setText(response.body().get(0).getNutration().get(0).getValue().toString());
                fat.setText(response.body().get(0).getNutration().get(1).getValue().toString() + " g");
                carbs.setText(response.body().get(0).getNutration().get(2).getValue().toString() + " g");
                protein.setText(response.body().get(0).getNutration().get(3).getValue().toString() + " g");
                sodium.setText(response.body().get(0).getNutration().get(4).getValue().toString() + " mg");
                potassium.setText(response.body().get(0).getNutration().get(5).getValue().toString() + " mg");
                fiber.setText(response.body().get(0).getNutration().get(6).getValue().toString() + " g");
                sugar.setText(response.body().get(0).getNutration().get(7).getValue().toString() + " g");
                vita.setText(response.body().get(0).getNutration().get(8).getValue().toString() + " %");
                vitc.setText(response.body().get(0).getNutration().get(9).getValue().toString() + " %");
                calcium.setText(response.body().get(0).getNutration().get(10).getValue().toString() + " %");
                iron.setText(response.body().get(0).getNutration().get(11).getValue().toString() + " %");

































                float calo = Float.parseFloat(response.body().get(0).getNutration().get(0).getValue());
                float fatt = Float.parseFloat(response.body().get(0).getNutration().get(1).getValue());
                float proo = Float.parseFloat(response.body().get(0).getNutration().get(3).getValue());
                float carbb = Float.parseFloat(response.body().get(0).getNutration().get(2).getValue());
                float sodi = Float.parseFloat(response.body().get(0).getNutration().get(4).getValue());
                float pota = Float.parseFloat(response.body().get(0).getNutration().get(5).getValue());
                float fibe = Float.parseFloat(response.body().get(0).getNutration().get(6).getValue());
                float suga = Float.parseFloat(response.body().get(0).getNutration().get(7).getValue());
                float vitaa = Float.parseFloat(response.body().get(0).getNutration().get(8).getValue());
                float vitac = Float.parseFloat(response.body().get(0).getNutration().get(9).getValue());
                float calc = Float.parseFloat(response.body().get(0).getNutration().get(10).getValue());
                float iro = Float.parseFloat(response.body().get(0).getNutration().get(11).getValue());




                ArrayList<Entry> entries = new ArrayList<>();

                entries.add(new Entry(calo , 0));
                entries.add(new Entry(fatt , 1));
                entries.add(new Entry(carbb , 2));
                entries.add(new Entry(proo , 3));
                entries.add(new Entry(sodi , 4));
                entries.add(new Entry(pota , 5));
                entries.add(new Entry(fibe , 6));
                entries.add(new Entry(suga , 7));
                entries.add(new Entry(vitaa , 8));
                entries.add(new Entry(vitac , 9));
                entries.add(new Entry(calc , 10));
                entries.add(new Entry(iro , 11));




                ArrayList<String> labels = new ArrayList<String>();

                labels.add("Calories");
                labels.add("Fat");
                labels.add("Carbohydrates");
                labels.add("Protein");
                labels.add("Sodium");
                labels.add("Potassium");
                labels.add("Fiber");
                labels.add("Sugar");
                labels.add("Vitamin A");
                labels.add("Vitamin C");
                labels.add("Calcium");
                labels.add("Iron");










                ArrayList<BarEntry> entries1 = new ArrayList<>();
                entries1.add(new BarEntry(calo , 0));
                entries1.add(new BarEntry(fatt , 1));
                entries1.add(new BarEntry(carbb , 2));
                entries1.add(new BarEntry(proo , 3));
                entries1.add(new BarEntry(sodi , 4));
                entries1.add(new BarEntry(pota , 5));
                entries1.add(new BarEntry(fibe , 6));
                entries1.add(new BarEntry(suga , 7));
                entries1.add(new BarEntry(vitaa , 8));
                entries1.add(new BarEntry(vitac , 9));
                entries1.add(new BarEntry(calc , 10));
                entries1.add(new BarEntry(iro , 11));

                BarDataSet barDataSet = new BarDataSet(entries1 , null);

                barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                BarData data1 = new BarData(labels ,  barDataSet);
                //data1.setValueFormatter(new PercentFormatter());
                data1.setValueTextSize(11f);
                data1.setValueTextColor(Color.BLACK);
                barChart.setData(data1);
                barChart.animate();
                barChart.setDescription("");
                barChart.invalidate();





            }

            @Override
            public void onFailure(Call<ArrayList<CompareModel>> call, Throwable t) {
                //Log.d("Error",t.getMessage());
            }
        });






    }





    @Override
    public void onResume() {
        super.onResume();

        //CoordinatorLayout coordinatorLayout = (CoordinatorLayout)((MainActivity)getContext()).findViewById(R.id.coordinate);

        //View view1 = coordinatorLayout.findViewById(R.id.bottombar);

        //bar = BottomSheetBehavior.from(view1);

        final comparebean b = (comparebean)getContext().getApplicationContext();

        add.setText("ADD TO BASKET");
        add.setBackground(getResources().getDrawable(R.drawable.grey));

        for (int k = 0 ; k<b.tempList.size() ; k++)
        {
            if (Objects.equals(iidd, b.tempList.get(k).getId()))
            {
                add.setText("ADDED");
                add.setBackground(getResources().getDrawable(R.drawable.dark));

                bar.setState(BottomSheetBehavior.STATE_EXPANDED);
                TextView comp = (TextView)((MainActivity)getActivity()).findViewById(R.id.textView5);
                if (comp != null) {
                    comp.setText(String.valueOf(b.tempList.size()));
                }


            }

        }


        compare.setBackground(getResources().getDrawable(R.drawable.grey));

        for (int i = 0 ; i<b.list.size() ; i++)
        {



            if (Objects.equals(b.list.get(i).getId(), iidd))
            {
                compare.setBackground(getResources().getDrawable(R.drawable.dark));
                bar.setState(BottomSheetBehavior.STATE_EXPANDED);
                TextView comp = (TextView)((MainActivity)getContext()).findViewById(R.id.textView4);
                if (comp != null) {
                    comp.setText(String.valueOf(b.list.size()));
                }
            }

        }


        //rat = 0;

        //new connect2(GET_REVIEWS + iidd).execute();

        fetch2(iidd);




    }

    private Bitmap LoadImageFromURL(String url)

    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            return BitmapFactory.decodeStream(is);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        comparebean b = (comparebean)getActivity().getApplicationContext();
        if (v == add)
        {


            if (!add.getText().equals("ADDED"))
            {
                Model item = new Model();
                item.setId(iidd);
                b.tempList.add(item);
                b.comparecount++;
                add.setText("ADDED");
                add.setBackground(getResources().getDrawable(R.drawable.dark));
                if (bar.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                {
                  //  TranslateAnimation animate = new TranslateAnimation(0,0,bar.getHeight(),0);
                   // animate.setDuration(500);
                   // animate.setFillAfter(true);
                   // bar.startAnimation(animate);
                   // bar.setVisibility(View.VISIBLE);

                    bar.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                TextView comp = (TextView)((MainActivity)getActivity()).findViewById(R.id.textView5);
                if (comp != null) {
                    comp.setText(String.valueOf(b.tempList.size()));
                }

                Toast.makeText(getContext() , "Added in Basket" , Toast.LENGTH_SHORT).show();

            }
            else
            {


                int index = 0;
                int l = b.tempList.size();
                for (int i = 0 ; i<l ; i++)
                {
                    if (Objects.equals(iidd, b.tempList.get(i).getId()))
                    {
                        index = i;
                    }
                }


                b.tempList.remove(index);
                b.comparecount--;



                TextView comp = (TextView)((MainActivity)getContext()).findViewById(R.id.textView5);
                comp.setText(String.valueOf(b.tempList.size()));

                if (l == 1)
                {
                    TextView comp1 = (TextView)((MainActivity)getContext()).findViewById(R.id.textView4);

                    if (comp1.getText().equals("0"))
                    {
                        if (bar.getState() == BottomSheetBehavior.STATE_EXPANDED)
                        {

                            bar.setState(BottomSheetBehavior.STATE_COLLAPSED);

                        }
                    }




                }


                add.setText("ADD TO BASKET");
                add.setBackground(getResources().getDrawable(R.drawable.grey));

                Toast.makeText(getContext() , "Removed from Basket" , Toast.LENGTH_SHORT).show();


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


          //  Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try
            {
                Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext() , R.anim.fade);
                iv.startAnimation(animation);
                iv.setImageBitmap(d);
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }


        }
    }

    public class connect extends AsyncTask<Void , Void , Void>
    {





        InputStream is;
        String json;
        JSONObject object;
        String prie , desc , nae , bran = "" , size="";
        JSONArray mainArray , nutArray;
        String faat , pro , carb;



        int length;
        String url;

        connect(String url)
        {
            this.url = url;
        }




        @Override
        protected Void doInBackground(Void... params) {


            try {
                // HttpClient client = new DefaultHttpClient();
                //  HttpGet get = new HttpGet(url);
                //  HttpResponse response = client.execute(get);
                //HttpEntity entity = response.getEntity();
                //is = entity.getContent();

                URL u = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)u.openConnection();
                if(connection.getResponseCode()==200)
                {
                    is = connection.getInputStream();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                mainArray = new JSONArray(json);
                //length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            try {
                object = mainArray.getJSONObject(0);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }


            try {
                prie = object.getString("price");
                nae = object.getString("name");
                size = object.getString("size");
                pId = object.getString("id");
                desc = object.getString("description");
                bran = object.getString("brand");
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }


            try {
                nutArray = object.getJSONArray("nutration");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            length = nutArray.length();

            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = nutArray.getJSONObject(i);

                    if (obj.getString("unit").equals("per"))
                    {
                        nutrition.add(obj.getString("value") + "%");
                    }

                    else
                    {
                        nutrition.add(obj.getString("value") +  obj.getString("unit"));
                    }



                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            try {
                faat = nutArray.getJSONObject(1).getString("value");
                pro = nutArray.getJSONObject(3).getString("value");
                carb = nutArray.getJSONObject(2).getString("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            title.setText(nae);
            siz.setText(size);
           // list.add(new bean("price" , prie));
           // list.add(new bean("description" , desc));




            calories_single.setText(nutrition.get(0));
            brand.setText(bran);
            description.setText(desc);
            price_single.setText(prie);



            sliderName.setText(nae);
            sliderBelowText.setText("There are "+ nutrition.get(0)+ " in a 100g serving of "+ nae+".\n"+"Calorie breakdown: "+nutrition.get(1)+" fat, "+nutrition.get(2)+" carbs, "+nutrition.get(3)+" protein.");

            calories.setText(nutrition.get(0));
            fat.setText(nutrition.get(1));
            carbs.setText(nutrition.get(2));
            protein.setText(nutrition.get(3));
            sodium.setText(nutrition.get(4));
            potassium.setText(nutrition.get(5));
            fiber.setText(nutrition.get(6));
            sugar.setText(nutrition.get(7));
            vita.setText(nutrition.get(8));
            vitc.setText(nutrition.get(9));
            calcium.setText(nutrition.get(10));
            iron.setText(nutrition.get(11));



            float calo = Float.parseFloat(nutrition.get(0));
            float fatt = Float.parseFloat(nutrition.get(1));
            float proo = Float.parseFloat(nutrition.get(3));
            float carbb = Float.parseFloat(nutrition.get(2));
            float sodi = Float.parseFloat(nutrition.get(4));
            float pota = Float.parseFloat(nutrition.get(5));
            float fibe = Float.parseFloat(nutrition.get(6));
            float suga = Float.parseFloat(nutrition.get(7));
            float vitaa = Float.parseFloat(nutrition.get(8));
            float vitac = Float.parseFloat(nutrition.get(9));
            float calc = Float.parseFloat(nutrition.get(10));
            float iro = Float.parseFloat(nutrition.get(11));




            ArrayList<Entry> entries = new ArrayList<>();

                entries.add(new Entry(calo , 0));
                entries.add(new Entry(fatt , 1));
                entries.add(new Entry(carbb , 2));
                entries.add(new Entry(proo , 3));
                entries.add(new Entry(sodi , 4));
                entries.add(new Entry(pota , 5));
                entries.add(new Entry(fibe , 6));
                entries.add(new Entry(suga , 7));
                entries.add(new Entry(vitaa , 8));
                entries.add(new Entry(vitac , 9));
                entries.add(new Entry(calc , 10));
                entries.add(new Entry(iro , 11));




            ArrayList<String> labels = new ArrayList<String>();

                labels.add("Calories");
                labels.add("Fat");
                labels.add("Carbohydrates");
                labels.add("Protein");
                labels.add("Sodium");
                labels.add("Potassium");
                labels.add("Fiber");
                labels.add("Sugar");
                labels.add("Vitamin A");
                labels.add("Vitamin C");
                labels.add("Calcium");
                labels.add("Iron");










            ArrayList<BarEntry> entries1 = new ArrayList<>();
            entries1.add(new BarEntry(calo , 0));
            entries1.add(new BarEntry(fatt , 1));
            entries1.add(new BarEntry(carbb , 2));
            entries1.add(new BarEntry(proo , 3));
            entries1.add(new BarEntry(sodi , 4));
            entries1.add(new BarEntry(pota , 5));
            entries1.add(new BarEntry(fibe , 6));
            entries1.add(new BarEntry(suga , 7));
            entries1.add(new BarEntry(vitaa , 8));
            entries1.add(new BarEntry(vitac , 9));
            entries1.add(new BarEntry(calc , 10));
            entries1.add(new BarEntry(iro , 11));

            BarDataSet barDataSet = new BarDataSet(entries1 , null);

            barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            BarData data1 = new BarData(labels ,  barDataSet);
            //data1.setValueFormatter(new PercentFormatter());
            data1.setValueTextSize(11f);
            data1.setValueTextColor(Color.BLACK);
            barChart.setData(data1);
            barChart.animate();
            barChart.setDescription("");
            barChart.invalidate();


        }
    }






    public void fetch2(final String url)
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

                ratrr.setRating(rat/response.body().size());


                scroller.setVisibility(View.VISIBLE);

                barr.setVisibility(View.GONE);

                Log.d("sdfjnolisdfsf" , String.valueOf(url));

                rat = 0;




            }

            @Override
            public void onFailure(Call<ArrayList<ReviewModel>> call, Throwable t) {

            }
        });



    }



}
