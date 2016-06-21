package com.gobble.gobble_up;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
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

/**
 * Created by hi on 07-06-2016.
 */
public class SubListAdapter extends ArrayAdapter<subListBean> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<subListBean> list = new ArrayList<>();
    private String DELETE_LIST = "http://nationproducts.in/global/api/removefromlist";
    private String GET_LIST_ITEMS = "http://nationproducts.in/global/api/listitems/listId/";
    String lid , pid , result;
    SubListAdapter adapter;


    public SubListAdapter(Context context, int resource , ArrayList<subListBean> list) {
        super(context, resource , list);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;
    }

    public void setGridData(ArrayList<subListBean> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.sublistImage = (ImageView) row.findViewById(R.id.sublistImage);
            holder.sublistName = (TextView) row.findViewById(R.id.subListName);
            holder.subListPrice = (TextView)row.findViewById(R.id.subListPrice);
            holder.delete = (ImageButton)row.findViewById(R.id.sub_list_delete);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final subListBean item = list.get(position);


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(false).resetViewBeforeLoading(true).build();


        ImageLoader imageLoader = ImageLoader.getInstance();







        try
        {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final subListBean item = list.get(position);
                    lid = item.getListId();
                    pid = item.getProductId();


                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.delete_list_dialog);
                    dialog.setCancelable(false);
                    dialog.show();

                    Button YES = (Button)dialog.findViewById(R.id.confirmDelete);
                    Button NO = (Button)dialog.findViewById(R.id.cancel_delete_list);

                    YES.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {




                            new delete(DELETE_LIST).execute();
                            dialog.dismiss();



                            SubList l = new SubList();
                            l.list = new ArrayList<subListBean>();
                            adapter = new SubListAdapter(context , R.layout.add_list_model , list);
                            l.lv = (ListView)((SubList)context).findViewById(R.id.sub_list);

                            if (l.lv != null) {
                                l.lv.setAdapter(adapter);
                            }

                            list.clear();
                            new connect(GET_LIST_ITEMS + lid).execute();
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



       // try
        //{




                  //imageLoader.displayImage(item.getImage() , holder.sublistImage , options);



                  // new loadImage(holder.sublistImage , item.getImage());
                  holder.subListPrice.setText("Price: "+ item.getPrice());
                  holder.sublistName.setText("Name: "+ item.getName());


       // }catch (NullPointerException e)
       // {
       //     e.printStackTrace();
      //  }

        return row;
    }

    public float getTotal()
    {
        float sum = 0;
        for (int i = 0 ; i<getCount() ; i++)
        {
            subListBean item = list.get(i);
            float temp = Float.parseFloat(item.getPrice());
            sum = sum+temp;


        }
        return sum;
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
                // Log.e("Buffer Error", "Error converting result " + e.toString());
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
                    subListBean bean = new subListBean();
                    bean.setName(obj.getString("name"));
                    bean.setListId(obj.getString("listId"));
                    bean.setImage(obj.getString("image"));
                    bean.setQuantity(obj.getString("quantity"));
                    bean.setPrice(obj.getString("price"));
                    bean.setProductId(obj.getString("productId"));
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


            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("listId" , lid));
            data.add(new BasicNameValuePair("productId" , pid));

            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(DELETE_LIST , data);
            try {
                JSONObject obj = new JSONObject(result);

                //idd = obj.getString("listId");
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // new MainList().refresh();


        }
    }

    public class loadImage extends AsyncTask<Void , Void , Void>
    {

        String url;
        ImageView iv;
        Bitmap d;

        public loadImage(ImageView iv , String url)
        {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {


           // Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            iv.setImageBitmap(d);

        }
    }

    static class ViewHolder {
        ImageView sublistImage;
        TextView sublistName , subListPrice;
        ImageButton delete;
    }

    private Bitmap LoadImageFromURL(String url)

    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            return d;
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
