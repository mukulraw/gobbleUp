package com.gobble.gobble_up;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Rate_Review extends AppCompatActivity {

    private String pId;
    private RatingBar rate;
    private EditText comment;
    private Button submitRate;

    private String COMMENT = "http://nationproducts.in/global/api/productreview";

    private int rater;
    private String commenter;
    private String iidd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate__review);
        pId = getIntent().getExtras().getString("iidd");

        rate = (RatingBar)findViewById(R.id.rate);
        comment = (EditText) findViewById(R.id.comment);
        submitRate = (Button)findViewById(R.id.submit_rate);

        final comparebean be = (comparebean)this.getApplicationContext();

          iidd = be.user_id;

        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rater = ratingBar.getNumStars();
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

}
