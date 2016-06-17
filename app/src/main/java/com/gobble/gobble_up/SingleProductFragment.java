package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.Toast;

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

/**
 * Created by hi on 10-06-2016.
 */
public class SingleProductFragment extends Fragment implements View.OnClickListener {
    ArrayList<bean> list;
    RecyclerView lv;
    ImageView iv;
    TextView title;
    private GridLayoutManager lLayout;
    Button add , compare;
    String iidd;
    List<String> nutrition;
    PieChart pieChart;
    private String GET_PRODUCT = "http://nationproducts.in/global/api/product/id/";
    BarChart barChart;

    RelativeLayout bar;

    TextView sliderName , sliderBelowText , calories , fat , carbs , protein , sodium , potassium , fiber , sugar , vita , vitc , calcium , iron;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.single_prod_main , container , false);

        add = (Button)view.findViewById(R.id.addtolist);
        title = (TextView)view.findViewById(R.id.title_single);

        nutrition = new ArrayList<>();
        pieChart = (PieChart)view.findViewById(R.id.pie);
        pieChart.setUsePercentValues(true);

        compare = (Button)view.findViewById(R.id.addtocompare);
        barChart = (BarChart)view.findViewById(R.id.bar_chart);



        //pieChart.setDrawHoleEnabled(true);
        //pieChart.setHoleColorTransparent(true);
        //pieChart.setHoleRadius(7);
        //pieChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        //pieChart.setRotationAngle(0);
        //pieChart.setRotationEnabled(true);

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

        View bottom = (View)view.findViewById(R.id.bottom_sheet);

bar = (RelativeLayout)((MainActivity)getContext()).findViewById(R.id.bottombar);


        mBottomSheetBehavior = BottomSheetBehavior.from(bottom);
        mBottomSheetBehavior.setPeekHeight(90);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);



        //TextView openSlider = (TextView)view.findViewById(R.id.open_slider);



      //  openSlider.setOnClickListener(new View.OnClickListener() {
       //     @Override
    //        public void onClick(View v) {
      //          mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    //        }
   //     });


        lLayout = new GridLayoutManager(getContext() , 1);

        iidd = getArguments().getString("id");


        final comparebean b = (comparebean)getContext().getApplicationContext();

        //title.setText(a);
        iv = (ImageView)view.findViewById(R.id.prodImage1);
        lv = (RecyclerView)view. findViewById(R.id.prodList1);

        new loadImage(iv , getArguments().getString("image")).execute();


        list = new ArrayList<>();

        new connect(GET_PRODUCT+iidd).execute();

       // list.add(new bean("price" , price));
       // list.add(new bean("description" , desc));


        for (int i = 0 ; i<b.list.size() ; i++)
        {



            if (b.list.get(i).getId() == Integer.parseInt(iidd))
            {
                compare.setBackground(getResources().getDrawable(R.drawable.dark));

            }

        }

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int flag = 0;
                for (int i = 0 ; i<b.list.size() ; i++)
                {
                    if (b.list.get(i).getId() == Integer.parseInt(iidd))
                    {
                       flag = 1;
                    }
                }

                if (flag == 1)
                {
                    Toast.makeText(getContext() , "Already added to compare" , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ProductBean item = new ProductBean();
                    if (b.list.size() < 4)
                    {
                        item.setId(Integer.parseInt(iidd));
                        b.list.add(item);
                        b.comparecount++;
                        item.setSet(true);
                        compare.setBackground(getResources().getDrawable(R.drawable.dark));
                        //Toast.makeText(getContext() , "Added to compare" , Toast.LENGTH_SHORT).show();

                        if (bar.getVisibility() == View.GONE)
                        {
                            TranslateAnimation animate = new TranslateAnimation(0,0,bar.getHeight(),0);
                            animate.setDuration(500);
                            animate.setFillAfter(true);
                            bar.startAnimation(animate);
                            bar.setVisibility(View.VISIBLE);
                        }



                        TextView comp = (TextView)((MainActivity)getContext()).findViewById(R.id.barcompare);
                        comp.setText("Compare " + String.valueOf(b.list.size()));

                        //bar.animate().alpha(1.0f);
                        // b.bitmaps.add(LoadImageFromURL(item.getImage()));
                        //Log.d("asdasdasd" , String.valueOf(b.list.size()));
                        Toast.makeText(getContext() , "Added to Compare" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext() , "Max. limit reached" , Toast.LENGTH_SHORT).show();

                    }








                }
            }
        });






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
                if (bar.getVisibility() == View.GONE)
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
        String prie , desc , nae;
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
                desc = object.getString("description");
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
            list.add(new bean("price" , prie));
            list.add(new bean("description" , desc));

            singleAdapter2 adapter = new singleAdapter2(getContext() ,list);
            lv.setHasFixedSize(true);
            lv.setLayoutManager(lLayout);
            lv.setAdapter(adapter);

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

            float fatt = Float.parseFloat(faat);
            float proo = Float.parseFloat(pro);
            float carbb = Float.parseFloat(carb);




            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(fatt , 0));
            entries.add(new Entry(carbb , 1));
            entries.add(new Entry(proo , 2));

            PieDataSet dataset = new PieDataSet(entries, null);
            dataset.setSliceSpace(3);
            dataset.setSelectionShift(5);

            dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("Fat");
            labels.add("Carbohydrates");
            labels.add("Protein");

            PieData data = new PieData(labels, dataset);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            pieChart.setData(data);
            pieChart.setDescription("Nutrition");
            pieChart.animate();

            pieChart.invalidate();



            ArrayList<BarEntry> entries1 = new ArrayList<>();
            entries1.add(new BarEntry(fatt , 0));
            entries1.add(new BarEntry(carbb , 1));
            entries1.add(new BarEntry(proo , 2));

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



}
