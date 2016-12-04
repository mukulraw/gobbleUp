package com.gobble.gobble_up;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gobble.gobble_up.POJO.Model;
import com.gobble.gobble_up.searchPOJO.searchBean;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SearchResultActivity extends AppCompatActivity implements TextWatcher {

    private ArrayList<searchBean> original;
    List<searchBean> result;
    SearchAdapter adapter;
    private RecyclerView lv;

    GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        original = new ArrayList<>();
        original.clear();

        String GET_ALL = "http://nationproducts.in/global/api/searchproduct";
        //new connect(GET_ALL).execute();

        EditText searchbar = (EditText) findViewById(R.id.searchBar);

        lv = (RecyclerView) findViewById(R.id.search_list);

        result = new ArrayList<>();

        adapter = new SearchAdapter(this , result);


        manager = new GridLayoutManager(this , 1);

        lv.setLayoutManager(manager);
        lv.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new
                SimpleDividerItemDecoration(this, SimpleDividerItemDecoration.VERTICAL_LIST);


        lv.addItemDecoration(itemDecoration);


      /*  lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                searchBean item = (searchBean) parent.getItemAtPosition(position);







                Intent resultintent = getIntent();
                resultintent.putExtra("result" , item.getId());
                resultintent.putExtra("image" , item.getImage());
                setResult(RESULT_OK , resultintent);
                finish();

            }
        });
*/
        searchbar.addTextChangedListener(this);

    }


    private void handleIntent(String query) {

        //result = new ArrayList<>();
        //original = new ArrayList<>();


        //new connect(GET_ALL).execute();

                if (query!=null)
                {
                    /*int textLength = query.length();
                    result.clear();

                    for (int i = 0; i < original.size(); i++) {
                        String playerName = original.get(i).getName();
                        if (textLength <= playerName.length()) {

                            if (query.equalsIgnoreCase(playerName.substring(0, textLength)))
                                result.add(original.get(i));
                        }

*/
                    //adapter = new searchAdapter(getApplicationContext(), R.layout.search_model, result);
                    String SUB_CATEGORY = "http://nationproducts.in/";
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(SUB_CATEGORY)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final CompareAPI request = retrofit.create(CompareAPI.class);

                    Call<List<searchBean>> call = request.search(query , "0" , "0");

                    call.enqueue(new Callback<List<searchBean>>() {
                        @Override
                        public void onResponse(Call<List<searchBean>> call, Response<List<searchBean>> response) {


                            Log.d("asdasdasd" , String.valueOf(response.body().size()));

                           // adapter = new searchAdapter(getApplicationContext() , R.layout.search_model , response.body());

                            result = response.body();

                            adapter.setGridData(result);


                            //if (lv != null) {
                                lv.setAdapter(adapter);
                            //}


                        }

                        @Override
                        public void onFailure(Call<List<searchBean>> call, Throwable t) {

                        }
                    });






                }



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        handleIntent(null);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length()>0)
        {
            handleIntent(String.valueOf(s));
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void afterTextChanged(Editable s) {

        handleIntent(null);

    }



    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>
    {

        List<searchBean> list;
        Context context;


        public SearchAdapter(Context context , List<searchBean> list)
        {
            this.list = list;
            this.context = context;
        }


        public  void setGridData(List<searchBean> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.search_model , parent , false);
            return new SearchAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            searchBean item = list.get(position);
            holder.textView.setText(item.getName());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);

                textView = (TextView)itemView.findViewById(R.id.search_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        searchBean item = list.get(getAdapterPosition());

                        Intent resultintent = getIntent();
                        resultintent.putExtra("result" , item.getId());
                        resultintent.putExtra("image" , item.getImage());
                        setResult(RESULT_OK , resultintent);
                        finish();
                    }
                });

            }
        }

    }



}
