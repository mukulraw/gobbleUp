package com.gobble.gobble_up;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gobble.gobble_up.POJO.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SubCatFragment extends Fragment {

    private String PROD_BY_CAT = "http://nationproducts.in/global/api/products/id/";
    private ArrayList<Model> list1;
    private ArrayList<Model> subList;
    private ProdAdapter2 adapter;
    private RecyclerView grid;
    private int page;
    private TextView sort , filter;
    String id;
    private LinearLayout sortFilter;
    private ProgressBar bar;
    String name;
    private boolean sort_flag = false;

    static SubCatFragment newInstance(int page, String id) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("id" , id);
        SubCatFragment fragment = new SubCatFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        this.page = getArguments().getInt("page");
        this.id = getArguments().getString("id");
        this.name = getArguments().getString("name");



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_model, container, false);
        GridLayoutManager lLayout = new GridLayoutManager(view.getContext(), 1);

        bar = (ProgressBar)view.findViewById(R.id.progress_sub_cat_list);

        sortFilter = (LinearLayout)view.findViewById(R.id.sort_and_filter);

        comparebean b = (comparebean)getActivity().getApplicationContext();

        //bar = (RelativeLayout)getActivity().findViewById(R.id.bottombar);



        sort = (TextView) view.findViewById(R.id.sort);
        filter = (TextView) view.findViewById(R.id.filter);


        grid = (RecyclerView)view.findViewById(R.id.sub_cat_grid);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        grid.setHasFixedSize(true);
        grid.setLayoutManager(linearLayoutManager);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)((MainActivity)getContext()).findViewById(R.id.coordinate);
        View vie = coordinatorLayout.findViewById(R.id.bottombar);
        BottomSheetBehavior bot = BottomSheetBehavior.from(vie);


        list1 = new ArrayList<>();
        subList = new ArrayList<>();
        adapter = new ProdAdapter2(getContext() , list1 , bot);

        grid.setAdapter(adapter);



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







                final Dialog dialog = new Dialog(getActivity());

                dialog.setContentView(R.layout.filter_dialog);
                dialog.setCancelable(true);

                dialog.show();


                TextView priceFilter = (TextView)dialog.findViewById(R.id.price_filter);
                TextView brandFilter = (TextView)dialog.findViewById(R.id.brand_filter);

                final List<String> blist = new ArrayList<String>();
                final brandAdapter[] adapter1 = new brandAdapter[1];
                final Set<String> s = new HashSet<String>();
                final RadioGroup rg = (RadioGroup)dialog.findViewById(R.id.radio_group_pricer);
                final RadioGroup rg2 = (RadioGroup)dialog.findViewById(R.id.radio_group_brand);
                final RecyclerView brandList = (RecyclerView)dialog.findViewById(R.id.brand_list);

                priceFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (rg2.getVisibility() == View.VISIBLE)
                        {
                            rg2.setVisibility(View.GONE);
                            rg.setVisibility(View.VISIBLE);
                        }

                    }
                });

                brandFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (rg.getVisibility() == View.VISIBLE)
                        {
                            rg.setVisibility(View.GONE);
                            rg2.setVisibility(View.VISIBLE);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            brandList.setLayoutManager(layoutManager);


                            for ( int i = 0 ; i < list1.size() ; i++)
                            {
                                blist.add(list1.get(i).getBrand());
                            }

                            s.addAll(blist);
                            blist.clear();
                            blist.addAll(s);

                            adapter1[0] = new brandAdapter(getContext() , blist);
                            brandList.setAdapter(adapter1[0]);


                        }

                    }
                });











                TextView clear = (TextView)dialog.findViewById(R.id.clear);

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sort_flag = false;

                        adapter.setGridData(list1);
                        adapter.notifyDataSetChanged();

                        dialog.dismiss();

                    }
                });


                TextView filterer = (TextView) dialog.findViewById(R.id.filterer);

                filterer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (rg.getVisibility() == View.VISIBLE)
                        {
                            final int selectedId = rg.getCheckedRadioButtonId();



                            if (selectedId == R.id.price_under_h)
                            {
                                Log.d("asdasdasdasd" , "selese");

                                subList.clear();

                                for (int i = 0 ; i<list1.size() ; i++)
                                {
                                    if (Float.parseFloat(list1.get(i).getPrice())<100)
                                    {
                                        Log.d("asdasdasd" , "entered");
                                        subList.add(list1.get(i));
                                    }
                                }


                                adapter.setGridData(subList);
                                adapter.notifyDataSetChanged();

                                sort_flag = true;
                                dialog.dismiss();
                            }

                            if (selectedId == R.id.price_between_h_and_5h)
                            {
                                subList.clear();

                                for (int i = 0 ; i<list1.size() ; i++)
                                {
                                    if (Float.parseFloat(list1.get(i).getPrice())<500 && Float.parseFloat(list1.get(i).getPrice())>=100)
                                    {
                                        subList.add(list1.get(i));
                                    }
                                }




                                adapter.setGridData(subList);
                                adapter.notifyDataSetChanged();


                                sort_flag = true;
                                dialog.dismiss();
                            }

                        }

                        if (rg2.getVisibility() == View.VISIBLE)
                        {
                            List<String> checkedList = adapter1[0].getCheckedIds();




                            subList.clear();

                            if (checkedList.size()>0)
                            {
                                for (int i = 0 ; i<list1.size() ; i++)
                                {

                                    for (int j = 0 ; j < checkedList.size() ; j++)
                                    {
                                        if (Objects.equals(list1.get(i).getBrand(), checkedList.get(j)))
                                        {
                                            subList.add(list1.get(i));
                                        }
                                    }

                                }
                                dialog.dismiss();
                            }
                            else
                            {
                                sort_flag = false;

                                adapter.setGridData(list1);
                                adapter.notifyDataSetChanged();

                                dialog.dismiss();
                            }





                            adapter.setGridData(subList);
                            adapter.notifyDataSetChanged();


                            sort_flag = true;



                        }






                    }
                });








            }
        });


        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.sort_dialog);
                dialog.setCancelable(false);
                dialog.show();

                ImageButton close = (ImageButton)dialog.findViewById(R.id.close_dialog);

                final RadioGroup rg = (RadioGroup)dialog.findViewById(R.id.radioGroup);

                TextView sort = (TextView)dialog.findViewById(R.id.sorter);





                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        int selectedId = rg.getCheckedRadioButtonId();



                        if (selectedId == R.id.price_high_to_low)
                        {
                            if (!sort_flag)
                            {
                                Collections.sort(list1, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getPrice()) < Float.parseFloat(rhs.getPrice()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });


                                adapter.setGridData(list1);
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Collections.sort(subList, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getPrice()) < Float.parseFloat(rhs.getPrice()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });


                                adapter.setGridData(subList);
                                adapter.notifyDataSetChanged();
                            }


                        }

                        if (selectedId == R.id.price_low_to_high)
                        {
                            if (!sort_flag)
                            {
                                Collections.sort(list1, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getPrice()) > Float.parseFloat(rhs.getPrice()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });


                                adapter.setGridData(list1);
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                Collections.sort(subList, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getPrice()) > Float.parseFloat(rhs.getPrice()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });


                                adapter.setGridData(subList);
                                adapter.notifyDataSetChanged();
                            }

                        }

                        if (selectedId == R.id.proteinSort)
                        {
                            if (!sort_flag)
                            {
                                Collections.sort(list1, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getNutration().get(3).getValue().toString()) < Float.parseFloat(rhs.getNutration().get(3).getValue().toString()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });



                                adapter.setGridData(list1);
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Collections.sort(subList, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getNutration().get(3).getValue().toString()) < Float.parseFloat(rhs.getNutration().get(3).getValue().toString()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });



                                adapter.setGridData(subList);
                                adapter.notifyDataSetChanged();
                            }


                        }

                        if (selectedId == R.id.caloriesSort)
                        {
                            if (!sort_flag)
                            {
                                Collections.sort(list1, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getNutration().get(0).getValue().toString()) < Float.parseFloat(rhs.getNutration().get(0).getValue().toString()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });


                                adapter.setGridData(list1);
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Collections.sort(subList, new Comparator<Model>() {
                                    @Override
                                    public int compare(Model lhs, Model rhs) {
                                        if (Float.parseFloat(lhs.getNutration().get(0).getValue().toString()) < Float.parseFloat(rhs.getNutration().get(0).getValue().toString()))
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });


                                adapter.setGridData(subList);
                                adapter.notifyDataSetChanged();
                            }


                        }







                        dialog.dismiss();



                    }
                });









            }
        });




        return view;
    }






    private void refresh(String cat)
    {


        list1 = new ArrayList<>();
        list1.clear();
        String url = PROD_BY_CAT + cat;
        //new connect(url).execute();

        getch();
        //mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        sortFilter.setVisibility(View.GONE);
        grid.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);

        refresh(getArguments().getString("id"));

        //getch();




    }




    public void getch()
    {
        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final subCat2API request = retrofit.create(subCat2API.class);
        Call<ArrayList<Model>> call = request.getBooks(getArguments().getString("id"));

        call.enqueue(new Callback<ArrayList<Model>>() {
            @Override
            public void onResponse(Call<ArrayList<Model>> call, Response<ArrayList<Model>> response) {

                list1 = response.body();





                try
                {
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)((MainActivity)getContext()).findViewById(R.id.coordinate);
                    View view = coordinatorLayout.findViewById(R.id.bottombar);
                    BottomSheetBehavior bot = BottomSheetBehavior.from(view);
                    //adapter = new ProdAdapter2(getContext() ,  list1 , bot);
                    adapter.setGridData(list1);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

                //adapter.setGridData(list1);
                //grid.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                bar.setVisibility(View.GONE);
                sortFilter.setVisibility(View.VISIBLE);
                grid.setVisibility(View.VISIBLE);





            }

            @Override
            public void onFailure(Call<ArrayList<Model>> call, Throwable t) {
             //   Log.d("Error",t.getMessage());
            }
        });



    }



    public class connect extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;
        comparebean b;
        int length;
        String url;


        connect(String url)
        {
            this.url = url;
        }




        @Override
        protected Void doInBackground(Void... params) {


           // Log.d("Sub category fragment" , url);

            try {
                //HttpClient client = new DefaultHttpClient();
                //HttpGet get = new HttpGet(url);
                //HttpResponse response = client.execute(get);
                //HttpEntity entity = response.getEntity();
                //is = entity.getContent();
                URL u = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)u.openConnection();
                if(connection.getResponseCode()==200)
                {
                    is = connection.getInputStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
                refresh(getArguments().getString("id"));
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
               // Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                array = new JSONArray(json);
                length = array.length();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
               // Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            try {
                b = (comparebean) getActivity().getApplicationContext();
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }


            for (int i=0 ; i<length;i++)
            {

                try {
                    JSONObject obj = array.getJSONObject(i);
                    ProductBean bean = new ProductBean();
                    bean.setId(obj.getInt("id"));
                    bean.setName(obj.getString("name"));
                    bean.setPrice(obj.getString("price"));

                    bean.setBrand(obj.getString("brand"));
                    bean.setSize(obj.getString("size"));



                    JSONArray nutr = obj.getJSONArray("nutration");
                    JSONObject cal = nutr.getJSONObject(0);
                    JSONObject pro = nutr.getJSONObject(3);

                    bean.setCalories(cal.getString("value"));

                    bean.setProtein(pro.getString("value"));

                    //bean.setDescription(obj.getString("description"));
                    String image = obj.getString("image");
                    image = image.replaceAll(" " , "%20");
                    bean.setImage(image);
                    int l = b.list.size();
        /*            if (l >0) {
                        if (l == 1) {
                            if (obj.getInt("id") == b.list.get(0).getId()) {
                              //  Log.d("asdasdasd", "checked");
                    bean.setSet(true);
                            }
                        }
                        if (l == 2) {
                            if (obj.getInt("id") == b.list.get(0).getId()) {
                                bean.setSet(true);
                            }

                            if (obj.getInt("id") == b.list.get(1).getId()) {
                                bean.setSet(true);
                            }


                        }

                        if (l == 3) {
                            if (obj.getInt("id") == b.list.get(0).getId() || obj.getInt("id") == b.list.get(1).getId() || obj.getInt("id") == b.list.get(2).getId()) {
                                bean.setSet(true);
                            }
                        }
                        if (l == 4) {
                            if (obj.getInt("id") == b.list.get(0).getId() || obj.getInt("id") == b.list.get(1).getId() || obj.getInt("id") == b.list.get(2).getId() || obj.getInt("id") == b.list.get(3).getId()) {
                                bean.setSet(true);
                            }
                        }
                    }

                    int l2 = b.tempList.size();
                    if (l2 >0) {

                        for (int j = 0 ; j<l2 ; j++)
                        {
                            if (obj.getInt("id") == b.tempList.get(j).getId())
                            {
                                bean.setSetlist(true);
                            }
                        }

                    }




                        list1.add(bean);*/
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

           // adapter.setGridData(list1);



            try
            {
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout)((MainActivity)getContext()).findViewById(R.id.coordinate);
                View view = coordinatorLayout.findViewById(R.id.bottombar);
                BottomSheetBehavior bot = BottomSheetBehavior.from(view);
                //adapter = new ProdAdapter2(getContext() ,  list1 , bot);
                adapter.setGridData(list1);
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            //adapter.setGridData(list1);
            //grid.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            bar.setVisibility(View.GONE);
            sortFilter.setVisibility(View.VISIBLE);
            grid.setVisibility(View.VISIBLE);



            //list.clear();
            //mProgressBar.setVisibility(View.GONE);

        }
    }


}