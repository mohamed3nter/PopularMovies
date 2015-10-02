package com.example.mohamedanter.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mohamed Anter on 10/2/2015.
 */
public class FetchJSON_STR extends AsyncTask<Void, Void, Void> {
    private final String LOG_TAG = FetchJSON_STR.class.getSimpleName();
    public String forecastJsonStr = null;
    public ArrayList<Movie> Data;
    public String[] Trailer;
    public String[] Review;

    public FetchJSON_STR(ArrayList<Movie> Data) {
        this.Data = Data;
        Trailer = new String[Data.size()];
        Review = new String[Data.size()];
        this.execute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
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
}
