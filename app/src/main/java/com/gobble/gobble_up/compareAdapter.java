package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


class compareAdapter extends RecyclerView.Adapter<compareAdapter.RecycleViewHolder> implements ItemTouchHelperAdapter{
    private final Context context;
    List<comparelistBean> list = new ArrayList<>();

    ImageButton a;

    compareAdapter(Context context, List<comparelistBean> list)
    {
        this.context = context;
        this.list = list;
    }


    public void setGridData(List<comparelistBean> list)
    {
        this.list = list;
    }


    @Override
    public compareAdapter.RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.compare_model, null);
        return new RecycleViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {

        final comparelistBean item = list.get(position);
        holder.setIsRecyclable(false);
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(false).build();


      //  if (item.getPrice() == null)
      //  {
      //      holder.add.setVisibility(View.GONE);
      //      holder.price.setVisibility(View.INVISIBLE);
      //      holder.calories.setGravity(Gravity.START);
      //      holder.fat.setGravity(Gravity.START);
      //  }

            ImageLoader imageLoader = ImageLoader.getInstance();


        a = holder.add;

            imageLoader.displayImage(item.getImage() , holder.image , options);
            Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
            holder.image.startAnimation(animation);

            holder.price.setText("Price: "+item.getPrice());
            holder.name.setText(item.getName());
            holder.calories.setText(Html.fromHtml("<b><i>Calories:   </i></b>"+item.getCalories()));
            //holder.calories.setText("<b>Calories:   </b>"+item.getCalories());
            holder.fat.setText(Html.fromHtml("<b><i>Calories:   </i></b>"+item.getCalories()));
            holder.carbs.setText(Html.fromHtml("<b><i>Carbohydrates:   </i></b>"+item.getCarbs()));
            holder.protein.setText(Html.fromHtml("<b><i>Protein:   </i></b>"+item.getProtein()));
            holder.sodium.setText(Html.fromHtml("<b><i>Sodium:   </i></b>"+item.getSodium()));
            holder.potassium.setText(Html.fromHtml("<b><i>Potassium:   </i></b>"+item.getPotassium()));
            holder.fiber.setText(Html.fromHtml("<b><i>Fiber:   </i></b>"+item.getFiber()));
            holder.sugar.setText(Html.fromHtml("<b><i>Sugar:   </i></b>"+item.getSugar()));
            holder.vita.setText(Html.fromHtml("<b><i>Vitamin A:   </i></b>"+item.getVita()));
            holder.vitc.setText(Html.fromHtml("<b><i>Vitamin C:   </i></b>"+item.getVitc()));
            holder.calcium.setText(Html.fromHtml("<b><i>Calcium:   </i></b>"+item.getCalcium()));
            holder.iron.setText(Html.fromHtml("<b><i>Iron:   </i></b>"+item.getIron()));


        final comparebean b = (comparebean)context.getApplicationContext();
        for (int i = 0 ; i<b.tempList.size() ; i++)
        {
            if (Integer.parseInt(item.getId()) == b.tempList.get(i).getId())
            {
                holder.add.setBackground(context.getResources().getDrawable(R.drawable.list_yellow));
            }
        }


        holder.add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int flag = 0;


                for (int i = 0 ; i<b.tempList.size() ; i++)
                {
                    if (Integer.parseInt(item.getId()) == b.tempList.get(i).getId())
                    {
                        flag++;
                    }
                }


                if (flag <1)
                {
                    ProductBean item1 = new ProductBean();
                    item1.setId(Integer.parseInt(item.getId()));
                    item1.setSetlist(true);
                    b.tempList.add(item1);
                    b.comparecount++;

                    holder.add.setBackground(context.getResources().getDrawable(R.drawable.list_yellow));
                }
                else
                {
                    Toast.makeText(context , "Already in list" , Toast.LENGTH_SHORT).show();
                }

                }
















        });









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
        b.list.remove(position);
        list.remove(position);
        notifyItemRemoved(position);


    }

    class RecycleViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView price , name;
        TextView calories , fat , carbs , protein , sodium , potassium , fiber , sugar , vita ,vitc , calcium , iron;

        ImageButton add;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.compare_model_image);
            price = (TextView)itemView.findViewById(R.id.compare_model_price);
            name = (TextView)itemView.findViewById(R.id.compare_model_name);
            calories = (TextView)itemView.findViewById(R.id.compare_calories);
            fat = (TextView)itemView.findViewById(R.id.compare_fat);
            carbs = (TextView)itemView.findViewById(R.id.compare_carbs);
            protein = (TextView)itemView.findViewById(R.id.compare_protein);
            sodium = (TextView)itemView.findViewById(R.id.compare_sodium);
            potassium= (TextView)itemView.findViewById(R.id.compare_potassium);
            fiber = (TextView)itemView.findViewById(R.id.compare_fiber);
            sugar = (TextView)itemView.findViewById(R.id.compare_sugar);
            vita = (TextView)itemView.findViewById(R.id.compare_vita);
            vitc = (TextView)itemView.findViewById(R.id.compare_vitc);
            calcium = (TextView)itemView.findViewById(R.id.compare_calcium);
            iron = (TextView)itemView.findViewById(R.id.compare_iron);
            add = (ImageButton) itemView.findViewById(R.id.compare_model_add);


        }












    }



}
