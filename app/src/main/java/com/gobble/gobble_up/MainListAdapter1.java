package com.gobble.gobble_up;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.StringTokenizer;

/**
 * Created by hi on 6/17/2016.
 */
public class MainListAdapter1 extends ArrayAdapter<addListBean> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<addListBean> list = new ArrayList<>();
    private String DELETE_LIST = "http://nationproducts.in/global/api/deletelist/listId/";
    private String GET_ALL_LIST = "http://nationproducts.in/global/api/alllists/userId/";
    private String UPDATE_LIST_NAME = "http://nationproducts.in/global/api/updatelistname";
    addToListAdapter adapter;


    public MainListAdapter1(Context context, int resource , ArrayList<addListBean> list) {
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

                //final addListBean item = (addListBean)list.get(position);
                final String idd = item.getListId();

                final Dialog dialog1 = new Dialog(getContext());
                dialog1.setContentView(R.layout.edit_dialog);
                dialog1.setCancelable(false);
                dialog1.show();


                final EditText updatename = (EditText)dialog1.findViewById(R.id.new_name);
                Button update = (Button)dialog1.findViewById(R.id.dialogipdate);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final comparebean be = (comparebean)context.getApplicationContext();

                        String iidd = item.getListId();


                        String name = updatename.getText().toString();

                        new login(name , iidd).execute();
                        dialog1.dismiss();

                        MainList l = new MainList();
                        l.list = new ArrayList<addListBean>();
                        adapter = new addToListAdapter(context , R.layout.main_list_model , list);
                        l.lview = (ListView)((MainList)context).findViewById(R.id.main_list_listview);

                        if (l.lview != null) {
                            l.lview.setAdapter(adapter);
                        }

                        list.clear();
                        new connect(GET_ALL_LIST + iidd).execute();
                    }
                });




            }
        });



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        final comparebean be = (comparebean)context.getApplicationContext();

                        String iidd = be.user_id;

                        new delete(DELETE_LIST+idd).execute();
                        dialog.dismiss();
                        MainList l = new MainList();
                        l.list = new ArrayList<addListBean>();
                        adapter = new addToListAdapter(context , R.layout.main_list_model , list);
                        l.lview = (ListView)((MainList)context).findViewById(R.id.main_list_listview);

                        if (l.lview != null) {
                            l.lview.setAdapter(adapter);
                        }

                        list.clear();
                        new connect(GET_ALL_LIST + iidd).execute();
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

        return row;


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


            Log.d("asdasdasd" , password);
            data.add(new BasicNameValuePair("listId" , password));
            data.add(new BasicNameValuePair("listName" , username));

            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(UPDATE_LIST_NAME , data);

            Log.d("asdasdasd" , result);

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
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }
    static class ViewHolder {
        TextView listtName , edit;
        TextView listtCreatedTime , listttotal , listtidd;
        Button delete;
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
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }catch (NullPointerException e)
            {
                e.printStackTrace();
                //Toast.makeText(getBaseContext() , "failed to fetch data" , Toast.LENGTH_SHORT).show();
            }








            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // new MainList().refresh();


        }
    }

}
