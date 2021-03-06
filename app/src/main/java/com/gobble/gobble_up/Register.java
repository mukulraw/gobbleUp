package com.gobble.gobble_up;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Register extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener , View.OnClickListener{


    EditText name , email , password , retype;
    private static final int RC_SIGN_IN = 9001;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    Button register;
    public static final int CONNECTION_TIMEOUT=10000;
    ProgressBar loading;
    private Boolean goog_flag = false;
    public static final int READ_TIMEOUT=15000;
    private Boolean fb_flag = false;
    private String imageUrl = null;
    Toast toast;
    private String SIGN_UP = "http://nationproducts.in/global/api/register";

    Button goog;
    LoginButton fb;
    private SharedPreferences.Editor edit;

    private AccessTokenTracker accessTokenTracker;
    //private ProfileTracker profileTracker;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_register);

        toast = Toast.makeText(this , null , Toast.LENGTH_SHORT);

        loading = (ProgressBar)findViewById(R.id.register_loading);

        SharedPreferences pref = getSharedPreferences("MySignin", Context.MODE_PRIVATE);
        edit = pref.edit();

        name = (EditText)findViewById(R.id.et_username);
        email = (EditText)findViewById(R.id.et_Email_id);
        password = (EditText)findViewById(R.id.et_pass);
        retype = (EditText)findViewById(R.id.et_re_type_password);

        goog = (Button)findViewById(R.id.bt_google);
        fb = (LoginButton)findViewById(R.id.bt_mailid);

        register = (Button)findViewById(R.id.bt_submit);



        callbackManager = CallbackManager.Factory.create();






        fb.setReadPermissions(Arrays.asList("public_profile"));
        fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            final String p = profile2.getId();
                           //Log.d("asdasdasdfbId" , p);
                            final String e = profile2.getId();
                            String n = profile2.getName();
                            fb_flag = true;
                            //new login(e , n , p).execute();

                            loading.setVisibility(View.VISIBLE);


                            String SUB_CATEGORY = "http://nationproducts.in/";
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(SUB_CATEGORY)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            final CompareAPI request = retrofit.create(CompareAPI.class);

                            Call<registerBean> call = request.register(n , e , p , "address" , "country" , "city" , "state");


                            call.enqueue(new Callback<registerBean>() {
                                @Override
                                public void onResponse(Call<registerBean> call, Response<registerBean> response) {



                                    Call<loginBean> call1 = request.login(e , p);

                                    call1.enqueue(new Callback<loginBean>() {
                                        @Override
                                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                            //Toast.makeText(getApplicationContext() , "welcome "+response.body().getUserName() , Toast.LENGTH_SHORT).show();

                                            toast.setText("welcome "+response.body().getUserName());
                                            toast.show();

                                            Intent i = new Intent(getApplicationContext() , GetStartActivity.class);


                                            edit.putBoolean("fb" , true);
                                            edit.apply();
                                            i.putExtra("url" , imageUrl);

                                            i.putExtra("id" , response.body().getUserId());
                                            i.putExtra("name" , response.body().getUserName());
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            loading.setVisibility(View.GONE);

                                            startActivity(i);
                                            finish();







                                        }

                                        @Override
                                        public void onFailure(Call<loginBean> call, Throwable t) {

                                            loading.setVisibility(View.GONE);

                                            LoginManager.getInstance().logOut();
                                            fb_flag = false;


                                        }
                                    });







                                }

                                @Override
                                public void onFailure(Call<registerBean> call, Throwable t) {
                                    Call<loginBean> call1 = request.login(e , p);

                                    call1.enqueue(new Callback<loginBean>() {
                                        @Override
                                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                            //Toast.makeText(getApplicationContext() , "welcome "+response.body().getUserName() , Toast.LENGTH_SHORT).show();
                                            toast.setText("welcome "+response.body().getUserName());
                                            toast.show();
                                            Intent i = new Intent(getApplicationContext() , GetStartActivity.class);


                                            edit.putBoolean("fb" , true);
                                            edit.apply();
                                            i.putExtra("url" , imageUrl);

                                            i.putExtra("id" , response.body().getUserId());
                                            i.putExtra("name" , response.body().getUserName());
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            loading.setVisibility(View.GONE);

                                            startActivity(i);
                                            finish();







                                        }

                                        @Override
                                        public void onFailure(Call<loginBean> call, Throwable t) {

                                            loading.setVisibility(View.GONE);
                                            LoginManager.getInstance().logOut();
                                            fb_flag = false;


                                        }
                                    });




                                }
                            });



                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    final String p = profile.getId();
                   // Log.d("asdasdasdfbId" , p);
                    final String e = profile.getId();
                    String n = profile.getName();
                    fb_flag = true;
                    //new login(e , n , p).execute();

                    loading.setVisibility(View.VISIBLE);


                    String SUB_CATEGORY = "http://nationproducts.in/";
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(SUB_CATEGORY)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final CompareAPI request = retrofit.create(CompareAPI.class);

                    Call<registerBean> call = request.register(n , e , p , "address" , "country" , "city" , "state");


                    call.enqueue(new Callback<registerBean>() {
                        @Override
                        public void onResponse(Call<registerBean> call, Response<registerBean> response) {



                            Call<loginBean> call1 = request.login(e , p);

                            call1.enqueue(new Callback<loginBean>() {
                                @Override
                                public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                    //Toast.makeText(getApplicationContext() , "welcome "+response.body().getUserName() , Toast.LENGTH_SHORT).show();
                                    toast.setText("welcome "+response.body().getUserName());
                                    toast.show();
                                    Intent i = new Intent(getApplicationContext() , GetStartActivity.class);


                                    edit.putBoolean("fb" , true);
                                    edit.apply();
                                    i.putExtra("url" , imageUrl);

                                    i.putExtra("id" , response.body().getUserId());
                                    i.putExtra("name" , response.body().getUserName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    loading.setVisibility(View.GONE);

                                    startActivity(i);
                                    finish();







                                }

                                @Override
                                public void onFailure(Call<loginBean> call, Throwable t) {

                                    loading.setVisibility(View.GONE);
                                    LoginManager.getInstance().logOut();
                                    fb_flag = false;


                                }
                            });







                        }

                        @Override
                        public void onFailure(Call<registerBean> call, Throwable t) {
                            Call<loginBean> call1 = request.login(e , p);

                            call1.enqueue(new Callback<loginBean>() {
                                @Override
                                public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                    //Toast.makeText(getApplicationContext() , "welcome "+response.body().getUserName() , Toast.LENGTH_SHORT).show();
                                    toast.setText("welcome "+response.body().getUserName());
                                    toast.show();
                                    Intent i = new Intent(getApplicationContext() , GetStartActivity.class);


                                    edit.putBoolean("fb" , true);
                                    edit.apply();
                                    i.putExtra("url" , imageUrl);

                                    i.putExtra("id" , response.body().getUserId());
                                    i.putExtra("name" , response.body().getUserName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    loading.setVisibility(View.GONE);

                                    startActivity(i);
                                    finish();







                                }

                                @Override
                                public void onFailure(Call<loginBean> call, Throwable t) {
                                    loading.setVisibility(View.GONE);

                                    LoginManager.getInstance().logOut();
                                    fb_flag = false;


                                }
                            });
                        }
                    });




                }





            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if(checkPlayServices())
        {

            buildGoogleApiClient();

            goog.setOnClickListener(this);

        }


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
                        //Toast.makeText(Register.this,,Toast.LENGTH_SHORT).show();
                        toast.setText("Please Enter Your Name");
                        toast.show();
                    }else if(mail.length()==0){
                        toast.setText("Please Enter Your Email.");
                        toast.show();
                        //Toast.makeText(Register.this,,Toast.LENGTH_SHORT).show();
                    }else if(!Util.emailValidater(mail)){
                        toast.setText("Please Enter a Valid Email");
                        toast.show();
                        //Toast.makeText(Register.this,,Toast.LENGTH_SHORT).show();
                    }else if(pass.length()==0){
                        toast.setText("Please Enter Your Password");
                        toast.show();
                        //Toast.makeText(Register.this,,Toast.LENGTH_SHORT).show();
                    }

                    else if(!pass.equals(retpe))
                    {
                       toast.setText("Passwords did not match");
                        toast.show();
                        //Toast.makeText(getBaseContext() ,  , Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        loading.setVisibility(View.VISIBLE);
                        new login(mail , nam , pass).execute();
                    }
                }
                else
                {
                    toast.setText("No internet Connection");
                    toast.show();
                    //Toast.makeText(getBaseContext() ,  , Toast.LENGTH_SHORT).show();
                }



            }
        });

    }





    private boolean checkPlayServices() {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
       // Log.i("DEBUG_TAG",
          //      "checkGooglePlayServicesAvailable, connectionStatusCode="
           //             + checkGooglePlayServices);

        if (GooglePlayServicesUtil.isUserRecoverableError(checkGooglePlayServices)) {
            showGooglePlayServicesAvailabilityErrorDialog(checkGooglePlayServices);
            return false;
        }


        return true;

    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                final Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,Register.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST);
                if (dialog == null) {
                   // Log.e("DEBUG_TAG",
                       //     "couldn't get GooglePlayServicesUtil.getErrorDialog");
                    Toast.makeText(getBaseContext(),
                            "incompatible version of Google Play Services",
                            Toast.LENGTH_LONG).show();

                    Button button = (Button)findViewById(R.id.button);
                    button.setVisibility(View.GONE);

                    dialog.show();
                }
                //this was wrong here -->dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //String url = String.valueOf(acct.getPhotoUrl());
            String e = acct.getId();
         //   Log.d("asdasd"  , e);
            String n = acct.getDisplayName();
            String p = acct.getId();
           // Log.d("asdasdid" , p);







            //new login(e , n , p).execute();




            //Log.d("asdasdasd" , p);


            goog_flag = true;


            //new login(e , p).execute();

            loading.setVisibility(View.VISIBLE);

            String SUB_CATEGORY = "http://nationproducts.in/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SUB_CATEGORY)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final CompareAPI request = retrofit.create(CompareAPI.class);

            Call<registerBean> call = request.register(n , e , p , "address" , "country" , "city" , "state");


            final String finalE = e;
            final String finalP = p;
            call.enqueue(new Callback<registerBean>() {
                @Override
                public void onResponse(Call<registerBean> call, Response<registerBean> response) {



                    Call<loginBean> call1 = request.login(finalE, finalP);

                    call1.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {


                            toast.setText("welcome "+response.body().getUserName());
                            //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext() , GetStartActivity.class);


                            edit.putBoolean("google" , true);
                            edit.apply();
                            i.putExtra("url" , imageUrl);

                            i.putExtra("id" , response.body().getUserId());
                            i.putExtra("name" , response.body().getUserName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            loading.setVisibility(View.GONE);

                            startActivity(i);
                            finish();







                        }

                        @Override
                        public void onFailure(Call<loginBean> call, Throwable t) {


                            if (mGoogleApiClient.isConnected()) {

                                loading.setVisibility(View.GONE);

                                signOut();
                                goog_flag = false;
                            }


                        }
                    });







                }

                @Override
                public void onFailure(Call<registerBean> call, Throwable t) {

                    Call<loginBean> call1 = request.login(finalE, finalP);

                    call1.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                            toast.setText("welcome "+response.body().getUserName());
                            toast.show();
                            //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext() , GetStartActivity.class);


                            edit.putBoolean("google" , true);
                            edit.apply();
                            i.putExtra("url" , imageUrl);

                            i.putExtra("id" , response.body().getUserId());
                            i.putExtra("name" , response.body().getUserName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            loading.setVisibility(View.GONE);

                            startActivity(i);
                            finish();







                        }

                        @Override
                        public void onFailure(Call<loginBean> call, Throwable t) {


                            if (mGoogleApiClient.isConnected()) {

                                loading.setVisibility(View.GONE);

                                signOut();
                                goog_flag = false;
                            }


                        }
                    });
                }
            });







        } else {
            // Signed out, show unauthenticated UI.

        }
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {




                    }
                });
    }


    private synchronized void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).enableAutoManage(this,this)
                .addApi(LocationServices.API).build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {

        if (v == goog)
        {
            signIn();
        }



    }


    private class login extends AsyncTask<Void , Void , Void>
    {

        String result;
        HttpURLConnection conn;
        URL url = null;
        String email2 , name2 , pass2;

        String name1 , email1;

        login(String email , String name , String pass)
        {
            this.email2 = email;
            this.name2 = name;
            this.pass2 = pass;
        }


        @Override
        protected Void doInBackground(Void... params) {



            try {
                url = new URL(SIGN_UP);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.connect();
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_name", name2)
                        .appendQueryParameter("user_email", email2)
                        .appendQueryParameter("password", pass2)
                        .appendQueryParameter("address", "address")
                        .appendQueryParameter("country", "country")
                        .appendQueryParameter("state", "state")
                        .appendQueryParameter("city", "city");

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                result = bufferedReader.readLine();

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


            //result = ruc.sendPostRequest(SIGN_UP , data);
            try {
                JSONObject obj = new JSONObject(result);
                name1 = obj.getString("user_name");
                email1 = obj.getString("user_email");
//
              //  Log.d("asdasdasd" , name1);
              //  Log.d("asdasdasd" , email1);
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

                toast.setText(name1+"\n"+"successfully registered\nPlease Login to continue");
                toast.show();

               // Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();


                name.setText("");
                email.setText("");
                password.setText("");
                retype.setText("");



                Intent i = new Intent(getApplicationContext() , LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                loading.setVisibility(View.GONE);

                startActivity(i);
                finish();


                LoginManager.getInstance().logOut();


            }
            else
            {

                loading.setVisibility(View.GONE);
                toast.setText("already registered");
                toast.show();
                //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
            }

//
          //  Log.d("asdasdasd" , result);

            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient!=null&&mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
