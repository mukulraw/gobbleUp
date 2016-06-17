package com.gobble.gobble_up;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by Dheeraj on 8/6/2015.
 */
public class RegisterUserClass {
 
    public String sendPostRequest(String requestURL,
                                  List<NameValuePair> data) {

        URL url;
        InputStream is = null;
        String response = null;
        String result = null;
        String json = null;
        JSONObject jObj = null;
        try {






            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            post.setEntity(new UrlEncodedFormEntity(data));

try
{
    HttpResponse httpResponse = client.execute(post);
    HttpEntity entity = httpResponse.getEntity();

    is = entity.getContent();

}catch (HttpHostConnectException e)
{

    HttpResponse httpResponse = client.execute(post);
    HttpEntity entity = httpResponse.getEntity();

    is = entity.getContent();
}





        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
          //  Log.e("Buffer Error", "Error converting result " + e.toString());
        }










        return json;
    }






}