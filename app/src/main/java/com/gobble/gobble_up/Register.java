package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Register extends AppCompatActivity {


    EditText name , email , password , retype;
    Button register;
    private String SIGN_UP = "http://nationproducts.in/global/api/register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.et_username);
        email = (EditText)findViewById(R.id.et_Email_id);
        password = (EditText)findViewById(R.id.et_pass);
        retype = (EditText)findViewById(R.id.et_re_type_password);

        register = (Button)findViewById(R.id.bt_submit);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectionDetector cd = new ConnectionDetector(getBaseContext());
                if(cd.isConnectingToInternet())
                {
                    String mail = email.getText().toString();
                    String nam = name.getText().toString();
                    String pass = password.getText().toString();
                    String retpe = retype.getText().toString();

                    if(nam.length()==0){
                        Toast.makeText(Register.this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
                    }else if(mail.length()==0){
                        Toast.makeText(Register.this,"Please Enter Your Email.",Toast.LENGTH_SHORT).show();
                    }else if(!Util.emailValidater(mail)){
                        Toast.makeText(Register.this,"Please Enter a Valid Email",Toast.LENGTH_SHORT).show();
                    }else if(pass.length()==0){
                        Toast.makeText(Register.this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
                    }

                    else if(!pass.equals(retpe))
                    {
                        Toast.makeText(getBaseContext() , "Passwords did not match" , Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        new login(mail , nam , pass).execute();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext() , "No internet Connection" , Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    public class login extends AsyncTask<Void , Void , Void>
    {

        String result;

        String email2 , name2 , pass2;

        String name1 , email1;

        public login(String email , String name , String pass)
        {
            this.email2 = email;
            this.name2 = name;
            this.pass2 = pass;
        }


        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("user_email" , email2));
            data.add(new BasicNameValuePair("password" , pass2));
            data.add(new BasicNameValuePair("user_name" , name2));
            data.add(new BasicNameValuePair("address" , "address"));
            data.add(new BasicNameValuePair("country" , "country"));
            data.add(new BasicNameValuePair("state" , "state"));
            data.add(new BasicNameValuePair("city" , "city"));

            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(SIGN_UP , data);
            try {
                JSONObject obj = new JSONObject(result);
                name1 = obj.getString("user_name");
                email1 = obj.getString("user_email");

                Log.d("asdasdasd" , name1);
                Log.d("asdasdasd" , email1);
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (NullPointerException e)
            {
             e.printStackTrace();
                //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }



            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            if(name1!=null && email1!=null)
            {


                Toast.makeText(getApplicationContext() , email1+"\n"+"successfully registered" , Toast.LENGTH_SHORT).show();


                name.setText("");
                email.setText("");
                password.setText("");
                retype.setText("");


            }
            else
            {
                Toast.makeText(getApplicationContext() , "already registered" , Toast.LENGTH_SHORT).show();
            }


            Log.d("asdasdasd" , result);

            super.onPostExecute(aVoid);
        }
    }

}
