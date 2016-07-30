package com.gobble.gobble_up;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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

public class SearchResultActivity extends AppCompatActivity implements TextWatcher {

    private ArrayList<searchBean> original;
    ArrayList<searchBean> result;
    searchAdapter adapter;
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        original = new ArrayList<>();
        original.clear();

        String GET_ALL = "http://nationproducts.in/global/api/allproduct";
        new connect(GET_ALL).execute();

        EditText searchbar = (EditText) findViewById(R.id.searchBar);

        lv = (ListView) findViewById(R.id.search_list);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                searchBean item = (searchBean) parent.getItemAtPosition(position);







                Intent resultintent = getIntent();
                resultintent.putExtra("result" , item.getId());
                resultintent.putExtra("image" , item.getImage());
                setResult(RESULT_OK , resultintent);
                finish();

            }
        });

        searchbar.addTextChangedListener(this);

    }


    private void handleIntent(String query) {

        result = new ArrayList<>();
        //original = new ArrayList<>();


        //new connect(GET_ALL).execute();

                if (query!=null)
                {
                    int textLength = query.length();
                    result.clear();

                    for (int i = 0; i < original.size(); i++) {
                        String playerName = original.get(i).getName();
                        if (textLength <= playerName.length()) {

                            if (query.equalsIgnoreCase(playerName.substring(0, textLength)))
                                result.add(original.get(i));
                        }


                        adapter = new searchAdapter(getApplicationContext(), R.layout.search_model, result);

                        adapter.setGridData(result);


                        if (lv != null) {
                            lv.setAdapter(adapter);
                        }
                        // lv.setAdapter(adapter);


                    }
                }



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        handleIntent(null);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length()>0)
        {
            handleIntent(String.valueOf(s));
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void afterTextChanged(Editable s) {

        handleIntent(null);

    }

    public class connect extends AsyncTask<Void, Void, Void> {

            InputStream is;
            String json;
            JSONArray array;
            HttpURLConnection connection;
            URL u = null;

            int length;
            String url;

            connect(String url) {
                this.url = url;
            }


            @Override
            protected Void doInBackground(Void... params) {


                try {
                    // HttpClient client = new DefaultHttpClient();
                    //  HttpGet get = new HttpGet(url);
                    //  HttpResponse response = client.execute(get);
                    //HttpEntity entity = response.getEntity();
                    //is = entity.getContent();

                    u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();
                    if (connection.getResponseCode() == 200) {
                        is = connection.getInputStream();
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "utf-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    is.close();
                    json = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    connection.disconnect();
                }



                try {
                    array = new JSONArray(json);
                    length = array.length();
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                    //Log.e("JSON Parser", "Error parsing data " + e.toString());
                }


                for (int i = 0; i < length; i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        searchBean bean = new searchBean();
                        bean.setName(obj.getString("name"));
                        bean.setPrice(obj.getString("price"));
                        bean.setImage(obj.getString("image"));
                        bean.setCatId(obj.getString("cat_id"));
                        bean.setId(obj.getString("id"));
                        bean.setNutrition(obj.getString("nutration"));
                        bean.setSubCatId(obj.getString("sub_cat_id"));
                      //  Log.d("asdasdasd", obj.getString("name"));
                        original.add(bean);
                      //  Log.d("asdasdasd", "added");
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }


                return null;
            }


        }



}
