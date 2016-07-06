package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hi on 6/30/2016.
 */

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RecycleViewHolder>{


    private Context context;
    //private final LayoutInflater mInflater;
    private ArrayList<RateBean> list = new ArrayList<>();


    public RateAdapter(Context context , ArrayList<RateBean> list)
    {
        this.context = context;
        this.list = list;
    }


    public void setGridData(ArrayList<RateBean> list)
    {
        this.list = list;
    }



    @Override
    public RateAdapter.RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_model, null);

        RateAdapter.RecycleViewHolder viewHolder = new RateAdapter.RecycleViewHolder(layoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {


        final RateBean item = list.get(position);
        holder.setIsRecyclable(false);

        holder.username.setText(item.getUsername());
        holder.date.setText(item.getDate());
        holder.review.setText(item.getReview());
        holder.rating.setRating(Float.parseFloat(item.getRating()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView review , date;
        RatingBar rating;



        public RecycleViewHolder(View itemView) {
            super(itemView);
            username = (TextView)itemView.findViewById(R.id.getCommentUser);
            review = (TextView) itemView.findViewById(R.id.getCommentReview);
            date = (TextView) itemView.findViewById(R.id.getCommentDate);
            rating = (RatingBar) itemView.findViewById(R.id.getCommentRating);



        }








    }

}
