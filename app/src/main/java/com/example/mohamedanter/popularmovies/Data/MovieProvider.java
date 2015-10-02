package com.example.mohamedanter.popularmovies.Data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Mohamed Anter on 9/20/2015.
 */
public class MovieProvider extends ContentProvider {
    static final int HIGHEST_RATED_MOVIES = 10;
    static final int HIGHEST_RATED_MOVIES_WITH_MOVIE_ID = 11;
    static final int MOST_POPULAR_MOVIES = 12;
    static final int MOST_POPULAR_MOVIES_WITH_MOVIE_ID = 13;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String MOVIES_WITH_MOVIE_ID =
            MovieContract.COLUMN_MOVIE_ID + " = ? ";
    private MovieDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.MOST_POPULAR_PATH, MOST_POPULAR_MOVIES);
        matcher.addURI(authority, MovieContract.HIGHEST_RATED_PATH, HIGHEST_RATED_MOVIES);
        matcher.addURI(authority, MovieContract.MOST_POPULAR_PATH + "/#", MOST_POPULAR_MOVIES_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.HIGHEST_RATED_PATH + "/#", HIGHEST_RATED_MOVIES_WITH_MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    private Cursor getPopularMovieWithMovieId(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.PopularMoviesEntry.TABLE_NAME,
                projection,
                MOVIES_WITH_MOVIE_ID,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getHighestMovieWithMovieId(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.HighestMoviesEntry.TABLE_NAME,
                projection,
                MOVIES_WITH_MOVIE_ID,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOST_POPULAR_MOVIES:
                return MovieContract.PopularMoviesEntry.CONTENT_TYPE;
            case HIGHEST_RATED_MOVIES:
                return MovieContract.HighestMoviesEntry.CONTENT_TYPE;
            case MOST_POPULAR_MOVIES_WITH_MOVIE_ID:
                return MovieContract.PopularMoviesEntry.CONTENT_ITEM_TYPE;
            case HIGHEST_RATED_MOVIES_WITH_MOVIE_ID:
                return MovieContract.HighestMoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOST_POPULAR_MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.PopularMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOST_POPULAR_MOVIES_WITH_MOVIE_ID:
                retCursor = getPopularMovieWithMovieId(uri, projection, sortOrder);
                break;
            case HIGHEST_RATED_MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.HighestMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case HIGHEST_RATED_MOVIES_WITH_MOVIE_ID:
                retCursor = getHighestMovieWithMovieId(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOST_POPULAR_MOVIES:
                long _id = db.insert(MovieContract.PopularMoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.PopularMoviesEntry.buildMoviesUriWithID(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case HIGHEST_RATED_MOVIES:
                _id = db.insert(MovieContract.HighestMoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.HighestMoviesEntry.buildMoviesUriWithID(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOST_POPULAR_MOVIES:
                rowsDeleted = db.delete(
                        MovieContract.PopularMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HIGHEST_RATED_MOVIES:
                rowsDeleted = db.delete(
                        MovieContract.HighestMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOST_POPULAR_MOVIES:
                rowsUpdated = db.update(
                        MovieContract.PopularMoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case HIGHEST_RATED_MOVIES:
                rowsUpdated = db.update(
                        MovieContract.HighestMoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOST_POPULAR_MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.PopularMoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case HIGHEST_RATED_MOVIES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.HighestMoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}

