package com.gobble.gobble_up;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

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

@SuppressWarnings("ALL")
public class AddtoList extends AppCompatActivity {


    private FloatingActionButton addinList;
    private ListView addToListListview;

    private ProgressBar bar;

    ArrayList<addListBean> list;

    DBHandler handler;

    ConnectionDetector cd;
    addToListAdapter adapter;

    private String iidd;


    private String GET_ALL_LIST = "http://nationproducts.in/global/api/alllists/userId/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cd = new ConnectionDetector(this);

        handler = new DBHandler(this);

        bar = (ProgressBar)findViewById(R.id.add_to_list_progress);


        addinList = (FloatingActionButton) findViewById(R.id.create_new_list);
        addToListListview = (ListView)findViewById(R.id.add_to_list_listview);

        if (addToListListview != null) {
            addToListListview.setDividerHeight(0);
        }


        addToListListview.setDividerHeight(1);


        final comparebean be = (comparebean)this.getApplicationContext();

        iidd = be.user_id;

        addinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(AddtoList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.create_list_dialog);
                dialog.setCancelable(true);
                dialog.show();

                Button Create = (Button)dialog.findViewById(R.id.buttonncreate);
                Button Cancel = (Button)dialog.findViewById(R.id.buttoncancel);
                final EditText CreateName = (EditText)dialog.findViewById(R.id.create_list_name);

                Create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nnaammee = CreateName.getText().toString();
                        new login(be.user_id , nnaammee).execute();
                        CreateName.setText("");
                        dialog.dismiss();
                        refresh();


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
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

                final comparebean b = (comparebean)getBaseContext().getApplicationContext();

                final addListBean item = (addListBean)parent.getItemAtPosition(position);
                final String idd = item.getListId();
                final Dialog dialog = new Dialog(AddtoList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add);
                dialog.setCancelable(true);
                dialog.show();

                Button YES = (Button)dialog.findViewById(R.id.dialogAdd);
                Button NO = (Button)dialog.findViewById(R.id.dialogNo);



                YES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0 ; i<b.tempList.size() ; i++)
                        {
                            new addToList(item.getListId() , String.valueOf(b.tempList.get(i).getId()) , "1").execute();
                        }



                        bar.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext() , "Adding..." , Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext() , "Please Wait" , Toast.LENGTH_SHORT).show();

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



    private class login extends AsyncTask<Void , Void , Void>
    {

        String userId , listName;
        String result;
        String name;
        String idd;

        login(String username, String password)
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
            String CREATE_LIST = "http://nationproducts.in/global/api/createlist";
            result = ruc.sendPostRequest(CREATE_LIST, data);


            try {
                JSONObject obj = new JSONObject(result);

                idd = obj.getString("listId");
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {


            final comparebean b = (comparebean)getBaseContext().getApplicationContext();


            if (idd!=null)
            {
                for (int i = 0 ; i<b.tempList.size() ; i++)
                {
                    new addToList(idd , String.valueOf(b.tempList.get(i).getId()) , "1").execute();
                }



                bar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext() , "Adding..." , Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext() , "Please Wait" , Toast.LENGTH_SHORT).show();

            }


            super.onPostExecute(aVoid);
        }
    }

    public void refresh()
    {
        addToListListview.setVisibility(View.GONE);
        addinList.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);

        list.clear();
        String GET_ALL_LIST = "http://nationproducts.in/global/api/alllists/userId/";
        new connect(GET_ALL_LIST + iidd).execute();

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
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();

            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();

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
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.setGridData(list);
            addinList.setVisibility(View.VISIBLE);
            addToListListview.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);


        }
    }



    private class addToList extends AsyncTask<Void , Void , Void> {

        String pId, lId, quant;
        String result;


        addToList(String lId, String pId, String quant) {
            this.pId = pId;
            this.lId = lId;
            this.quant = quant;
        }


        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("listId", lId));
            data.add(new BasicNameValuePair("productId", pId));
            data.add(new BasicNameValuePair("quantity", quant));

            RegisterUserClass ruc = new RegisterUserClass();
            String ADD_TO_LIST = "http://nationproducts.in/global/api/addtolist";
            result = ruc.sendPostRequest(ADD_TO_LIST, data);


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {





            syncSQLite();







            super.onPostExecute(aVoid);
        }
    }

    public void syncSQLite()
    {


        if (cd.isConnectingToInternet())
        {
            new syncmain(GET_ALL_LIST + iidd).execute();
        }
    }

    public class syncmain extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;



        String id;


        int length;
        String url;

        syncmain(String url)
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
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    offlineMainListBean bean = new offlineMainListBean();
                    bean.setListName(obj.getString("listName"));
                    bean.setListId(obj.getString("listId"));

                    id = obj.getString("listId");

                    bean.setCreatedTime(obj.getString("createdTime"));
                    bean.setTotalItems(obj.getString("totalItem"));




                    handler.insertUser(bean);



                    //list.add(bean);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);






            syncSubList(id);







            //refreshList();

