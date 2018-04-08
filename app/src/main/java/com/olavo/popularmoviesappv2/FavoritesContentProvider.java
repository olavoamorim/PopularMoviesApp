package com.olavo.popularmoviesappv2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Olavo on 4/7/2018.
 */

public class FavoritesContentProvider extends ContentProvider {

    public static final int FAVORITE = 100;
    public static final int FAVORITE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoritesDb.AUTHORITY, FavoritesDb.PATH_FAVORITES, FAVORITE);
        uriMatcher.addURI(FavoritesDb.AUTHORITY, FavoritesDb.PATH_FAVORITES + "/#", FAVORITE_WITH_ID);

        return uriMatcher;
    }


    private DatabaseHelper mFavoritesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoritesDbHelper = new DatabaseHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavoritesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            case FAVORITE:
                retCursor = db.query(FavoritesDb.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "id=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(FavoritesDb.FavoritesEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mFavoritesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVORITE:
                long id = db.insert(FavoritesDb.FavoritesEntry.TABLE_NAME, null, values);
                if (id>0) {
                    returnUri = ContentUris.withAppendedId(FavoritesDb.FavoritesEntry.CONTENT_URI, id);
                } else {
                    throw new UnsupportedOperationException("Failed to insert row into " + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
