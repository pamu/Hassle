package com.iit_mandi.android.hassle.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pamu on 2/4/15.
 */
public class MenuContract {
    public static final String CONTENT_AUTHORITY = "com.iit_mandi.android.hassle";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DAY_INFO = "dayinfo";

    public final static class DayInfo implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(PATH_DAY_INFO).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAY_INFO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAY_INFO;

        public static final String TABLE_NAME = "day_info";
        public static final String SLOT_NUMBER = "slotNo";
        public static final String SLOT_NAME = "slotName";
        public static final String START_TIME = "startTime";
        public static final String SPAN = "span";
        public static final String SPECIAL = "special";
        public static final String DESCRIPTION = "description";

        public static Uri buildDayInfoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSlotInfoUri(long id) {
            return CONTENT_URI.buildUpon().appendQueryParameter(DayInfo._ID, Long.valueOf(id).toString()).build();
        }
    }
}
