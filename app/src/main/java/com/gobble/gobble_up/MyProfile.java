package com.gobble.gobble_up;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyProfile extends AppCompatActivity {

    TextView edit , name , age , phone , gender , height , weight , bmi;

    CircleImageView profilePic;
    private final int PICK_IMAGE_REQUEST = 2;
    Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        edit = (TextView)findViewById(R.id.edit_profile_button);

        profilePic = (CircleImageView)findViewById(R.id.profile_picture);

        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        phone = (TextView)findViewById(R.id.phone);
        gender = (TextView)findViewById(R.id.gender);
        height = (TextView)findViewById(R.id.height);
        weight = (TextView)findViewById(R.id.weight);
        bmi = (TextView)findViewById(R.id.bmi);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);

            }
        });

        String SUB_CATEGORY = "http://nationproducts.in/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SUB_CATEGORY)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CompareAPI request = retrofit.create(CompareAPI.class);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext() , UpdateProfile.class);
                startActivityForResult(i , 121);


            }
        });

        comparebean b = (comparebean)getApplicationContext();

        Call<profileBean> call = request.getProfile(b.user_id);

        call.enqueue(new Callback<profileBean>() {
            @Override
            public void onResponse(Call<profileBean> call, Response<profileBean> response) {



                try {

                    name.setText(response.body().getUserName());
                    age.setText(response.body().getAge());
                    phone.setText(response.body().getPhone());
                    gender.setText(response.body().getGender());
                    height.setText(response.body().getHeight() + "cm");
                    weight.setText(response.body().getWeight() + "kg");

                    Float bm = Float.parseFloat(response.body().getBmi());
                    String s = String.format("%.2f", bm);
                    bmi.setText(s);

                    ImageLoader loader = ImageLoader.getInstance();

                    loader.displayImage(response.body().getUserImage() , profilePic);


                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<profileBean> call, Throwable t) {

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 121)
        {
            String SUB_CATEGORY = "http://nationproducts.in/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SUB_CATEGORY)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final CompareAPI request = retrofit.create(CompareAPI.class);
            comparebean b = (comparebean)getApplicationContext();

            Call<profileBean> call = request.getProfile(b.user_id);

            call.enqueue(new Callback<profileBean>() {
                @Override
                public void onResponse(Call<profileBean> call, Response<profileBean> response) {



                    try {

                        name.setText(response.body().getUserName());
                        age.setText(response.body().getAge());
                        phone.setText(response.body().getPhone());
                        gender.setText(response.body().getGender());
                        height.setText(response.body().getHeight() + "cm");
                        weight.setText(response.body().getWeight() + "kg");
                        bmi.setText(response.body().getBmi());


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<profileBean> call, Throwable t) {

                }
            });


        }


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(String.valueOf(data.getData())));

                //browse_image.setImageBitmap(bitmap);
                Uri selectedImageUri = data.getData();

                //bean.setBrowse(bitmap);



                String mCurrentPhotoPath = getPath(getApplicationContext() , selectedImageUri);

                String SUB_CATEGORY = "http://nationproducts.in/";
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SUB_CATEGORY)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final CompareAPI request = retrofit.create(CompareAPI.class);

                comparebean b = (comparebean)getApplicationContext();

                File file = new File(mCurrentPhotoPath);



                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part body = MultipartBody.Part.createFormData("video", file.getName(), reqFile);

                Call<String> call = request.updateImage(b.user_id , body);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {


                        String SUB_CATEGORY = "http://nationproducts.in/";
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(SUB_CATEGORY)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        final CompareAPI request = retrofit.create(CompareAPI.class);
                        comparebean b = (comparebean)getApplicationContext();

                        Call<profileBean> call2 = request.getProfile(b.user_id);

                        call2.enqueue(new Callback<profileBean>() {
                            @Override
                            public void onResponse(Call<profileBean> call, Response<profileBean> response) {



                                try {
                                    ImageLoader loader = ImageLoader.getInstance();
                                    if (response.body().getUserImage().length()>0)
                                    {
                                        loader.displayImage(response.body().getUserImage() , profilePic);
                                    }





                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(Call<profileBean> call, Throwable t) {

                            }
                        });



                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String getPath(final Context context, final Uri uri)
    {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


}
