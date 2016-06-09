package com.gobble.gobble_up;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener{

    private Timer t;
    private String LOG_IN = "http://nationproducts.in/global/api/login";
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private static final int RC_SIGN_IN = 9001;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    String imageUrl = null;
    String nn = null;
    String pp = null;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        pref = getSharedPreferences("MySignin", Context.MODE_PRIVATE);
        //edit = pref.edit();





        if ( pref.getBoolean("google" , false)  )
        {

            if(checkPlayServices())
            {

                buildGoogleApiClient();

                signIn();


            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            String e = acct.getEmail();
            nn = e;
            //Log.d("asdsd" , e);
            String p = acct.getEmail();
            pp = p;
            imageUrl = String.valueOf(acct.getPhotoUrl());
            //Log.d("asdasdasd" , p);





            new login(e , p).execute();


        } else {
            // Signed out, show unauthenticated UI.

        }
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
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                final Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,StartActivity.this,
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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
