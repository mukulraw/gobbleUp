package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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

public class SubList extends AppCompatActivity {

    ArrayList<offlineSubListBean> list;
    private RecyclerView lv;
    SubListAdapter adapter;
    String id;
    private TextView total;
    private GridLayoutManager lLayout;
    SubListDBHandler handler;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list);
        handler = new SubListDBHandler(this);
        cd = new ConnectionDetector(this);



        total = (TextView)findViewById(R.id.total);

        Bundle b = getIntent().getExtras();

        id = String.valueOf(b.get("id"));

        Log.d("asdasdasd" , id);

        lLayout = new GridLayoutManager(this , 1);

        lv = (RecyclerView) findViewById(R.id.sub_list);
        list = new ArrayList<>();



        //refresh();

        getFromOffline(id);




    }




    public void getFromOffline(String iidd)
    {
        adapter = new SubListAdapter(getBaseContext() , list);
        lv.setAdapter(adapter);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(lLayout);

        List<offlineSubListBean> ll = handler.getSubData(iidd);


        for (int i=0 ; i<ll.size();i++)
        {
            try {

                offlineSubListBean bean = ll.get(i);




                list.add(bean);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        adapter.setGridData(list);

        total.setText("TOTAL:  "+String.valueOf(adapter.getTotal()));


        syncOffline();


    }


    public void syncOffline()
    {
        String GET_LIST_ITEMS = "http://nationproducts.in/global/api/listitems/listId/";
        new connect(GET_LIST_ITEMS + id).execute();
    }


    public void refresh()
    {
        ArrayList<offlineSubListBean> l2 = new ArrayList<>();

        List<offlineSubListBean> ll = handler.getSubData(id);
        for (int i=0 ; i<ll.size();i++)
        {
            try {

                offlineSubListBean bean = ll.get(i);




                l2.add(bean);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        adapter.setGridData(l2);
        total.setText("TOTAL:  "+String.valueOf(adapter.getTotal()));
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

            refresh();


            //adapter.setGridData(list);
            //total.setText("TOTAL:  "+String.valueOf(adapter.getTotal()));
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }


    private class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.RecyclerViewHolder> {

        Context context;
    private ArrayList<offlineSubListBean> list1 = new ArrayList<>();
    private String DELETE_LIST = "http://nationproducts.in/global/api/removefromlist";

    String lid , pid , result;



        class RecyclerViewHolder extends RecyclerView.ViewHolder{
            ImageView sublistImage;
            TextView sublistName , subListPrice;
            ImageButton delete;

            RecyclerViewHolder(View itemView) {
                super(itemView);

                sublistImage = (ImageView)itemView.findViewById(R.id.sublistImage);
                sublistName = (TextView) itemView.findViewById(R.id.subListName);
                subListPrice = (TextView)itemView.findViewById(R.id.subListPrice);
                delete = (ImageButton)itemView.findViewById(R.id.sub_list_delete);

            }
        }



    SubListAdapter(Context context, ArrayList<offlineSubListBean> list) {
        this.context = context;
        this.list1 = list;
    }

    public void setGridData(ArrayList<offlineSubListBean> mGridData) {
        this.list1 = mGridData;
        notifyDataSetChanged();
    }

        @Override
        public SubListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_list_model, null);
            return new RecyclerViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int pos) {
            final offlineSubListBean item = list1.get(holder.getAdapterPosition());


            holder.setIsRecyclable(false);



            holder.sublistImage.setImageBitmap(Utils.getImage(item.getImage()));







            holder.sublistName.setText(item.getName());
            holder.subListPrice.setText(item.getPrice());


            if (cd.isConnectingToInternet())
            {
                try
                {
                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lid = item.getListId();
                            pid = item.getProductId();


                            final Dialog dialog = new Dialog(SubList.this);
                            dialog.setContentView(R.layout.delete_list_dialog);
                            dialog.setCancelable(false);
                            dialog.show();

                            Button YES = (Button)dialog.findViewById(R.id.confirmDelete);
                            Button NO = (Button)dialog.findViewById(R.id.cancel_delete_list);

                            YES.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {




                                    new delete1(DELETE_LIST).execute();
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

                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

            }


        }

        @Override
        public int getItemCount() {
            return list1.size();
        }





    public float getTotal()
    {
        float sum = 0;
        for (int i = 0 ; i<getItemCount() ; i++)
        {
            offlineSubListBean item = list1.get(i);
            float temp = Float.parseFloat(item.getPrice());
            sum = sum+temp;


        }
        return sum;
    }



    class delete1 extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;




        int length;
        String url;

        delete1(String url)
        {
            this.url = url;
        }





        @Override
        protected Void doInBackground(Void... params) {


            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("listId" , lid));
            data.add(new BasicNameValuePair("id" , pid));

            Log.d("asdasdsdlistId" , lid);
            Log.d("asdasdaProductId" , pid);


            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(url , data);
            try {
                JSONObject obj = new JSONObject(result);

                Log.d("asdasdasd" , String.valueOf(obj));


            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            list = new ArrayList<offlineSubListBean>();
          refresh();


        }
    }







}



}
