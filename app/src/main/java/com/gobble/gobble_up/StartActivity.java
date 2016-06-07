package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    private Timer t;
    private String LOG_IN = "http://nationproducts.in/global/api/login";
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    String imageUrl = null;
    String nn = null;
    String pp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        pref = getSharedPreferences("MySignin", Context.MODE_PRIVATE);
        edit = pref.edit();

        nn = pref.getString("email",null);
        pp = pref.getString("pass",null);
        imageUrl = pref.getString("image" , null);

        if ( (nn!=null) && (pp!=null)  )
        {
            new login(nn , pp).execute();
        }


else
        {
            t=new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, 2000);
        }





    }

    public class login extends AsyncTask<Void , Void , Void>
    {

        String username , password;
        String result;
        String name , email;
        String idd;

        public login(String username , String password)
        {
            this.username = username;
            this.password = password;
        }










        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("user_email" , username));
            data.add(new BasicNameValuePair("password" , password));

            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(LOG_IN , data);

            try {
                JSONObject obj = new JSONObject(result);
                name = obj.getString("user_name");
                email = obj.getString("user_email");
                idd = obj.getString("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                // Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {




            if(name!=null && email!=null)
            {


                Toast.makeText(getApplicationContext() , "welcome "+email , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext() , GetStartActivity.class);
                if (imageUrl!=null)
                {
                    i.putExtra("url" , imageUrl);
                }
                i.putExtra("id" , idd);
                i.putExtra("name" , name);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext() , "Invalid Email Id or Password" , Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(aVoid);
        }
    }

}
