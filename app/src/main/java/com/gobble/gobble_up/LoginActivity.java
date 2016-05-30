package com.gobble.gobble_up;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private String LOG_IN = "http://nationproducts.in/global/api/login";

    EditText email , password;

    TextView forgot;
    Button sign_in , sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.et_Email);
        password = (EditText)findViewById(R.id.et_Password);

        forgot = (TextView)findViewById(R.id.tv_userforgotpassword);

        sign_in = (Button)findViewById(R.id.bt_signin);
        sign_up = (Button)findViewById(R.id.bt_creataccount);





        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();

ConnectionDetector cd = new ConnectionDetector(getBaseContext());

                if(cd.isConnectingToInternet())
                {
                    new login(mail , pass).execute();
                }
                else
                {
                    Toast.makeText(getBaseContext() , "No internet Connection" , Toast.LENGTH_SHORT).show();
                }

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext() , Register.class);
                startActivity(i);
            }
        });



    }
    public class login extends AsyncTask<Void , Void , Void>
    {

        String username , password;
        String result;
        String name , email;

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

            Log.d("asdasdasd" , result);


            if(name!=null && email!=null)
            {


                Toast.makeText(getApplicationContext() , "welcome "+email , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext() , GetStartActivity.class);
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
