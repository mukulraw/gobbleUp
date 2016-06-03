package com.gobble.gobble_up;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hi on 02-06-2016.
 */
public class compareListAdapter extends ArrayAdapter<compareListViewBean>{

    private Context context;
    private int layoutResourceId;
    private ArrayList<compareListViewBean> list = new ArrayList<>();

    public compareListAdapter(Context context, int resource , ArrayList<compareListViewBean> list) {
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
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.header = (TextView) row.findViewById(R.id.compare_list_header);
            holder.first = (TextView) row.findViewById(R.id.compare_item_text1);
            holder.second = (TextView)row.findViewById(R.id.compare_item_text2);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        compareListViewBean item = list.get(position);
        holder.header.setText(item.getHeader());
        holder.first.setText(item.getFirst());
        holder.second.setText(item.getSecond());


        return row;
    }

    static class ViewHolder {
        TextView header;
        TextView first;
        TextView second;
    }

}
