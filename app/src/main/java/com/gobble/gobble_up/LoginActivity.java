package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;


import com.facebook.FacebookSdk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener  , View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;


    ProgressBar loading;

    private EditText email , password;
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;

    private SharedPreferences.Editor edit;

    private String imageUrl = null;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Boolean goog_flag = false;
    private Boolean fb_flag = false;
    private Boolean sign_flag = false;


    private SignInButton google_signin;
    private GoogleApiClient mGoogleApiClient;

    Toast toast;



    //private ProfileTracker profileTracker;
    private CallbackManager callbackManager;



    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();




        setContentView(R.layout.activity_login);


        toast = Toast.makeText(this , null , Toast.LENGTH_SHORT);

        loading = (ProgressBar)findViewById(R.id.login_progress);


        SharedPreferences pref = getSharedPreferences("MySignin", Context.MODE_PRIVATE);
        edit = pref.edit();

        email = (EditText)findViewById(R.id.et_Email);
        password = (EditText)findViewById(R.id.et_Password);





        google_signin = (SignInButton) findViewById(R.id.bt_google_sign);
        LoginButton fb_signin = (LoginButton) findViewById(R.id.bt_facebook1);
        fb_signin.setHeight(40);
        fb_signin.setText("SIGNUP USING FACEBOOK");


        TextView forgot = (TextView) findViewById(R.id.tv_userforgotpassword);





        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);

                dialog.setContentView(R.layout.forgot_dialog);

                dialog.show();


               final EditText asd = (EditText)dialog.findViewById(R.id.forgot_email);
                TextView close = (TextView)dialog.findViewById(R.id.close_forget_dialog);
                TextView sub = (TextView)dialog.findViewById(R.id.submit_forget_dialog);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Util.emailValidater(asd.getText().toString()))
                        {
                            String as = asd.getText().toString();
                            new login2(as).execute();
                            dialog.dismiss();
                        }
                        else
                        {
                            toast.setText("Not a valid Email Id");
                            toast.show();
                            //Toast.makeText(getApplication() ,  , Toast.LENGTH_SHORT).show();
                        }



                    }
                });





            }
        });




        Button sign_in = (Button) findViewById(R.id.bt_signin);
        TextView sign_up = (TextView) findViewById(R.id.bt_creataccount);





        fb_signin.setReadPermissions(Collections.singletonList("public_profile"));
        fb_signin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;
            @Override
            public void onSuccess(LoginResult loginResult) {

                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            //Log.v("facebook - profile1", profile2.getFirstName());
                            final String p = profile2.getId();
                            final String e = profile2.getId();
                            String n = profile2.getName();
                            imageUrl = String.valueOf(profile2.getProfilePictureUri(70,70));
                            fb_flag = true;
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


                            call.enqueue(new Callback<registerBean>() {
                                @Override
                                public void onResponse(Call<registerBean> call, Response<registerBean> response) {



                                    Call<loginBean> call1 = request.login(e , p);

                                    call1.enqueue(new Callback<loginBean>() {
                                        @Override
                                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                            toast.setText("welcome "+response.body().getUserName());
                                            toast.show();
                                            //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
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

                                            toast.setText("welcome "+response.body().getUserName());
                                            toast.show();
                                            //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
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
                    //Log.v("facebook - profile2", profile.getFirstName());
                    final String p = profile.getId();
                    final String e = profile.getId();
                    String n = profile.getName();
                    imageUrl = String.valueOf(profile.getProfilePictureUri(70,70));
                    fb_flag = true;


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

                                    toast.setText("welcome "+response.body().getUserName());
                                    toast.show();

                                    //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
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

                                    toast.setText("welcome "+response.body().getUserName());
                                    toast.show();

                                    //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
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

            google_signin.setOnClickListener(this);

        }


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();

ConnectionDetector cd = new ConnectionDetector(getBaseContext());

                if(cd.isConnectingToInternet())
                {


                    if (mail.length()>0 && pass.length()>0)
                    {


                        if (!Util.emailValidater(mail))
                        {
                            toast.setText("Not a valid Email Id");
                            toast.show();
                            //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loading.setVisibility(View.VISIBLE);
                            new login(mail , pass).execute();
                            sign_flag = true;

                        }







                    }
                    else
                    {
                        toast.setText("Invalid email or password");
                        toast.show();
                        //Toast.makeText(getBaseContext() , , Toast.LENGTH_SHORT).show();
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
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext() , Register.class);
                startActivity(i);
            }
        });



    }
    private boolean checkPlayServices() {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        //Log.i("DEBUG_TAG",
               // "checkGooglePlayServicesAvailable, connectionStatusCode="
                       // + checkGooglePlayServices);

        if (GooglePlayServicesUtil.isUserRecoverableError(checkGooglePlayServices)) {
            showGooglePlayServicesAvailabilityErrorDialog(checkGooglePlayServices);
            return false;
        }


        return true;

    }
    private void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                final Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,LoginActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST);
                if (dialog == null) {
                    //Log.e("DEBUG_TAG",
                            //"couldn't get GooglePlayServicesUtil.getErrorDialog");
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
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient!=null&&mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

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

        //callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            loading.setVisibility(View.VISIBLE);

               final String e = acct.getId();

            //Log.d("asdsd" , e);


                final String p = acct.getId();

            if (acct != null) {
                imageUrl = String.valueOf(acct.getPhotoUrl());
            }

            String n = null;
            if (acct != null) {
                n = acct.getDisplayName();
            }

            //Log.d("asdasdasd" , p);


            goog_flag = true;


           //new login(e , p).execute();

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



                    Call<loginBean> call1 = request.login(e, p);

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

                @Override
                public void onFailure(Call<registerBean> call, Throwable t) {

                    Call<loginBean> call1 = request.login(e, p);

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





        }
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {

        if (v == google_signin)
        {
            signIn();

        }



    }





    private class login extends AsyncTask<Void , Void , Void>
    {

        String username , password;
        String result;
        String name , email;
        String idd;
        HttpURLConnection conn;
        URL url = null;

        login(String username, String password)
        {
            this.username = username;
            this.password = password;
        }










        @Override
        protected Void doInBackground(Void... params) {

            try {
                String LOG_IN = "http://nationproducts.in/global/api/login";
                url = new URL(LOG_IN);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.connect();
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_email", username)
                        .appendQueryParameter("password", password);
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

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                conn.disconnect();
            }









            try {
                JSONObject obj = new JSONObject(result);
                name = obj.getString("user_name");
                email = obj.getString("user_email");
                idd = obj.getString("user_id");
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            //Log.d("asdasdasd" , result);


            if(name!=null && email!=null)
            {



                loading.setVisibility(View.GONE);


                toast.setText("welcome "+name);
                toast.show();

                //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext() , GetStartActivity.class);




                if (goog_flag)
                {
                    edit.putBoolean("google",true);
                    edit.apply();
                    i.putExtra("url" , imageUrl);
                }

                if (fb_flag)
                {
                    edit.putBoolean("fb" , true);
                    edit.apply();
                    i.putExtra("url" , imageUrl);
                }

                i.putExtra("id" , idd);
                i.putExtra("name" , name);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            else
            {

                if (goog_flag)
                {
                    if (mGoogleApiClient.isConnected())
                    {
                        signOut();
                        goog_flag = false;
                        loading.setVisibility(View.GONE);
                        Intent i = new Intent(getBaseContext() , Register.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext() , "You are an unregistered user, please register" , Toast.LENGTH_SHORT).show();

                    }
                }

               if (fb_flag)
                {
                    LoginManager.getInstance().logOut();
                    fb_flag = false;
                    loading.setVisibility(View.GONE);
                    Intent i = new Intent(getBaseContext() , Register.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext() , "You are an unregistered user, please register" , Toast.LENGTH_SHORT).show();

                }


                if (sign_flag)
                {
                    sign_flag = false;
                    loading.setVisibility(View.GONE);
                    toast.setText("Invalid Email Id or Password");
                    toast.show();
                    //Toast.makeText(getApplicationContext() , "" , Toast.LENGTH_SHORT).show();
                }

            }

            super.onPostExecute(aVoid);
        }
    }



    private class login2 extends AsyncTask<Void , Void , Void>
    {

        String username;
        String result;

        HttpURLConnection conn;
        URL url = null;

        login2(String username)
        {
            this.username = username;

        }










        @Override
        protected Void doInBackground(Void... params) {

            try {
                String LOG_IN = "http://nationproducts.in/global/api/forgotpassword";
                url = new URL(LOG_IN);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.connect();
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_email", username);
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

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                conn.disconnect();
            }










            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            if (result.equals("{\"responce_code\":200,\"msg\":\"New password send to your email\"}"))
            {
            toast.setText("A new password has been sent to your inbox");
                toast.show();
                //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
            }
            else
            {
                toast.setText("Not a valid user");
                toast.show();
                //Toast.makeText(getApplicationContext() ,  , Toast.LENGTH_SHORT).show();
            }





            super.onPostExecute(aVoid);
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

}
