package com.iit_mandi.android.hassle;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.iit_mandi.android.hassle.data.DayInfoHelper;

/**
 * Created by pamu on 2/4/15.
 */
public class TestDb extends AndroidTestCase {
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DayInfoHelper.DATABASE_NAME);
        DayInfoHelper helper = new DayInfoHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }
}
