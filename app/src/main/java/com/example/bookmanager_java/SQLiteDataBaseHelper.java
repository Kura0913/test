package com.example.bookmanager_java;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    String TableName;


    public SQLiteDataBaseHelper(@Nullable Context context
            , @Nullable String dataBaseName
            , @Nullable SQLiteDatabase.CursorFactory factory, int version, String TableName) {
        super(context, dataBaseName, factory, version);
        this.TableName = TableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "BookName TEXT, " +
                "Author TEXT," +
                "Press TEXT," +
                "Counter TEXT" +
                ");";
        db.execSQL(SQLTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL = "DROP TABLE " + TableName;
        db.execSQL(SQL);

    }



    //檢查資料表狀態，若無指定資料表則新增
    public void chickTable(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0)
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "BookName TEXT, " +
                        "Author TEXT," +
                        "Press TEXT," +
                        "Counter TEXT" +
                        ");");
            cursor.close();
        }
    }
    //新增資料
    public void addData(String bookName, String author, String press, String counter) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("BookName", bookName);
        values.put("Author", author);
        values.put("Press", press);
        values.put("Counter", counter);
        db.insert(TableName, null, values);
    }

    //顯示所有資料
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName, null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String bookName = c.getString(1);
            String author = c.getString(2);
            String press = c.getString(3);
            String counter = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("bookName", bookName);
            hashMap.put("author", author);
            hashMap.put("press", press);
            hashMap.put("counter", counter);
            arrayList.add(hashMap);
        }
        return arrayList;

    }
    //以id搜尋特定資料
    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String bookName = c.getString(1);
            String author = c.getString(2);
            String press = c.getString(3);
            String counter = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("bookName", bookName);
            hashMap.put("author", author);
            hashMap.put("press", press);
            hashMap.put("counter", counter);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //以書名篩選資料
    public ArrayList<HashMap<String, String>> searchByBookName(String getBookName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE BookName =" + "'" + getBookName + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String id = c.getString(0);
            String bookName = c.getString(1);
            String author = c.getString(2);
            String press = c.getString(3);
            String counter = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("bookName", bookName);
            hashMap.put("author", author);
            hashMap.put("press", press);
            hashMap.put("counter", counter);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //修改資料
    public void modify(String id, String bookName, String author, String press, String counter) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" UPDATE " + TableName
                + " SET BookName=" + "'" + bookName + "',"
                + "Author=" + "'" + author + "',"
                + "Press=" + "'" + press + "',"
                + "Counter=" + "'" + counter + "'"
                + " WHERE _id=" + "'" + id + "'");
    }

    //更新書本數量
    public void ChangeCounter(String id, String bookName, String author, String press, int counter) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" UPDATE " + TableName
                + " SET BookName=" + "'" + bookName + "',"
                + "Author=" + "'" + author + "',"
                + "Press=" + "'" + press + "',"
                + "Counter=" + "'" + Integer.toString(counter) + "'"
                + " WHERE _id=" + "'" + id + "'");
    }

    //以id刪除資料
    public void deleteById(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }


}
