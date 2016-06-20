package com.gobble.gobble_up;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hi on 6/18/2016.
 */
public class tempAdapter extends RecyclerView.Adapter<tempAdapter.RecycleViewHolder> {
    private final Context context;
    List<comparelistBean> list = new ArrayList<>();



    public tempAdapter(Context context , List<comparelistBean> list)
    {
        this.context = context;
        this.list = list;
    }



    @Override
    public tempAdapter.RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_list_model, null);
        return new RecycleViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {

        final comparelistBean item = list.get(position);
        holder.setIsRecyclable(false);
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(false).resetViewBeforeLoading(true).build();


        //  if (item.getPrice() == null)
        //  {
        //      holder.add.setVisibility(View.GONE);
        //      holder.price.setVisibility(View.INVISIBLE);
        //      holder.calories.setGravity(Gravity.START);
        //      holder.fat.setGravity(Gravity.START);
        //  }

        ImageLoader imageLoader = ImageLoader.getInstance();




        imageLoader.displayImage(item.getImage() , holder.image , options);
        Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
        holder.image.startAnimation(animation);

        holder.price.setText("Price: "+item.getPrice());
        holder.name.setText(item.getName());













    }









    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView price , name;


        ImageButton add;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.tempprodImage);
            price = (TextView)itemView.findViewById(R.id.tempprodPrice);
            name = (TextView)itemView.findViewById(R.id.tempprodName);



        }












    }



}