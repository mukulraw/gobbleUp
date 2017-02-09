package com.gobble.gobble_up;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gobble.gobble_up.POJO.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;


class brandAdapter extends RecyclerView.Adapter<brandAdapter.ViewHolder> {

    Context context;
    List<String> list = new ArrayList<>();
    List<Model> l2 = new ArrayList<>();
    private List<String> clist = new ArrayList<>();

    brandAdapter(Context context, List<String> list , List<Model> l2)
    {
        this.context = context;
        this.list = list;
        this.l2 = l2;
    }

    public void setGridData(List<String> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.brand_model , null);
        return new ViewHolder(v);
    }


    List<String> getCheckedIds()
    {
        return clist;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String brand = list.get(holder.getAdapterPosition());

        holder.tv.setText(brand);

        clist.clear();

        for (int i = 0 ; i < l2.size() ; i++)
        {
            if (Objects.equals(l2.get(i).getBrand(), brand))
            {
                holder.box.setChecked(true);

                int flash = 0;

                for (int j = 0 ; j < clist.size() ; j++)
                {
                    if (Objects.equals(clist.get(j), brand))
                    {
                        flash++;
                    }
                }

                if (flash == 0)
                {
                    clist.add(list.get(holder.getAdapterPosition()));
                }

            }
            else
            {
                holder.box.setChecked(false);
                //clist.remove(list.get(holder.getAdapterPosition()));
            }
        }



        holder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (holder.box.isChecked())
                {
                    int flag = 0;

                    for (int i = 0 ; i < clist.size() ; i++)
                    {
                        if (Objects.equals(list.get(holder.getAdapterPosition()), clist.get(i)))
                        {
                            flag++;
                        }
                    }

                    if (flag == 0)
                    {
                        clist.add(list.get(holder.getAdapterPosition()));
                    }



                    //compoundButton.toggle();
                }
                if (!holder.box.isChecked())
                {
                    clist.remove(list.get(holder.getAdapterPosition()));
                    //compoundButton.toggle();
                }


            }
        });


        /*holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (holder.box.isChecked())
                {
                    clist.remove(list.get(position).getName());
                    holder.box.toggle();
                }
                if (!holder.box.isChecked())
                {
                    clist.add(list.get(position).getName());
                    holder.box.toggle();
                }



            }
        });


*/





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
