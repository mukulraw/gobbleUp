package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gobble.gobble_up.POJO.CompareModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;


class tempAdapter extends RecyclerView.Adapter<tempAdapter.RecycleViewHolder> implements ItemTouchHelperAdapter {
    private final Context context;
    ArrayList<CompareModel> list = new ArrayList<>();



    tempAdapter(Context context, ArrayList<CompareModel> list)
    {
        this.context = context;
        this.list = list;
    }


    public void setGridData(ArrayList<CompareModel> list)
    {
     this.list = list;
    }



    @Override
    public tempAdapter.RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_list_model, null);
        return new RecycleViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {

        final CompareModel item = list.get(position);
        holder.setIsRecyclable(false);
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(false).build();




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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {
        comparebean b = (comparebean)context.getApplicationContext();
        b.tempList.remove(position);
        list.remove(position);
        notifyItemRemoved(position);
    }

    class RecycleViewHolder extends AnimateViewHolder implements View.OnClickListener{

        ImageView image;
        TextView price , name;


        ImageButton add;




        RecycleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView)itemView.findViewById(R.id.tempprodImage);
            price = (TextView)itemView.findViewById(R.id.tempprodPrice);
            name = (TextView)itemView.findViewById(R.id.tempprodName);



        }

        @Override
        public void animateAddImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }



        @Override
        public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(-itemView.getHeight() * 0.3f)
                    .alpha(0)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }


        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context , MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putBoolean("basket" , true);
            b.putString("id" , list.get(getAdapterPosition()).getId());
            b.putString("image" , list.get(getAdapterPosition()).getImage());
            intent.putExtras(b);


            context.startActivity(intent);

        }
    }



}
