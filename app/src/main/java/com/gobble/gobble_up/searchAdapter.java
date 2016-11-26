package com.gobble.gobble_up;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gobble.gobble_up.POJO.Model;

import java.util.ArrayList;
import java.util.List;

class searchAdapter extends ArrayAdapter<searchBean> {

    private Context context;
    private int layoutResourceId;
    private List<Model> list = new ArrayList<>();

    searchAdapter(Context context, int resource, List<Model> list) {
        super(context,resource);
        this.context = context;
        this.layoutResourceId = resource;
        this.list = list;
    }


    public void setGridData(List<Model> mGridData) {
        this.list = mGridData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
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

        Model item = list.get(position);
        holder.searchName.setText(item.getName());

      //  Log.d("adapter" , item.getName());

        return row;
    }

    static class ViewHolder {
        TextView searchName;
    }


}
