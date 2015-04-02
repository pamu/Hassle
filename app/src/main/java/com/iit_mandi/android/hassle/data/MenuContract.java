package com.iit_mandi.android.hassle.data;

import android.provider.BaseColumns;

/**
 * Created by pamu on 2/4/15.
 */
public class MenuContract {
    public static final String CONTENT_AUTHORITY = "com.iit_mandi.android.hassle";
    public
    public final static class DayInfo implements BaseColumns {
        public static final String TABLE_NAME = "day_info";
        public static final String SLOT_NUMBER = "slotNo";
        public static final String SLOT_NAME = "slotName";
        public static final String START_TIME = "startTime";
        public static final String SPAN = "span";
        public static final String SPECIAL = "special";
        public static final String DESCRIPTION = "description";
    }
}