//            adapter.setGridData(list);

            // bar.setVisibility(View.GONE);

            // lview.setVisibility(View.VISIBLE);
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }


    public void syncSubList(String id)
    {

        if (cd.isConnectingToInternet())
        {
            String GET_LIST_ITEMS = "http://nationproducts.in/global/api/listitems/listId/";
            new syncSub(GET_LIST_ITEMS + id).execute();
        }

    }



    public class syncSub extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;




        int length;
        String url;

        syncSub(String url)
        {
            this.url = url;
        }




        @Override
        protected Void doInBackground(Void... params) {


            try {

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
                // Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    offlineSubListBean bean = new offlineSubListBean();
                    bean.setName(obj.getString("name"));
                    bean.setListId(obj.getString("listId"));


                    ImageLoader imageLoader = ImageLoader.getInstance();

                    Bitmap bitmap = imageLoader.loadImageSync(obj.getString("image"));

                    bean.setImage(Utils.getImageBytes(bitmap));


                    bean.setProductId(obj.getString("productId"));
                    //bean.setImage(obj.getString("image"));

                    bean.setPrice(obj.getString("price"));

                    handler.insertSubData(bean);

                    //list.add(bean);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //adapter = new SubListAdapter(getBaseContext() , list);
            //lv.setAdapter(adapter);
            //lv.setHasFixedSize(true);
            //lv.setLayoutManager(lLayout);

            //refresh();


            Toast.makeText(getApplicationContext() , "Added Successfully" , Toast.LENGTH_SHORT).show();

            bar.setVisibility(View.GONE);

            Intent resultIntent = getIntent();
            resultIntent.putExtra("result","result");
            setResult(RESULT_OK , resultIntent);
            finish();

            //adapter.setGridData(list);
            //total.setText("TOTAL:  "+String.valueOf(adapter.getTotal()));
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }




    private class addToListAdapter extends ArrayAdapter<addListBean> {

        private Context context;
        private int layoutResourceId;
        private ArrayList<addListBean> list = new ArrayList<>();
        private String DELETE_LIST = "http://nationproducts.in/global/api/deletelist/listId/";




        addToListAdapter(Context context, int resource, ArrayList<addListBean> list) {
            super(context, resource , list);
            this.context = context;
            this.layoutResourceId = resource;
            this.list = list;
        }

        public void setGridData(ArrayList<addListBean> mGridData) {
            this.list = mGridData;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;
            if (row == null) {

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.listtName = (TextView) row.findViewById(R.id.addlistlistName);
                holder.listtCreatedTime = (TextView) row.findViewById(R.id.addlidtlidtCreatedTime);
                holder.listtidd = (TextView)row.findViewById(R.id.addlistlistId);
                holder.listttotal = (TextView)row.findViewById(R.id.addlistTotalItem);
                //holder.delete = (Button)row.findViewById(R.id.deleteList);

                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            addListBean item = list.get(position);
            holder.listtName.setText(Html.fromHtml("<b>Name: </b>"+item.getListName()));
            holder.listtCreatedTime.setText("Date: "+item.getCreatedTime());
            holder.listttotal.setText(item.getTotalItem());
            holder.listtidd.setText("Id: "+item.getListId());


            /*holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addListBean item = list.get(position);
                    final String idd = item.getListId();

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.delete_list_dialog);
                    dialog.setCancelable(false);
                    dialog.show();

                    Button YES = (Button)dialog.findViewById(R.id.confirmDelete);
                    Button NO = (Button)dialog.findViewById(R.id.cancel_delete_list);

                    YES.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {




                            new delete(DELETE_LIST+idd).execute();
                            dialog.dismiss();



                            refresh();


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
*/
            return row;


        }

        class ViewHolder {
            TextView listtName;
            TextView listtCreatedTime , listttotal , listtidd;
            //Button delete;
        }
        class delete extends AsyncTask<Void , Void , Void>
        {

            InputStream is;
            String json;
            JSONArray array;




            int length;
            String url;

            delete(String url)
            {
                this.url = url;
            }





            @Override
            protected Void doInBackground(Void... params) {


                try {


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

                }

                try {
                    array = new JSONArray(json);
                    length = array.length();
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();

                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);



            }
        }

    }



}


