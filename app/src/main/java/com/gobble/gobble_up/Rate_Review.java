package com.gobble.gobble_up;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class Rate_Review extends AppCompatActivity {

    private String pId;
    private RatingBar rate;
    private EditText comment;
    private Button submitRate;
    private GridLayoutManager lLayout;
    private String COMMENT = "http://nationproducts.in/global/api/productreview";
    private String GET_REVIEWS = "http://nationproducts.in/global/api/productreviews/id/";
    ArrayList<RateBean> list;
    RateAdapter adapter;

    private float rater;
    private String commenter;
    private String iidd;
    RecyclerView getRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate__review);
        pId = getIntent().getExtras().getString("iidd");

        rate = (RatingBar)findViewById(R.id.rate);
        comment = (EditText) findViewById(R.id.comment);
        submitRate = (Button)findViewById(R.id.submit_rate);

        getRatings = (RecyclerView)findViewById(R.id.allcomments);

        lLayout = new GridLayoutManager(this, 1);


        getRatings.setHasFixedSize(true);
        getRatings.setLayoutManager(lLayout);



        list = new ArrayList<>();
        list.clear();
        String url = GET_REVIEWS+pId;
        new connect(url).execute();




        final comparebean be = (comparebean)this.getApplicationContext();

          iidd = be.user_id;

        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rater = ratingBar.getRating();
            }
        });


        submitRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                commenter = comment.getText().toString();
                new login().execute();
            }
        });


    }

    private class login extends AsyncTask<Void , Void , Void>
    {


        String result;
        String name;












        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("userId" , iidd));
            data.add(new BasicNameValuePair("productId" , pId));
            data.add(new BasicNameValuePair("rating" , String.valueOf(rater)));
            data.add(new BasicNameValuePair("review" , commenter));

            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(COMMENT , data);

            Log.d("asdasdasd" , result);

        //    try {
        //        JSONObject obj = new JSONObject(result);
       //         name = obj.getString("user_name");
       //         email = obj.getString("user_email");
       //         idd = obj.getString("user_id");
       //     } catch (JSONException e) {
      // //         e.printStackTrace();
      //      }catch (NullPointerException e)
      //      {
       //         e.printStackTrace();
                // Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
       //     }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public class connect extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;
        comparebean b;
        int length;
        String url;


        connect(String url)
        {
            this.url = url;
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
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("JSON Parser", "Error parsing data " + e.toString());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                // Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }







            for (int i=0 ; i<length;i++)
            {

                try {
                    JSONObject obj = array.getJSONObject(i);
                    RateBean bean = new RateBean();
                    bean.setUsername(obj.getString("userName"));
                    bean.setDate(obj.getString("createdTime"));
                    bean.setRating(obj.getString("rating"));
                    bean.setReview(obj.getString("review"));





                    list.add(bean);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // adapter.setGridData(list1);



            adapter = new RateAdapter(getApplicationContext() , list);

            adapter.setGridData(list);
            getRatings.setAdapter(adapter);
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);

        }
    }



}
