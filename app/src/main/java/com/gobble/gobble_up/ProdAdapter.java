package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hi on 30-05-2016.
 */
public class ProdAdapter extends ArrayAdapter<ProductBean>{

    private Context context;
    private int layoutResourceId;
    private ArrayList<ProductBean> list = new ArrayList<>();

    public ProdAdapter(Context context, int resource , ArrayList<ProductBean> list) {
        super(context, resource , list);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.prodName);
            holder.imageView = (ImageView) row.findViewById(R.id.prodImage);
            holder.priceTextView = (TextView)row.findViewById(R.id.prodPrice);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ProductBean item = list.get(position);
        holder.titleTextView.setText(item.getName());
        String price = "Price: " + item.getPrice();
        holder.priceTextView.setText(price);





        new loadImage(holder.imageView , item.getImage()).execute();


        return row;
    }

    public void setGridData(ArrayList<ProductBean> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
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


            Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            iv.setImageBitmap(d);

        }
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


    static class ViewHolder {
        TextView titleTextView;
        TextView priceTextView;
        ImageView imageView;
    }
}
