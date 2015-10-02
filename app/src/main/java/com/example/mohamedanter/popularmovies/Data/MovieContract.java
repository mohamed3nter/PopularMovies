package com.example.mohamedanter.popularmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mohamed Anter on 9/20/2015.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.mohamedanter.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String MOST_POPULAR_PATH = "MOST_POPULAR_Movies";
    public static final String HIGHEST_RATED_PATH = "HIGHEST_RATED_Movies";

    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_VOTE_COUNT = "vote_count";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_Trailer_JSON_STR = "trailer_json";
    public static final String COLUMN_REVIEW_JSON_STR = "review_json";

    public static final class PopularMoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOST_POPULAR_PATH).build();
        public static final String TABLE_NAME = "PopularMovies";

        public static Uri buildMoviesUriWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOST_POPULAR_PATH;

        public static Uri buildMoviesUriWithMovieId(Long movieId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOST_POPULAR_PATH;

    }

    public static final class HighestMoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(HIGHEST_RATED_PATH).build();
        public static final String TABLE_NAME = "HighestMovies";

        public static Uri buildMoviesUriWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + HIGHEST_RATED_PATH;

        public static Uri buildMoviesUriWithMovieId(Long movieId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + HIGHEST_RATED_PATH;

    }
}

