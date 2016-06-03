package com.gobble.gobble_up;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SingleProduct extends AppCompatActivity implements View.OnClickListener{


    ArrayList<bean> list;
    ListView lv;
    ImageView iv;
    TextView title;
    Button add;
    String iidd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);
        Bundle b = getIntent().getExtras();

        add = (Button)findViewById(R.id.addtolist);
        title = (TextView)findViewById(R.id.title_single);


        iidd = String.valueOf(b.get("id"));
        String a = String.valueOf(b.get("name"));
        String image = String.valueOf(b.get("image"));
        String price = String.valueOf(b.get("price"));
        String desc = String.valueOf(b.get("desc"));
        title.setText(a);
        iv = (ImageView)findViewById(R.id.prodImage1);
        lv = (ListView)findViewById(R.id.prodList1);
        new loadImage(iv , image).execute();


        list = new ArrayList<>();
        list.add(new bean("price" , price));
        list.add(new bean("description" , desc));

        singleAdapter adapter = new singleAdapter(this , R.layout.single_product , list);
        lv.setAdapter(adapter);


        add.setOnClickListener(this);






    }

    private Bitmap LoadImageFromURL(String url)

    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            return d;
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void onClick(View v) {

        if (v == add)
        {
            Intent i = new Intent(getApplicationContext() , AddtoList.class);

            i.putExtra("listId" , iidd);


            startActivity(i);
        }
    }


    public class loadImage extends AsyncTask<Void , Void , Void>
    {

        String url;
        ImageView iv;
        Bitmap d;

        public loadImage(ImageView iv , String url)
        {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {


            Log.d("asdasdasd" , url);
            d = LoadImageFromURL(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            iv.setImageBitmap(d);

        }
    }

}
