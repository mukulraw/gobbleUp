package com.gobble.gobble_up;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hi on 03-06-2016.
 */
public class addToListAdapter extends ArrayAdapter<addListBean> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<addListBean> list = new ArrayList<>();


    public addToListAdapter(Context context, int resource , ArrayList<addListBean> list) {
        super(context, resource , list);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;
    }

    public void setGridData(ArrayList<addListBean> mGridData) {
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
            holder.listtName = (TextView) row.findViewById(R.id.addlistlistName);
            holder.listtCreatedTime = (TextView) row.findViewById(R.id.addlidtlidtCreatedTime);
            holder.listtidd = (TextView)row.findViewById(R.id.addlistlistId);
            holder.listttotal = (TextView)row.findViewById(R.id.addlistTotalItem);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        addListBean item = list.get(position);
        holder.listtName.setText("Name: "+item.getListName());
        holder.listtCreatedTime.setText("Date: "+item.getCreatedTime());
        holder.listttotal.setText("Total: "+item.getTotalItem());
        holder.listtidd.setText("Id: "+item.getListId());


        return row;


    }
    static class ViewHolder {
        TextView listtName;
        TextView listtCreatedTime , listttotal , listtidd;
    }


}
