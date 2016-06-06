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
        import android.widget.CompoundButton;
        import android.widget.ImageView;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;


        import java.io.InputStream;
        import java.net.URL;
        import java.util.ArrayList;

/**
 * Created by hi on 30-05-2016.
 */
public class ProdAdapter extends ArrayAdapter<ProductBean> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ProductBean> list = new ArrayList<>();


    public ProdAdapter(Context context, int resource, ArrayList<ProductBean> list) {
        super(context, resource, list);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final comparebean b = (comparebean)getContext().getApplicationContext();
        View row = convertView;
        final ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.prodName);
            holder.imageView = (ImageView) row.findViewById(R.id.prodImage);
            holder.priceTextView = (TextView) row.findViewById(R.id.prodPrice);
            holder.switcher = (Switch)row.findViewById(R.id.switcher);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }






        final ProductBean item = list.get(position);
        holder.titleTextView.setText(item.getName());
        String price = "Price: " + item.getPrice();
        holder.priceTextView.setText(price);


        for (int i = 0 ; i<list.size() ; i++)
        {


            if(item.getSet()!=null)
            {
                if (item.getSet())
                {
                    holder.switcher.setChecked(true);

                }
            }
        }





        holder.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if (isChecked)
                {


                    if (b.list.size() < 4)
                    {

                        b.list.add(item);
                        // b.bitmaps.add(LoadImageFromURL(item.getImage()));
                        Log.d("asdasdasd" , String.valueOf(b.list.size()));
                        Toast.makeText(getContext() , "Added to list" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext() , "Max. limit reached" , Toast.LENGTH_SHORT).show();

                        holder.switcher.setChecked(false);
                    }


                }
                else
                {
                    int index = 0;
                    Log.d("asdasdasd" , item.getName());
                    int l = b.list.size();
                    for (int i = 0 ; i<l ; i++)
                    {
                        if (item.getId() == b.list.get(i).getId())
                        {
                            index = i;
                        }
                    }


                    b.list.remove(index);
                    //b.list.remove(item);
                    Toast.makeText(getContext() , "Item removed" ,Toast.LENGTH_SHORT ).show();
                }

            }
        });


        new loadImage(holder.imageView, item.getImage()).execute();


        return row;
    }

    public void setGridData(ArrayList<ProductBean> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
    }

    public class loadImage extends AsyncTask<Void, Void, Void> {

        String url;
        ImageView iv;
        Bitmap d;

        public loadImage(ImageView iv, String url) {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {


            Log.d("asdasdasd", url);
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
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            return d;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    static class ViewHolder {
        TextView titleTextView;
        TextView priceTextView;
        ImageView imageView;
        Switch switcher;
    }
}