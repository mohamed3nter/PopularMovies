package com.example.mohamedanter.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Mohamed Anter on 10/1/2015.
 */
public class FetchReview extends AsyncTask<String, Void, String> {
    private final String LOG_TAG = FetchReview.class.getSimpleName();
    private final Context mContext;

    public FetchReview(Context context) {
        mContext = context;
    }

    private ArrayList<Review> getReviewDataFromJson(String forecastJsonStr)
            throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray DataArray = forecastJson.getJSONArray("results");
        ArrayList<Review> ReviewData = new ArrayList<Review>();
        for (int i = 0; i < DataArray.length(); i++) {
            JSONObject object = DataArray.getJSONObject(i);
            String REVIEW_AUTHOR = object.getString("author");
            String REVIEW_CONTENT = object.getString("content");
            Review temp = new Review(REVIEW_AUTHOR, REVIEW_CONTENT);
            ReviewData.add(temp);
        }
        return ReviewData;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;
        try {
            String ReviewUri = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?api_key=1aa2b2989b33151515585a6ec53e0fa7";
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
        return forecastJsonStr;
    }
}
