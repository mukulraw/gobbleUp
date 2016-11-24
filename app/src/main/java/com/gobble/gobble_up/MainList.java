package com.gobble.gobble_up;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class MainList extends AppCompatActivity {

    ListView lview;
    ArrayList<addListBean> list;
    String iidd;

    MainListAdapter adapter;
    private String GET_ALL_LIST = "http://nationproducts.in/global/api/alllists/userId/";
    private String DELETE_LIST = "http://nationproducts.in/global/api/deletelist/listId/";
    ProgressBar bar;

    DBHandler handler;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);


        handler = new DBHandler(this);

        cd = new ConnectionDetector(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar11);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        bar = (ProgressBar)findViewById(R.id.main_list_progressbar);



        lview = (ListView)findViewById(R.id.main_list_listview);
        if (lview != null) {
            lview.setDividerHeight(0);
        }
        final comparebean be = (comparebean)this.getApplicationContext();

        iidd = be.user_id;



        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addListBean item = (addListBean)parent.getItemAtPosition(position);
                String idd = item.getListId();

                Log.d("asdasdasd" , idd);

                Intent i = new Intent(getBaseContext() , SubList.class);
                i.putExtra("id" , idd);
                startActivity(i);
            }
        });


        list = new ArrayList<>();

        adapter = new MainListAdapter(this , R.layout.main_list_model , list);
        lview.setAdapter(adapter);


        refresh();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void refresh()
    {

        bar.setVisibility(View.VISIBLE);

        lview.setVisibility(View.GONE);
        list.clear();
        //new connect(GET_ALL_LIST + iidd).execute();

        getFromSQLite();

        //mProgressBar.setVisibility(View.VISIBLE);
    }



    public void getFromSQLite()
    {


        List<offlineMainListBean> listt = handler.getAllusers();


        for (int i = 0 ; i < listt.size() ; i++)
        {
            addListBean bean = new addListBean();
            bean.setListName(listt.get(i).getListName());
            bean.setListId(listt.get(i).getListId());
            bean.setCreatedTime(listt.get(i).getCreatedTime());
            bean.setTotalItem(listt.get(i).getTotalItems());
            list.add(bean);
        }


        adapter.setGridData(list);

        bar.setVisibility(View.GONE);

        lview.setVisibility(View.VISIBLE);


        syncSQLite();




    }



    public void syncSQLite()
    {


        if (cd.isConnectingToInternet())
        {
            new connect(GET_ALL_LIST + iidd).execute();
        }
    }

    public void refreshList()
    {
        List<offlineMainListBean> listt = handler.getAllusers();

        list.clear();

        for (int i = 0 ; i < listt.size() ; i++)
        {
            addListBean bean = new addListBean();
            bean.setListName(listt.get(i).getListName());
            bean.setListId(listt.get(i).getListId());
            bean.setCreatedTime(listt.get(i).getCreatedTime());
            bean.setTotalItem(listt.get(i).getTotalItems());
            list.add(bean);
        }


        adapter.setGridData(list);

        bar.setVisibility(View.GONE);

        lview.setVisibility(View.VISIBLE);





    }
















    private class MainListAdapter extends ArrayAdapter<addListBean> {
        private Context context;
        private int layoutResourceId;
        private ArrayList<addListBean> list = new ArrayList<>();
        private String DELETE_LIST = "http://nationproducts.in/global/api/deletelist/listId/";
        private String GET_ALL_LIST = "http://nationproducts.in/global/api/alllists/userId/";
        private String UPDATE_LIST_NAME = "http://nationproducts.in/global/api/updatelistname";


        MainListAdapter(Context context, int resource, ArrayList<addListBean> list) {
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
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;
            if (row == null) {
                //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.listtName = (TextView) row.findViewById(R.id.addlistlistName);
                holder.listtCreatedTime = (TextView) row.findViewById(R.id.addlidtlidtCreatedTime);
                holder.listtidd = (TextView)row.findViewById(R.id.addlistlistId);
                holder.listttotal = (TextView)row.findViewById(R.id.addlistTotalItem);
                holder.delete = (Button)row.findViewById(R.id.deleteList);
                holder.edit = (TextView)row.findViewById(R.id.edit);




                holder.edit.setVisibility(View.GONE);



                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            final addListBean item = list.get(position);
            holder.listtName.setText("Name: "+item.getListName());
            holder.listtCreatedTime.setText("Date: "+item.getCreatedTime());
            holder.listttotal.setText("Total: "+item.getTotalItem());
            holder.listtidd.setText("Id: "+item.getListId());




            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    if (cd.isConnectingToInternet()) {
                        final Dialog dialog1 = new Dialog(getContext());
                        dialog1.setContentView(R.layout.edit_dialog);
                        dialog1.setCancelable(false);
                        dialog1.show();

                        ImageButton cross = (ImageButton) dialog1.findViewById(R.id.cancel_edit_dialog);

                        cross.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        final EditText updatename = (EditText) dialog1.findViewById(R.id.new_name);
                        Button update = (Button) dialog1.findViewById(R.id.dialogipdate);

                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                String iidd = item.getListId();


                                String name = updatename.getText().toString();


                                if (cd.isConnectingToInternet()) {
                                    new login(name, iidd).execute();
                                }

                                dialog1.dismiss();

                                refresh();
                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(context , "No Internet Connection" , Toast.LENGTH_SHORT).show();
                    }

                }
            });



            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (cd.isConnectingToInternet())
                    {



                    addListBean item = (addListBean)list.get(position);
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





                            if (cd.isConnectingToInternet())
                            {
                                new delete(DELETE_LIST+idd , idd).execute();
                            }


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
                    else
                    {
                        Toast.makeText(context , "No Internet Connection" , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return row;
        }
        class login extends AsyncTask<Void , Void , Void>
        {

            String username , password;
            String result;
            String name;


            login(String username, String password)
            {
                this.username = username;
                this.password = password;
            }

            @Override
            protected Void doInBackground(Void... params) {
                List<NameValuePair> data = new ArrayList<>();


                Log.d("asdasdasd" , password);
                data.add(new BasicNameValuePair("listId" , password));
                data.add(new BasicNameValuePair("listName" , username));

                RegisterUserClass ruc = new RegisterUserClass();
                result = ruc.sendPostRequest(UPDATE_LIST_NAME , data);

               // Log.d("asdasdasd" , result);

                //try {
                //  JSONObject obj = new JSONObject(result);
                //name = obj.getString("user_name");
                //email = obj.getString("user_email");
                //idd = obj.getString("user_id");
                //  } catch (JSONException e) {
                //       e.printStackTrace();
                ////   }catch (NullPointerException e)
                //  {
                //       e.printStackTrace();
                // Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
                //   }

                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);

                int a = handler.updateMainListName(password , username);
                refreshList();

            }
        }



        class ViewHolder {
            TextView listtName , edit;
            TextView listtCreatedTime , listttotal , listtidd;
            Button delete;
        }
        class delete extends AsyncTask<Void , Void , Void>
        {

            InputStream is;
            String json;
            JSONArray array;

            String iiid;



            int length;
            String url;

            delete(String url , String iiid)
            {
                this.url = url;
                this.iiid = iiid;
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
                    // Log.d("asdasdasdurl" , url);
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
                    //Log.d("asdasdasddelete" , json);
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


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


                handler.deleteMainList(iiid);
                refreshList();


                // new MainList().refresh();


            }
        }

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


            refreshList();

//            adapter.setGridData(list);

           // bar.setVisibility(View.GONE);

           // lview.setVisibility(View.VISIBLE);
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }

    public class delete extends AsyncTask<Void , Void , Void>
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
                // HttpClient client = new DefaultHttpClient();
                //  HttpGet get = new HttpGet(url);
                //  HttpResponse response = client.execute(get);
                //HttpEntity entity = response.getEntity();
                //is = entity.getContent();

                URL u = new URL(url);
                //Log.d("asdasdasdurl" , url);
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
                //Log.d("asdasdasddelete" , json);
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


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            refresh();

        }
    }

}
