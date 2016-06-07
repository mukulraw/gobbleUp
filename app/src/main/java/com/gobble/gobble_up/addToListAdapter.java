package com.gobble.gobble_up;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hi on 03-06-2016.
 */
public class addToListAdapter extends ArrayAdapter<addListBean> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<addListBean> list = new ArrayList<>();
    private String DELETE_LIST = "http://nationproducts.in/global/api/deletelist/listId/";

    public addToListAdapter(Context context, int resource , ArrayList<addListBean> list) {
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

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        addListBean item = list.get(position);
        holder.listtName.setText("Name: "+item.getListName());
        holder.listtCreatedTime.setText("Date: "+item.getCreatedTime());
        holder.listttotal.setText("Total: "+item.getTotalItem());
        holder.listtidd.setText("Id: "+item.getListId());


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


                        new delete(DELETE_LIST+idd).execute();
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

        return row;


    }
    static class ViewHolder {
        TextView listtName;
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
                Log.d("asdasdasdurl" , url);
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
                Log.d("asdasdasddelete" , json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
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




        }
    }

}
