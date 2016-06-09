package com.gobble.gobble_up;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hi on 07-06-2016.
 */
public class searchAdapter extends ArrayAdapter<searchBean> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<searchBean> list = new ArrayList<>();

    public searchAdapter(Context context, int resource , ArrayList<searchBean> list) {
        super(context,resource , list);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;
    }


    public void setGridData(ArrayList<searchBean> mGridData) {
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
            holder.searchName = (TextView) row.findViewById(R.id.search_name);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        searchBean item = list.get(position);
        holder.searchName.setText(item.getName());

        Log.d("adapter" , item.getName());

        return row;
    }

    static class ViewHolder {
        TextView searchName;
    }


}
