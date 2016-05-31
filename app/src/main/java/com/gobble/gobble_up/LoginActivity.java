package com.gobble.gobble_up;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import com.facebook.FacebookSdk;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener  , View.OnClickListener{
    private String LOG_IN = "http://nationproducts.in/global/api/login";
    private static final int RC_SIGN_IN = 9001;

    EditText email , password;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    TextView forgot;
    Button sign_in , sign_up;
    //private CallbackManager callbackManager;
    //private LoginButton loginButton;
    Button google_signin;
    //Button fb_signin;
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;

    String fb_email , fb_name , fb_id , fb_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.et_Email);
        password = (EditText)findViewById(R.id.et_Password);

        google_signin = (Button)findViewById(R.id.bt_google1);
        //fb_signin = (Button)findViewById(R.id.bt_facebook1);

        forgot = (TextView)findViewById(R.id.tv_userforgotpassword);

        sign_in = (Button)findViewById(R.id.bt_signin);
        sign_up = (Button)findViewById(R.id.bt_creataccount);




        //fb_signin.setOnClickListener(this);


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
    private boolean checkPlayServices() {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        Log.i("DEBUG_TAG",
                "checkGooglePlayServicesAvailable, connectionStatusCode="
                        + checkGooglePlayServices);

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
                        connectionStatusCode,LoginActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST);
                if (dialog == null) {
                    Log.e("DEBUG_TAG",
                            "couldn't get GooglePlayServicesUtil.getErrorDialog");
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
            String url = String.valueOf(acct.getPhotoUrl());
            String n = acct.getEmail();



            Log.d("asdasdasd", String.valueOf(acct.getPhotoUrl()));

            Toast.makeText(getApplicationContext() , "welcome "+n , Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext() , GetStartActivity.class);
            i.putExtra("url" , url);
            i.putExtra("name" , n);
            startActivity(i);
            finish();


        } else {
            // Signed out, show unauthenticated UI.

        }
    }





    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {

        if (v == google_signin)
        {
            signIn();

        }

      /*  if(v == fb_signin)
        {
            callbackManager= CallbackManager.Factory.create();

            loginButton= (LoginButton)findViewById(R.id.login_button);

            loginButton.setReadPermissions("public_profile", "email","user_friends");

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            loginButton.performClick();

            loginButton.setPressed(true);

            loginButton.invalidate();

            loginButton.registerCallback(callbackManager, mCallBack);

            loginButton.setPressed(false);

            loginButton.invalidate();

        }*/

    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {

                                fb_id = object.getString("id").toString();
                                fb_email = object.getString("email").toString();
                                fb_name = object.getString("name").toString();
                                fb_gender = object.getString("gender").toString();


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this,"welcome "+fb_name,Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(LoginActivity.this,GetStartActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };


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
