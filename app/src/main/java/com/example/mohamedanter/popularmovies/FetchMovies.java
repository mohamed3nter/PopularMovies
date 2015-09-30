package com.example.mohamedanter.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mohamedanter.popularmovies.Data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Mohamed Anter on 9/20/2015.
 */
public class FetchMovies extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchMovies.class.getSimpleName();
    private final Context mContext;

    public FetchMovies(Context context) {
        mContext = context;
    }

    private void getMoviesDataFromJson(String forecastJsonStr, String SortMethod)
            throws JSONException {
        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray DataArray = forecastJson.getJSONArray("results");
            Vector<ContentValues> cVVector = new Vector<ContentValues>(DataArray.length());
            String MOVIE_ID;
            String TITLE;
            String ORIGINAL_TITLE;
            String OVERVIEW;
            String VOTE_COUNT;
            String VOTE_AVARAGE;
            String RELEASE_DATE;
            String POSTER_PATH;
            for (int i = 0; i < DataArray.length(); i++) {
                JSONObject MovieOpj = DataArray.getJSONObject(i);
                MOVIE_ID = MovieOpj.getString("id");
                TITLE = MovieOpj.getString("title");
                ORIGINAL_TITLE = MovieOpj.getString("original_title");
                OVERVIEW = MovieOpj.getString("overview");
                VOTE_COUNT = MovieOpj.getString("vote_count");
                VOTE_AVARAGE = MovieOpj.getString("vote_average");
                RELEASE_DATE = MovieOpj.getString("release_date");
                POSTER_PATH = MovieOpj.getString("poster_path");
                ContentValues MovieValues = new ContentValues();

                MovieValues.put(MovieContract.COLUMN_MOVIE_ID, Long.parseLong(MOVIE_ID));
                MovieValues.put(MovieContract.COLUMN_TITLE, TITLE);
                MovieValues.put(MovieContract.COLUMN_ORIGINAL_TITLE, ORIGINAL_TITLE);
                MovieValues.put(MovieContract.COLUMN_OVERVIEW, OVERVIEW);
                MovieValues.put(MovieContract.COLUMN_VOTE_COUNT, Long.parseLong(VOTE_COUNT));
                MovieValues.put(MovieContract.COLUMN_VOTE_AVARAGE, Double.parseDouble(VOTE_AVARAGE));
                MovieValues.put(MovieContract.COLUMN_RELEASE_DATE, RELEASE_DATE);
                MovieValues.put(MovieContract.COLUMN_POSTER_PATH, POSTER_PATH);
                cVVector.add(MovieValues);
                Log.d(LOG_TAG, "//////////////////////////////////////////////////  POSTER_PATH ->. " + i + "=" + POSTER_PATH);
                //Log.d(LOG_TAG, "//////////////////////////////////////////////////  Movies num ->. " + i);
            }
            if (SortMethod.equals("Popular")) {
                Uri popularMoviesUri = MovieContract.PopularMoviesEntry.CONTENT_URI;
                int deleted = 0;
                deleted = mContext.getContentResolver().delete(popularMoviesUri, null, null);
                Log.d(LOG_TAG, "//////////////////////////////////////////////////Row Deleted From " + SortMethod + "Table " + deleted);
                int inserted = 0;
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(popularMoviesUri, cvArray);
                }
                Log.d(LOG_TAG, "////////////////////////////////////////////////// Inserted " + SortMethod + "Table " + inserted);
            } else {
                Uri HighestMoviesUri = MovieContract.HighestMoviesEntry.CONTENT_URI;
                int deleted = 0;
                //delete data from database
                deleted = mContext.getContentResolver().delete(HighestMoviesUri, null, null);
                Log.d(LOG_TAG, "//////////////////////////////////////////////////Row Deleted From " + SortMethod + "Table " + deleted);
                int inserted = 0;
                // add to database
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(HighestMoviesUri, cvArray);
                }
                Log.d(LOG_TAG, "////////////////////////////////////////////////// Inserted " + SortMethod + "Table " + inserted);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;
        String SelectionQuery = params[1];
        try {
            URL url = new URL(params[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
            Log.d(LOG_TAG, "////////////////////////////////////////////////// Fetch Movies Complete. ");
            getMoviesDataFromJson(forecastJsonStr, SelectionQuery);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }
}