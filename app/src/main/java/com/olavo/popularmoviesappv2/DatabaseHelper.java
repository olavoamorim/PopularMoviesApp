package com.olavo.popularmoviesappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olavo on 3/30/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "favorites.db";
    //public static final String TABLE_NAME = "favorites_table";
    //public static final String COL_1 = "ID";
    //public static final String COL_2 = "MOVIE_ID";
    //public static final String COL_3 = "MOVIE_TITLE";
    //public static final String COL_4 = "POSTER_DETAIL";
    //public static final String COL_5 = "RELEASE_DATE";
    //public static final String COL_6 = "RATING";
    //public static final String COL_7 = "SYNOPSIS";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FavoritesDb.FavoritesEntry.TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, MOVIE_ID INTEGER, MOVIE_TITLE TEXT, POSTER_DETAIL TEXT, RELEASE_DATE TEXT, RATING DOUBLE, SYNOPSIS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+FavoritesDb.FavoritesEntry.TABLE_NAME);
    }

    public boolean insertData (int movie_id, String movie_title, String poster_detail, String release_date, String rating, String synopsis){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesDb.FavoritesEntry.COL_2, movie_id);
        contentValues.put(FavoritesDb.FavoritesEntry.COL_3, movie_title);
        contentValues.put(FavoritesDb.FavoritesEntry.COL_4, poster_detail);
        contentValues.put(FavoritesDb.FavoritesEntry.COL_5, release_date);
        contentValues.put(FavoritesDb.FavoritesEntry.COL_6, rating);
        contentValues.put(FavoritesDb.FavoritesEntry.COL_7, synopsis);
        long result = db.insert(FavoritesDb.FavoritesEntry.TABLE_NAME,null,contentValues);
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }


    public List<MovieResults.ResultsBean> getAllData(){
        String[] columns = {FavoritesDb.FavoritesEntry.COL_1, FavoritesDb.FavoritesEntry.COL_2, FavoritesDb.FavoritesEntry.COL_3, FavoritesDb.FavoritesEntry.COL_4, FavoritesDb.FavoritesEntry.COL_5, FavoritesDb.FavoritesEntry.COL_6, FavoritesDb.FavoritesEntry.COL_7};

        String sortOrder = FavoritesDb.FavoritesEntry.COL_1;
        List<MovieResults.ResultsBean> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoritesDb.FavoritesEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                MovieResults.ResultsBean movie = new MovieResults.ResultsBean();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoritesDb.FavoritesEntry.COL_2))));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(FavoritesDb.FavoritesEntry.COL_3)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex(FavoritesDb.FavoritesEntry.COL_4)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(FavoritesDb.FavoritesEntry.COL_5)));
                movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(FavoritesDb.FavoritesEntry.COL_6)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoritesDb.FavoritesEntry.COL_7)));

                favoriteList.add(movie);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FavoritesDb.FavoritesEntry.TABLE_NAME, "MOVIE_ID = ?", new String[] {id});
    }

}
