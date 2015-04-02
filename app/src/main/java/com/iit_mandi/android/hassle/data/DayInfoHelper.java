package com.iit_mandi.android.hassle.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iit_mandi.android.hassle.data.MenuContract.DayInfo;

/**
 * Created by pamu on 2/4/15.
 */
public class DayInfoHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "menu.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DayInfoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String TABLE_CREATE = "CREATE TABLE " + DayInfo.TABLE_NAME + " ( " +
                DayInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DayInfo.SLOT_NUMBER + " INTEGER NOT NULL, " +
                DayInfo.SLOT_NAME + " TEXT NOT NULL, " +
                DayInfo.START_TIME + " TEXT NOT NULL, " +
                DayInfo.SPAN + " INTEGER NOT NULL, " +
                DayInfo.SPECIAL + " TEXT, " +
                DayInfo.DESCRIPTION + " TEXT NOT NULL );";
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DayInfo.TABLE_NAME);
        onCreate(db);
    }
}
