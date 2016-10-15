package com.zhexinj.tideapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhexinjia on 7/8/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TideInfo.db";
    private static final int DATABASE_VERSION = 1;
    private Context context = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TideInfo"
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " City TEXT,"
                + " Date TEXT,"
                + " Day TEXT,"
                + " Time TEXT,"
                + " HighLow TEXT,"
                + " Feet TEXT,"
                + " CM TEXT"
                + ");" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
