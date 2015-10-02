package com.example.mohamedanter.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohamed Anter on 9/20/2015.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_POPULAR_MOVIES_TABLE = "CREATE TABLE " + MovieContract.PopularMoviesEntry.TABLE_NAME + " (" +
                MovieContract.PopularMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_MOVIE_ID + " LONG NOT NULL, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_COUNT + " LONG NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_Trailer_JSON_STR + " TEXT NOT NULL, " +
                MovieContract.COLUMN_REVIEW_JSON_STR + " TEXT NOT NULL);";

        final String SQL_CREATE_HIGHEST_MOVIES_TABLE = "CREATE TABLE " + MovieContract.HighestMoviesEntry.TABLE_NAME + " (" +
                MovieContract.PopularMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_MOVIE_ID + " LONG NOT NULL, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_COUNT + " LONG NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_Trailer_JSON_STR + " TEXT NOT NULL, " +
                MovieContract.COLUMN_REVIEW_JSON_STR + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_POPULAR_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_HIGHEST_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.HighestMoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.PopularMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}