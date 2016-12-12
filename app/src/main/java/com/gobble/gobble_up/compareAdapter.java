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

import com.gobble.gobble_up.POJO.CompareModel;
import com.gobble.gobble_up.POJO.Model;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class compareAdapter extends RecyclerView.Adapter<compareAdapter.RecycleViewHolder> implements ItemTouchHelperAdapter{
    private final Context context;
    ArrayList<Model> list = new ArrayList<>();
    Toast toast;
    TextView a;

    compareAdapter(Context context, ArrayList<Model> list)
    {
        this.context = context;
        this.list = list;
    }


    public void setGridData(ArrayList<Model> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public compareAdapter.RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.compare_model, null);
        return new RecycleViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        toast = Toast.makeText(context , null , Toast.LENGTH_SHORT);

        final Model model = list.get(position);
        holder.setIsRecyclable(false);
        final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(false).build();






        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CompareAPI request = retrofit.create(CompareAPI.class);



        Call<ArrayList<CompareModel>> call = request.getBooks(model.getId());

        call.enqueue(new Callback<ArrayList<CompareModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CompareModel>> call, Response<ArrayList<CompareModel>> response) {

                // Log.d("asdasdasd" , response.body().toString());
                ImageLoader imageLoader = ImageLoader.getInstance();

                final CompareModel item = response.body().get(0);

                //final CompareModel model = response.body().get(0);


                a = holder.add;

                imageLoader.displayImage(item.getImage() , holder.image , options);
                Animation animation = AnimationUtils.loadAnimation(context , R.anim.fade);
                holder.image.startAnimation(animation);

                holder.price.setText(item.getPrice());
                holder.name.setText(item.getName());
                holder.calories.setText(Html.fromHtml(item.getNutration().get(0).getValue()));
                //holder.calories.setText("<b>Calories:   </b>"+item.getCalories());
                holder.fat.setText(Html.fromHtml(item.getNutration().get(1).getValue() + " g"));
                holder.carbs.setText(Html.fromHtml(item.getNutration().get(2).getValue() + " g"));
                holder.protein.setText(Html.fromHtml(item.getNutration().get(3).getValue() + " g"));
                holder.sodium.setText(Html.fromHtml(item.getNutration().get(4).getValue() + " mg"));
                holder.potassium.setText(Html.fromHtml(item.getNutration().get(5).getValue() + " mg"));
                holder.fiber.setText(Html.fromHtml(item.getNutration().get(6).getValue() + " g"));
                holder.sugar.setText(Html.fromHtml(item.getNutration().get(7).getValue() + " g"));
                holder.vita.setText(Html.fromHtml(item.getNutration().get(8).getValue() + " %"));
                holder.vitc.setText(Html.fromHtml(item.getNutration().get(9).getValue() + " %"));
                holder.calcium.setText(Html.fromHtml(item.getNutration().get(10).getValue()));
                holder.iron.setText(Html.fromHtml(item.getNutration().get(11).getValue() + " %"));





            }

            @Override
            public void onFailure(Call<ArrayList<CompareModel>> call, Throwable t) {
                //Log.d("Error",t.getMessage());
            }
        });



        final comparebean b = (comparebean)context.getApplicationContext();
        for (int i = 0 ; i<b.tempList.size() ; i++)
        {
            if (Objects.equals(model.getId(), b.tempList.get(i).getId()))
            {
                holder.add.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
        }


        holder.add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int flag = 0;


                for (int i = 0 ; i<b.tempList.size() ; i++)
                {
                    if (Objects.equals(model.getId(), b.tempList.get(i).getId()))
                    {
                        flag++;
                    }
                }


                if (flag <1)
                {
                    Model item1 = new Model();
                    item1.setId(model.getId());
                    b.tempList.add(item1);
                    b.comparecount++;

                    holder.add.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    //Toast.makeText(context , "Added in Basket" , Toast.LENGTH_SHORT).show();
                    toast.setText("Added in Basket");
                    toast.show();
                }
                else
                {
                    //Toast.makeText(context , "Already in list" , Toast.LENGTH_SHORT).show();



                    int index = 0;
                    int l = b.tempList.size();
                    for (int i = 0 ; i<l ; i++)
                    {
                        if (Objects.equals(model.getId(), b.tempList.get(i).getId()))
                        {
                            index = i;
                        }
                    }


                    b.tempList.remove(index);
                    b.comparecount--;


                    holder.add.setTextColor(context.getResources().getColor(R.color.grey));

                    //Toast.makeText(context , "Removed from Basket" , Toast.LENGTH_SHORT).show();

                    toast.setText("Removed from Basket");
                    toast.show();

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
        //list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();


    }

    class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView price , name;
        TextView calories , fat , carbs , protein , sodium , potassium , fiber , sugar , vita ,vitc , calcium , iron;

        TextView add;

        RecycleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView)itemView.findViewById(R.id.compare_model_image);
            price = (TextView)itemView.findViewById(R.id.compare_model_price);
            name = (TextView)itemView.findViewById(R.id.compare_model_name);
            calories = (TextView)itemView.findViewById(R.id.compare_calories);
            fat = (TextView)itemView.findViewById(R.id.compare_fat);
            carbs = (TextView)itemView.findViewById(R.id.compare_carbs);
            protein = (TextView)itemView.findViewById(R.id.compare_protein);
            sodium = (TextView)itemView.findViewById(R.id.compare_sodium);
            potassium = (TextView)itemView.findViewById(R.id.compare_potassium);
            fiber = (TextView)itemView.findViewById(R.id.compare_fiber);
            sugar = (TextView)itemView.findViewById(R.id.compare_sugar);
            vita = (TextView)itemView.findViewById(R.id.compare_vita);
            vitc = (TextView)itemView.findViewById(R.id.compare_vitc);
            calcium = (TextView)itemView.findViewById(R.id.compare_calcium);
            iron = (TextView)itemView.findViewById(R.id.compare_iron);
            add = (TextView) itemView.findViewById(R.id.compare_model_add);


        }


        @Override
        public void onClick(View view) {


            Intent intent = new Intent(context , MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putBoolean("compare" , true);
            b.putString("idc" , list.get(getAdapterPosition()).getId());
            b.putString("imagec" , list.get(getAdapterPosition()).getImage());
            intent.putExtras(b);


            context.startActivity(intent);





        }
    }



}
