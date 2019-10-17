package com.mulight.demo.photobooth.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mulight.demo.photobooth.ui.main.PhotoContent;

import java.util.ArrayList;

/**
 * DB Utilities
 */
public class DBUtil {

    private static final String DB_NAME = "photo.db";
    private static final String TABLE_NAME = "photos";
    private static final int VERSION = 1;

    private static DBUtil dbUtil;

    private SQLiteDatabase db;

    private DBUtil(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DBUtil getInstance(Context context) {
        if (dbUtil == null) {
            dbUtil = new DBUtil(context);
        }
        return dbUtil;
    }

    public void savePhoto(String name, String path) {
        ContentValues cValue = new ContentValues();

        cValue.put("name",name);
        cValue.put("path",path);

        db.insert(TABLE_NAME,null,cValue);
    }

    public ArrayList<PhotoContent.PhotoItem> queryPhotoItems(){
        ArrayList<PhotoContent.PhotoItem> arrayList = new ArrayList<>();

        Cursor cursor = db.query (TABLE_NAME,null,null,null,null,null,null);

        if (cursor.moveToFirst()){

            while (!cursor.isClosed() && !cursor.isAfterLast()) {

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String path = cursor.getString(2);
                String creatTime = cursor.getString(3);

                arrayList.add(new PhotoContent.PhotoItem(id, name, path, creatTime));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return arrayList;
    }


    public void release() {
        if (db != null) {
            db.close();
            dbUtil = null;
        }
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE photos  ("
                    + "_id INTEGER PRIMARY KEY,"
                    + "name TEXT,"
                    + "path TEXT,"
                    + "create_time TIMESTAMP DEFAULT (datetime('now','localtime'))"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}