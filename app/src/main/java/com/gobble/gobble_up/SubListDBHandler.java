package com.gobble.gobble_up;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SubListDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;


    private static final String DATABASE_NAME = "dbGobble";

    private static final String KEY_ID = "id";
    private static final String SUB_TABLE = "subListData";

    private static final String LIST_ID = "listId";
    private static final String PROD_NAME = "name";
    private static final String PROD_PRICE = "price";
    private static final String PROD_IMAGE = "image";
    private static final String PROD_ID = "productId";

    String query2 = "CREATE TABLE "+SUB_TABLE+"("+KEY_ID+" INTEGER PRIMARY KEY,"+LIST_ID+" TEXT,"+PROD_NAME+" TEXT,"+PROD_PRICE+" TEXT,"+PROD_IMAGE+" BLOB,"+PROD_ID+" TEXT)";

    public SubListDBHandler(Context context) {
        super(context, DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(query2);

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


/*        Cursor cursor = db.query(SUB_TABLE , columnNames , whereClause , null , null , null , null);
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

        }*/

        //cursor.close();
        return list;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
