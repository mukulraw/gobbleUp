package com.gobble.gobble_up;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by hi on 8/5/2016.
 */

class brandAdapter extends RecyclerView.Adapter<brandAdapter.ViewHolder> {

    Context context;
    List<String> list = new ArrayList<>();
    List<String> clist = new ArrayList<>();

    public brandAdapter(Context context , List<String> list)
    {
        this.context = context;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.brand_model , null);
        return new ViewHolder(v);
    }


    public List<String> getCheckedIds()
    {
        return clist;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String brand = list.get(position);

        holder.tv.setText(brand);

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (holder.box.isChecked())
                {
                    clist.remove(list.get(position));
                    holder.box.toggle();
                }
                if (!holder.box.isChecked())
                {
                    clist.add(list.get(position));
                    holder.box.toggle();
                }



            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        CheckBox box;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.brand_list_model);
            box = (CheckBox)itemView.findViewById(R.id.brand_list_model_check);


        }
    }




}
