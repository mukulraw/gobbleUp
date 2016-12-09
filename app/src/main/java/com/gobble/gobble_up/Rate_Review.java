package com.gobble.gobble_up;


import android.app.Dialog;
import android.os.AsyncTask;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.plus.model.people.Person;

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
    private EditText comment;
    ArrayList<RateBean> list;
    RateAdapter adapter;

    private float rater;
    TextView rateCount;
    private String commenter;
    RatingBar rate;
    private String iidd;
    TextView fab;
    private RecyclerView getRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate__review);
        pId = getIntent().getExtras().getString("iidd");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // rate = (RatingBar) findViewById(R.id.rate);
        //comment = (EditText) findViewById(R.id.comment);
        //Button submitRate = (Button) findViewById(R.id.submit_rate);

        getRatings = (RecyclerView)findViewById(R.id.allcomments);

       // rateCount = (TextView)findViewById(R.id.ratecount);

        GridLayoutManager lLayout = new GridLayoutManager(this, 1);

        fab = (TextView) findViewById(R.id.fab);

        getRatings.setHasFixedSize(true);
        getRatings.setLayoutManager(lLayout);


        RecyclerView.ItemDecoration itemDecoration = new
                SimpleDividerItemDecoration(this, SimpleDividerItemDecoration.VERTICAL_LIST);


        getRatings.addItemDecoration(itemDecoration);


        list = new ArrayList<>();
       refresh();




        final comparebean be = (comparebean)this.getApplicationContext();

          iidd = be.user_id;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(Rate_Review.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.comment_dialog);
                dialog.setCancelable(true);
                dialog.show();
                ImageButton tick = (ImageButton)dialog.findViewById(R.id.comment_dialog_right);
                //ImageButton cross = (ImageButton)dialog.findViewById(R.id.comment_dialog_close);
                final EditText comment = (EditText)dialog.findViewById(R.id.dialog_comment);
                RatingBar raterr = (RatingBar)dialog.findViewById(R.id.comment_dialog_rating);
                final TextView count = (TextView)dialog.findViewById(R.id.comment_dialog_count);


                raterr.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        rater = ratingBar.getRating();
                        count.setText(String.valueOf(ratingBar.getRating()));
                    }
                });

                tick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commenter = comment.getText().toString();
                        if (rater>0)
                        {
                            new login().execute();
                            refresh();
                            dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(getBaseContext() , "Please add a rating" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


/*        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rater = ratingBar.getRating();
                rateCount.setText(Float.toString(ratingBar.getRating()));
            }
        });
*/

        /*submitRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                commenter = comment.getText().toString();




                if (rater>0)
                {
                    new login().execute();
                }
                else
                {
                    Toast.makeText(getBaseContext() , "Please add a rating" , Toast.LENGTH_SHORT).show();
                }


            }
        });
        */


    }

    void refresh()
    {
        list.clear();
        String GET_REVIEWS = "http://nationproducts.in/global/api/productreviews/id/";
        String url = GET_REVIEWS +pId;
        new connect(url).execute();
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
            String COMMENT = "http://nationproducts.in/global/api/productreview";
            result = ruc.sendPostRequest(COMMENT, data);

            Log.d("asdasdasd" , result);



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
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                // Log.e("JSON Parser", "Error parsing data " + e.toString());
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
