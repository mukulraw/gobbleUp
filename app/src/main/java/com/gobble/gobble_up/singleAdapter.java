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
 * Created by hi on 30-05-2016.
 */
public class singleAdapter extends ArrayAdapter<bean> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<bean> list = new ArrayList<>();

    public singleAdapter(Context context, int resource , ArrayList<bean> list) {
        super(context, resource , list);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;

    }

    static class ViewHolder {
        TextView left , right;
    }

    public void setGridData(ArrayList<bean> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.left = (TextView) row.findViewById(R.id.leftId);
            holder.right = (TextView) row.findViewById(R.id.rightId);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        bean item = list.get(position);
        holder.left.setText(item.getLeft());
        holder.right.setText(item.getRight());


        return row;
    }
}
