package com.example.yami.yamiycp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

    private static MyDbHelper deHelper = null;

    public static MyDbHelper getInstance(Context context){
        if (deHelper == null){
            deHelper = new MyDbHelper(context);
        }
        return deHelper;
    }

    private MyDbHelper(Context context){
        super(context,"yamiycp.db",null,1);
    }


    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_class_table="create table if not exists classtable(_id integer primary key autoincrement,classtabledata text)";
        db.execSQL(sql_class_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
