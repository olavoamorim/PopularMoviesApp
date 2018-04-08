package com.olavo.popularmoviesappv2;

import android.provider.BaseColumns;

/**
 * Created by Olavo on 4/7/2018.
 */

public class FavoritesContract {


    public static final class FavoritesEntry implements BaseColumns{
        //public static final String DATABASE_NAME = "favorites.db";
        public static final String TABLE_NAME = "favorites_table";
        public static final String COL_1 = "ID";
        public static final String COL_2 = "MOVIE_ID";
        public static final String COL_3 = "MOVIE_TITLE";
        public static final String COL_4 = "POSTER_DETAIL";
        public static final String COL_5 = "RELEASE_DATE";
        public static final String COL_6 = "RATING";
        public static final String COL_7 = "SYNOPSIS";
    }
}
