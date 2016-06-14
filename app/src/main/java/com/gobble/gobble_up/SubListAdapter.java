package com.gobble.gobble_up;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hi on 07-06-2016.
 */
public class SubListAdapter extends ArrayAdapter<subListBean> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<subListBean> list = new ArrayList<>();

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
    public View getView(int position, View convertView, ViewGroup parent) {

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

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        subListBean item = list.get(position);


        new loadImage(holder.sublistImage , item.getImage());
        holder.subListPrice.setText("Price: "+ item.getPrice());
        holder.sublistName.setText("Name: "+ item.getName());


        return row;
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
