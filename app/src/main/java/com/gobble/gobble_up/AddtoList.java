package com.gobble.gobble_up;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.List;

public class AddtoList extends AppCompatActivity {


    Button addinList;
    ListView addToListListview;

    ArrayList<addListBean> list;

    addToListAdapter adapter;

    String iidd;
    String prodId;
    Boolean flag = false;

    private String CREATE_LIST = "http://nationproducts.in/global/api/createlist";
    private String GET_ALL_LIST = "http://nationproducts.in/global/api/alllists/userId/";
    private String ADD_TO_LIST = "http://nationproducts.in/global/api/addtolist";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_list);

        Bundle b = getIntent().getExtras();

        addinList = (Button)findViewById(R.id.create_new_list);
        addToListListview = (ListView)findViewById(R.id.add_to_list_listview);

        if (addToListListview != null) {
            addToListListview.setDividerHeight(0);
        }

        prodId = String.valueOf(b.get("listId"));

        final comparebean be = (comparebean)this.getApplicationContext();

        iidd = be.user_id;

        addinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(AddtoList.this);
                dialog.setContentView(R.layout.create_list_dialog);
                dialog.setCancelable(false);
                dialog.show();

                Button Create = (Button)dialog.findViewById(R.id.buttonncreate);
                Button Cancel = (Button)dialog.findViewById(R.id.buttoncancel);
                final EditText CreateName = (EditText)dialog.findViewById(R.id.create_list_name);

                Create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nnaammee = CreateName.getText().toString();
                        if (nnaammee!=null)
                        {
                            new login(be.user_id , nnaammee).execute();
                            CreateName.setText("");
                            dialog.dismiss();
                            refresh();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext() , "Name cannot be empty" , Toast.LENGTH_SHORT).show();
                        }



                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        refresh();
                    }
                });




            }
        });


        addToListListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                addListBean item = (addListBean)parent.getItemAtPosition(position);
                final String idd = item.getListId();
                final Dialog dialog = new Dialog(AddtoList.this);
                dialog.setContentView(R.layout.dialog_confirm_add);
                dialog.setCancelable(false);
                dialog.show();

                Button YES = (Button)dialog.findViewById(R.id.cinfirmAdd);
                Button NO = (Button)dialog.findViewById(R.id.cancel_adding_tolist);

                YES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new addToList(idd , prodId , "1").execute();
                        Intent resultIntent = getIntent();
                        resultIntent.putExtra("result","result");
                        setResult(RESULT_OK , resultIntent);
                        finish();
                        dialog.dismiss();
                    }
                });

                NO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     dialog.dismiss();
                    }
                });


            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        list = new ArrayList<>();

        adapter = new addToListAdapter(this , R.layout.add_list_model , list);
        addToListListview.setAdapter(adapter);


        refresh();
    }



    public class login extends AsyncTask<Void , Void , Void>
    {

        String userId , listName;
        String result;
        String name , email;
        String idd;

        public login(String username , String password)
        {
            this.userId = username;
            this.listName = password;
        }











        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("userId" , userId));
            data.add(new BasicNameValuePair("listName" , listName));

            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(CREATE_LIST , data);
            //Log.d("asdasd" , result);

            try {
                JSONObject obj = new JSONObject(result);

                idd = obj.getString("listId");
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

            //Log.d("asdasdasd" , result);


            if (idd!=null)
            {
                Toast.makeText(getApplicationContext() , "List Created successfully" , Toast.LENGTH_SHORT).show();
            }


            super.onPostExecute(aVoid);
        }
    }

    public void refresh()
    {
        list.clear();
        new connect(GET_ALL_LIST + iidd).execute();
        //mProgressBar.setVisibility(View.VISIBLE);
    }


    public class connect extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;




        int length;
        String url;

        connect(String url)
        {
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

                URL u = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)u.openConnection();
                if(connection.getResponseCode()==200)
                {
                    is = connection.getInputStream();
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
                e.printStackTrace();
                //Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }




            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    addListBean bean = new addListBean();
                    bean.setListName(obj.getString("listName"));
                    bean.setListId(obj.getString("listId"));
                    bean.setCreatedTime(obj.getString("createdTime"));
                    bean.setTotalItem(obj.getString("totalItem"));







                    list.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.setGridData(list);
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }



    public class addToList extends AsyncTask<Void , Void , Void>
    {

        String pId , lId , quant;
        String result;



        public addToList(String lId , String pId , String quant)
        {
            this.pId = pId;
            this.lId = lId;
            this.quant = quant;
        }










        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("listId" , lId));
            data.add(new BasicNameValuePair("productId" , pId));
            data.add(new BasicNameValuePair("quantity" , "1"));

            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(ADD_TO_LIST , data);
            //Log.d("asdasd" , result);

            try {
                JSONObject obj = new JSONObject(result);

                //idd = obj.getString("listId");
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

            //Log.d("asdasdasd" , result);





            super.onPostExecute(aVoid);
        }
    }


}


