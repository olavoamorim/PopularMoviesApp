package com.olavo.popularmoviesappv2;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Olavo on 4/3/2018.
 */

public class FavoritesDb {

    public static final String AUTHORITY = "com.olavo.popularmoviesappv2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";


public static final class FavoritesEntry implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

    //public static final String DATABASE_NAME = "favorites.db";
    public static final String TABLE_NAME = "favorites_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "MOVIE_ID";
    public static final String COL_3 = "MOVIE_TITLE";
    public static final String COL_4 = "POSTER_DETAIL";
    public static final String COL_5 = "RELEASE_DATE";
    public static final String COL_6 = "RATING";
    public static final String COL_7 = "SYNOPSIS";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " (" +
                    COL_1 + " integer PRIMARY KEY autoincrement," +
                    COL_2 + "," +
                    COL_3 + "," +
                    COL_4 + "," +
                    COL_5 + "," +
                    COL_6 + "," +
                    COL_7;

    public static void onCreate(SQLiteDatabase db) {
        Log.w("Error LOG_TAG", DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Error LOG_TAG", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
}