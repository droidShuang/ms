package com.junova.ms.utils;

import android.database.Cursor;

/**
 * Created by junova on 2017-02-17.
 */

public class CursorUtil {
    public static String getStringOfColumn(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    public static int getIntOfColum(Cursor cursor, String colum) {
        return cursor.getInt(cursor.getColumnIndex(colum));
    }
}
