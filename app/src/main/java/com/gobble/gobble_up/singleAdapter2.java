package com.gobble.gobble_up;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hi on 09-06-2016.
 */
public class singleAdapter2 extends RecyclerView.Adapter<singleAdapter2.RecycleViewHolder>{
    private Context context;
    private ArrayList<bean> list = new ArrayList<>();


    public singleAdapter2(Context context , ArrayList<bean> list)
    {
        this.context = context;
        this.list = list;
    }

    public void setGridData(ArrayList<bean> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product, null);
        return new RecycleViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {

        bean item = list.get(position);
        holder.left.setText(item.getLeft());
        holder.right.setText(item.getRight());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {

        TextView left;
        TextView right;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            left = (TextView) itemView.findViewById(R.id.leftId);
            right = (TextView) itemView.findViewById(R.id.rightId);
        }
    }
}
