package com.gobble.gobble_up;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;


    private static final String DATABASE_NAME = "dbGobble";

    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "listId";
    private static final String KEY_NAME = "listName";

    private static final String KEY_ADDRESS = "createdTime";
    private static final String TOTAL_ITEMS = "totalItems";


    private static final String TABLE_NAME = "mainListData";
    private static final String SUB_TABLE = "subListData";

    private static final String LIST_ID = "listId";
    private static final String PROD_NAME = "name";
    private static final String PROD_PRICE = "price";
    private static final String PROD_IMAGE = "image";
    private static final String PROD_ID = "productId";
    String query = "CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_USER_ID+" TEXT,"+KEY_NAME+" TEXT,"+KEY_ADDRESS+" TEXT,"+TOTAL_ITEMS+" TEXT)";
    String query2 = "CREATE TABLE "+SUB_TABLE+"("+KEY_ID+" INTEGER PRIMARY KEY,"+LIST_ID+" TEXT,"+PROD_NAME+" TEXT,"+PROD_PRICE+" TEXT,"+PROD_IMAGE+" BLOB,"+PROD_ID+" TEXT)";
    String qu = "CREATE TABLE subListData(id INTEGER PRIMARY KEY, listId TEXT, name TEXT, price TEXT, image BLOB NOT NULL, productId TEXT)";
    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("asdasd" , query2);
        //db.execSQL(query2);
        //Log.d("asdasd" , query);
        db.execSQL(query);
    }



    public void insertSubData(offlineSubListBean item)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        Boolean exists = DatabaseUtils.longForQuery(db ,  "select count(*) from " + SUB_TABLE + " where "+PROD_NAME+"=? AND "+LIST_ID+"=? limit 1" , new String[]{item.getName() , item.getListId()})  > 0;



        if (exists)
        {
            ContentValues cv = new ContentValues();
            cv.put(LIST_ID , item.getListId());
            cv.put(PROD_NAME , item.getName());
            cv.put(PROD_PRICE , item.getPrice());
            cv.put(PROD_IMAGE , item.getImage());
            cv.put(PROD_ID , item.getProductId());

            db.insert(SUB_TABLE , null , cv);
            db.close();
        }


    }


    public List<offlineSubListBean> getSubData(String iidd)
    {
        List<offlineSubListBean> list = new ArrayList<>();
       // String query = "SELECT * FROM "+SUB_TABLE+" WHERE "+LIST_ID+"="+iidd;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columnNames = new String[] {KEY_ID , LIST_ID , PROD_NAME , PROD_PRICE , PROD_IMAGE , PROD_ID};
        String whereClause = LIST_ID+"="+iidd;

       // return mDb.query(DATABASE_TABLE_TIMETABLE, columnNames, whereClause, null, null, null, null);


        Cursor cursor = db.query(SUB_TABLE , columnNames , whereClause , null , null , null , null);
        if (cursor.moveToFirst())
        {
            do {
                offlineSubListBean item = new offlineSubListBean();
                item.setListId(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setPrice(cursor.getString(3));
                item.setImage(cursor.getBlob(4));
                item.setProductId(cursor.getString(5));
                // Adding contact to list
                list.add(item);
            } while (cursor.moveToNext());

        }

        cursor.close();
        return list;
    }



    void deleteMainList(String listId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_USER_ID + " = ?",
                new String[] { String.valueOf(listId) });
        db.close();
    }


    int updateMainListName(String listId , String text)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, text);

        // updating row
        return db.update(TABLE_NAME, values, KEY_USER_ID + " = ?",
                new String[] { listId });
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


        //db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS "+SUB_TABLE);
        //onCreate(db);


    }


    void insertUser(offlineMainListBean item)
    {
        SQLiteDatabase db = this.getWritableDatabase();


       // String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_NAME;
        //Cursor c = db.rawQuery(query , new String[]{item.getName()});


        Boolean exists = DatabaseUtils.longForQuery(db ,  "select count(*) from " + TABLE_NAME + " where "+KEY_NAME+"=? limit 1" , new String[]{item.getListName()})  > 0;

        if (!exists)
        {
            ContentValues cv = new ContentValues();
            cv.put(KEY_USER_ID , item.getListId());
            cv.put(KEY_NAME , item.getListName());
            cv.put(KEY_ADDRESS , item.getCreatedTime());
            cv.put(TOTAL_ITEMS , item.getTotalItems());
            db.insert(TABLE_NAME , null , cv);
            db.close();
        }

        //c.close();


    }


    List<offlineMainListBean> getAllusers()
    {
        List<offlineMainListBean> list = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query , null);
        if (cursor.moveToFirst())
        {
            do {
                offlineMainListBean item = new offlineMainListBean();
                item.setListId(cursor.getString(1));
                item.setListName(cursor.getString(2));
                item.setCreatedTime(cursor.getString(3));
                item.setTotalItems(cursor.getString(4));
                // Adding contact to list
                list.add(item);
            } while (cursor.moveToNext());

        }

        cursor.close();
        return list;

    }


    int getUsersCount() {
        String countQuery = "SELECT * FROM " +TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        // return count
        return count;
    }




}
