package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class CatGridAdapter extends ArrayAdapter<categoryBean> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<categoryBean> list = new ArrayList<>();


    public CatGridAdapter(Context context, int layoutResourceId , ArrayList<categoryBean> list) {
        super(context, layoutResourceId,list);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
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
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_cat_text);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_cat_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        categoryBean item = list.get(position);
        holder.titleTextView.setText(item.getName());

        //new loadImage(item.getImage()).execute();



        //holder.imageView.setImageBitmap(bitmap);
        //holder.imageView.setImageDrawable(LoadImageFromURL(item.getImage()));


        new loadImage(holder.imageView , item.getImage()).execute();


        return row;
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


    public void setGridData(ArrayList<categoryBean> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
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

}
