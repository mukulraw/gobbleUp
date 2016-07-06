package com.gobble.gobble_up;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


@SuppressWarnings("ALL")
class CatGridAdapter extends RecyclerView.Adapter<CatGridAdapter.RecycleViewHolder> {

    private Context context;
    private ArrayList<categoryBean> list = new ArrayList<>();


    CatGridAdapter(Context context, ArrayList<categoryBean> list) {
        this.context = context;
        this.list = list;
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

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_model, null);
        return new RecycleViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        categoryBean item = list.get(position);
        holder.titleTextView.setText(item.getName());



        DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheOnDisk(true).cacheInMemory(true).build();


        ImageLoader imageLoader = ImageLoader.getInstance();




        imageLoader.displayImage(item.getImage() , holder.imageView  , options);
        Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
        holder.imageView.startAnimation(animation);

        holder.titleTextView.setVisibility(View.VISIBLE);

    }



    @Override
    public int getItemCount() {
        return list.size();
    }







    class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        ImageView imageView;

        RecycleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView)itemView.findViewById(R.id.grid_cat_text);
            imageView = (ImageView)itemView.findViewById(R.id.grid_cat_image);



        }

        @Override
        public void onClick(View v) {


            categoryBean item = list.get(getPosition());


            Bundle bundle = new Bundle();
            bundle.putString("id" , String.valueOf(item.getId()));
            bundle.putString("name" , item.getName());
            bundle.putString("image" , item.getImage());

            SubCategoryFragment frag2 = new SubCategoryFragment();
            frag2.setArguments(bundle);


            FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();


            ft.replace(R.id.layoutToReplace , frag2);

            ft.addToBackStack(null);
            ft.commit();




        }




    }
}
