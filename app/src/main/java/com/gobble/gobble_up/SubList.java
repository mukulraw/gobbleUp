package com.gobble.gobble_up;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;




public class SubList extends AppCompatActivity {

    ArrayList<offlineSubListBean> list;
    private RecyclerView lv;
    SubListAdapter adapter;
    String id;
    private TextView total;
    LinearLayout layoutToShare;
    private GridLayoutManager lLayout;
    DBHandler handler;
    ConnectionDetector cd;
    FloatingActionButton shareMyList;
    int height , wil;
    Bitmap shareBitmap;
    View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        handler = new DBHandler(this);
        cd = new ConnectionDetector(this);

        shareMyList = (FloatingActionButton) findViewById(R.id.share_list);

        layoutToShare = (LinearLayout)findViewById(R.id.layout_to_share);


        shareMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // tela();
                makePDF();




            }
        });




        total = (TextView)findViewById(R.id.total);

        Bundle b = getIntent().getExtras();

        id = String.valueOf(b.get("id"));

        Log.d("asdasdasd" , id);

        lLayout = new GridLayoutManager(this , 1);

        lv = (RecyclerView) findViewById(R.id.sub_list);
        list = new ArrayList<>();



        //refresh();

        getFromOffline(id);




    }







    public void makePDF()
    {
        String fpath = null;
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

        try {
            fpath = "/sdcard/gobble_up.pdf";
            File file = new File(fpath);
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            Paragraph paragraph = new Paragraph();

            // step 1
            Document document = new Document();
            // step 2
            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            // step 3
            document.addTitle("Gobble Up shared sheet");
            document.setPageSize(PageSize.LETTER);
            document.open();


            Drawable d = getResources().getDrawable(R.drawable.header);

            BitmapDrawable bitDw = ((BitmapDrawable) d);

            Bitmap bmp = bitDw.getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            Image image = Image.getInstance(stream.toByteArray());

            //document.add(image);

            // step 4

            float[] columnWidths = {5f, 2f};

            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);



            insertCell(table, "Product Name", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Price", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);
            insertCell(table, "", Element.ALIGN_LEFT, 4, bfBold12);

            for (int i = 0 ; i < list.size() ; i++)
            {
                insertCell(table, list.get(i).getName() , Element.ALIGN_LEFT, 1, bf12);
                insertCell(table,  list.get(i).getPrice(), Element.ALIGN_CENTER, 1, bf12);
                //document.add(new Paragraph(list.get(i).getName()));
            }

            PdfPCell cell = new PdfPCell(new Phrase("Gobble up shared list", bfBold12));

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            paragraph.add(table);

            PdfPTable table2 = new PdfPTable(1);


            table2.addCell(cell);

            paragraph.add(table2);

            document.add(image);
            document.add(paragraph);


            // step 5
            document.close();

            Log.d("Suceess", "Sucess");
        } catch (IOException e) {
            e.printStackTrace();

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }



        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(fpath);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fpath));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }


    }


    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }




    public void getFromOffline(String iidd)
    {
        adapter = new SubListAdapter(getBaseContext() , list);
        lv.setAdapter(adapter);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(lLayout);


        RecyclerView.ItemDecoration itemDecoration = new
                SimpleDividerItemDecoration(this, SimpleDividerItemDecoration.VERTICAL_LIST);


        lv.addItemDecoration(itemDecoration);


        List<offlineSubListBean> ll = handler.getSubData(iidd);


        for (int i=0 ; i<ll.size();i++)
        {
            try {

                offlineSubListBean bean = ll.get(i);




                list.add(bean);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        adapter.setGridData(list);

        total.setText("TOTAL:  "+String.valueOf(adapter.getTotal()));


        syncOffline();


    }


    public void syncOffline()
    {

        if (cd.isConnectingToInternet())
        {
            String GET_LIST_ITEMS = "http://nationproducts.in/global/api/listitems/listId/";
            new connect(GET_LIST_ITEMS + id).execute();
        }

    }


    public void refresh()
    {
        //ArrayList<offlineSubListBean> l2 = new ArrayList<>();

        list.clear();

        List<offlineSubListBean> ll = handler.getSubData(id);
        for (int i=0 ; i<ll.size();i++)
        {
            try {

                offlineSubListBean bean = ll.get(i);




                list.add(bean);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        adapter.setGridData(list);
        total.setText("TOTAL:  "+String.valueOf(adapter.getTotal()));
    }

    public class connect extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;




        int length;
        String url;

        connect(String url)
        {
            this.url = url;
        }




        @Override
        protected Void doInBackground(Void... params) {


            try {

                URL u = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)u.openConnection();
                if(connection.getResponseCode()==200)
                {
                    is = connection.getInputStream();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
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
                //Log.e("JSON Parser", "Error parsing data " + e.toString());
            }


            for (int i=0 ; i<length;i++)
            {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    offlineSubListBean bean = new offlineSubListBean();
                    bean.setName(obj.getString("name"));
                    bean.setListId(obj.getString("listId"));


                    ImageLoader imageLoader = ImageLoader.getInstance();

                    Bitmap bitmap = imageLoader.loadImageSync(obj.getString("image"));

                    bean.setImage(Utils.getImageBytes(bitmap));


                    bean.setProductId(obj.getString("productId"));
                    //bean.setImage(obj.getString("image"));

                    bean.setPrice(obj.getString("price"));

                    handler.insertSubData(bean);

                    //list.add(bean);
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //adapter = new SubListAdapter(getBaseContext() , list);
            //lv.setAdapter(adapter);
            //lv.setHasFixedSize(true);
            //lv.setLayoutManager(lLayout);

            refresh();


            //adapter.setGridData(list);
            //total.setText("TOTAL:  "+String.valueOf(adapter.getTotal()));
            //list.clear();
            //mProgressBar.setVisibility(View.GONE);
        }
    }


    private class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.RecyclerViewHolder> {

        Context context;
    private ArrayList<offlineSubListBean> list1 = new ArrayList<>();
    private String DELETE_LIST = "http://nationproducts.in/global/api/removefromlist";

    String lid , pid , result;



        class RecyclerViewHolder extends RecyclerView.ViewHolder{
            ImageView sublistImage;
            TextView sublistName , subListPrice;
            ImageButton delete;

            RecyclerViewHolder(View itemView) {
                super(itemView);

                sublistImage = (ImageView)itemView.findViewById(R.id.sublistImage);
                sublistName = (TextView) itemView.findViewById(R.id.subListName);
                subListPrice = (TextView)itemView.findViewById(R.id.subListPrice);
                delete = (ImageButton)itemView.findViewById(R.id.sub_list_delete);

            }
        }



    SubListAdapter(Context context, ArrayList<offlineSubListBean> list) {
        this.context = context;
        this.list1 = list;
    }

    public void setGridData(ArrayList<offlineSubListBean> mGridData) {
        this.list1 = mGridData;
        notifyDataSetChanged();
    }

        @Override
        public SubListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_list_model, null);
            return new RecyclerViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int pos) {
            final offlineSubListBean item = list1.get(holder.getAdapterPosition());


            holder.setIsRecyclable(false);



            holder.sublistImage.setImageBitmap(Utils.getImage(item.getImage()));







            holder.sublistName.setText(item.getName());
            holder.subListPrice.setText(item.getPrice());


            if (cd.isConnectingToInternet())
            {
                try
                {
                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (cd.isConnectingToInternet()) {


                                lid = item.getListId();
                                pid = item.getProductId();


                                final Dialog dialog = new Dialog(SubList.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.delete_list_dialog);
                                dialog.setCancelable(false);
                                dialog.show();

                                Button YES = (Button) dialog.findViewById(R.id.confirmDelete);
                                Button NO = (Button) dialog.findViewById(R.id.cancel_delete_list);

                                YES.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        new delete1(DELETE_LIST).execute();
                                        dialog.dismiss();


                                    }
                                });

                                NO.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });


                            }
                            else
                            {
                                Toast.makeText(context , "No Internet Connection" , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

            }


        }

        @Override
        public int getItemCount() {
            return list1.size();
        }





    public float getTotal()
    {
        float sum = 0;
        for (int i = 0 ; i<getItemCount() ; i++)
        {
            offlineSubListBean item = list1.get(i);
            float temp = Float.parseFloat(item.getPrice());
            sum = sum+temp;


        }
        return sum;
    }



    class delete1 extends AsyncTask<Void , Void , Void>
    {

        InputStream is;
        String json;
        JSONArray array;




        int length;
        String url;

        delete1(String url)
        {
            this.url = url;
        }





        @Override
        protected Void doInBackground(Void... params) {


            List<NameValuePair> data = new ArrayList<>();

            data.add(new BasicNameValuePair("listId" , lid));
            data.add(new BasicNameValuePair("id" , pid));

            Log.d("asdasdsdlistId" , lid);
            Log.d("asdasdaProductId" , pid);


            RegisterUserClass ruc = new RegisterUserClass();
            result = ruc.sendPostRequest(url , data);
            try {
                JSONObject obj = new JSONObject(result);

                Log.d("asdasdasd" , String.valueOf(obj));


            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            handler.deleteSubList(lid , pid);

            syncOffline();

          //refresh();


        }
    }







}



}
