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
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Mohamed Anter on 9/20/2015.
 */
public class FetchMovies extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchMovies.class.getSimpleName();
    private final Context mContext;
    public String[] Trailer = new String[20];
    public String[] Review = new String[20];
    private ArrayList<Movie> Data;
    private String SelectionQuery;

    public FetchMovies(Context context) {
        mContext = context;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String forecastJsonStr)
            throws JSONException {
        ArrayList<Movie> MovieData = new ArrayList<Movie>();
        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray DataArray = forecastJson.getJSONArray("results");
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
                Movie Temp = new Movie(Long.parseLong(MOVIE_ID), TITLE, ORIGINAL_TITLE, OVERVIEW, Long.parseLong(VOTE_COUNT),
                        Double.parseDouble(VOTE_AVARAGE), RELEASE_DATE, POSTER_PATH, "", "");
                MovieData.add(Temp);
                Log.d(LOG_TAG, "//////////////////////////////////////////////////  TITLE ->. " + i + "=" + TITLE);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return MovieData;
    }

    private void InsertInDataBase(Vector<ContentValues> cVVector, String SortMethod) {
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
    }

    private Vector<ContentValues> ConvertArrayListToVector(ArrayList<Movie> Data) {
        int size = Data.size();
        Vector<ContentValues> cVVector = new Vector<ContentValues>(size);
        for (int i = 0; i < size; i++) {
            ContentValues MovieValues = new ContentValues();
            Movie MovieObject = Data.get(i);
            MovieValues.put(MovieContract.COLUMN_MOVIE_ID, MovieObject.MOVIE_ID);
            MovieValues.put(MovieContract.COLUMN_TITLE, MovieObject.TITLE);
            MovieValues.put(MovieContract.COLUMN_ORIGINAL_TITLE, MovieObject.ORIGINAL_TITLE);
            MovieValues.put(MovieContract.COLUMN_OVERVIEW, MovieObject.OVERVIEW);
            MovieValues.put(MovieContract.COLUMN_VOTE_COUNT, MovieObject.VOTE_COUNT);
            MovieValues.put(MovieContract.COLUMN_VOTE_AVERAGE, MovieObject.VOTE_AVARAGE);
            MovieValues.put(MovieContract.COLUMN_RELEASE_DATE, MovieObject.RELEASE_DATE);
            MovieValues.put(MovieContract.COLUMN_POSTER_PATH, MovieObject.POSTER_PATH);
            MovieValues.put(MovieContract.COLUMN_Trailer_JSON_STR, Trailer[i]);
            MovieValues.put(MovieContract.COLUMN_REVIEW_JSON_STR, Review[i]);
            cVVector.add(MovieValues);
        }
        return cVVector;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;
        SelectionQuery = params[1];
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
            Data = getMoviesDataFromJson(forecastJsonStr);
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
        urlConnection = null;
        reader = null;
        for (int i = 0; i < Data.size(); i++) {
            try {
                String ReviewUri = "http://api.themoviedb.org/3/movie/" + (Data.get(i).MOVIE_ID).toString() + "/reviews?api_key=1aa2b2989b33151515585a6ec53e0fa7";
                URL url = new URL(ReviewUri);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
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
            } catch (IOException e) {
                forecastJsonStr = null;
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
            Review[i] = forecastJsonStr;
        }
        for (int i = 0; i < Data.size(); i++) {
            try {
                String ReviewUri = "http://api.themoviedb.org/3/movie/" + (Data.get(i).MOVIE_ID).toString() + "/videos?api_key=1aa2b2989b33151515585a6ec53e0fa7";
                URL url = new URL(ReviewUri);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
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
            } catch (IOException e) {
                forecastJsonStr = null;
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
            Trailer[i] = forecastJsonStr;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Vector<ContentValues> cVVector = ConvertArrayListToVector(Data);
        InsertInDataBase(cVVector, SelectionQuery);
    }
}