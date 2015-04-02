package com.iit_mandi.android.hassle;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.iit_mandi.android.hassle.data.MenuContract;

/**
 * Created by pamu on 3/4/15.
 */
public class TestProvider extends AndroidTestCase {
    public void testProvider() throws Throwable {
        ContentValues values = new ContentValues();
        values.put(MenuContract.DayInfo.SLOT_NUMBER, 0);
        values.put(MenuContract.DayInfo.SLOT_NAME, "BreadFast");
        values.put(MenuContract.DayInfo.START_TIME, "7:00");
        values.put(MenuContract.DayInfo.SPAN, 2);
        values.put(MenuContract.DayInfo.SPECIAL, "Parata");
        values.put(MenuContract.DayInfo.DESCRIPTION, "Parata with curd");
        getContext().getContentResolver().insert(MenuContract.DayInfo.CONTENT_URI, values);
        Cursor cursor = getContext().getContentResolver().query(MenuContract.DayInfo.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i ++) {
                System.out.println(cursor.getString(i));
                Log.d("test provider", cursor.getString(i));
            }
        }
        assertTrue(cursor.getCount() == 1);
    }
}
