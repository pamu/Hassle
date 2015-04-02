package com.iit_mandi.android.hassle.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by pamu on 2/4/15.
 */
public class MenuProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMacther();
    private DayInfoHelper dayInfoHelper;

    static final int DAY_INFO = 100;
    static final int SLOT_INFO = 101;

    static UriMatcher buildUriMacther() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MenuContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MenuContract.PATH_DAY_INFO, DAY_INFO);
        matcher.addURI(authority, MenuContract.PATH_DAY_INFO + "/*", SLOT_INFO);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dayInfoHelper = new DayInfoHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case DAY_INFO:
                cursor = getDayInfo(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case SLOT_INFO:
                cursor = getSlotInfo(uri, projection, selection, selectionArgs, sortOrder);
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    public Cursor getDayInfo(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return dayInfoHelper.getReadableDatabase().query(MenuContract.DayInfo.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    public Cursor getSlotInfo(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return dayInfoHelper.getReadableDatabase().query(MenuContract.DayInfo.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DAY_INFO:
                return MenuContract.DayInfo.CONTENT_TYPE;
            case SLOT_INFO:
                return MenuContract.DayInfo.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dayInfoHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case DAY_INFO:
                long _id = db.insert(MenuContract.DayInfo.TABLE_NAME, null, values);
                if (_id > 0) {
                    Log.i("MenuProvider", Long.valueOf(_id).toString());
                    returnUri = MenuContract.DayInfo.buildDayInfoUri(_id);
                } else {
                    throw new SQLException("Failed to insert the row into " + MenuContract.DayInfo.TABLE_NAME);
                }
                break;
            default:
                throw new UnsupportedOperationException("unknown uri " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dayInfoHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        if (null == selection) selection = "1";

        switch (match) {
            case DAY_INFO:
                rowsDeleted = db.delete(MenuContract.DayInfo.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dayInfoHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match) {
            case DAY_INFO:
                rowsUpdated = db.update(MenuContract.DayInfo.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dayInfoHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DAY_INFO:
                db.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MenuContract.DayInfo.TABLE_NAME, null, value);
                        if (_id != -1) {
                            count ++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        dayInfoHelper.close();
        super.shutdown();
    }
}
