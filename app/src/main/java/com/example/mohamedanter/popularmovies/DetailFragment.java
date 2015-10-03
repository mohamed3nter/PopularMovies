package com.example.mohamedanter.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mohamedanter.popularmovies.Data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed Anter on 9/29/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int LOADER = 0;
    TrailerAdaptor myadp2;
    ReviewAdaptor myadp1;
    private Uri mUri;
    private TextView t1;
    private ImageView img;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private ListView TrailerList;
    private ListView ReviewList;
    private ArrayList<Review> ReviewData;
    private ArrayList<Trailer> TrailerData;
    public DetailFragment() {
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        t1 = (TextView) rootView.findViewById(R.id.Movie_name);
        img = (ImageView) rootView.findViewById(R.id.image);
        t2 = (TextView) rootView.findViewById(R.id.Year);
        t3 = (TextView) rootView.findViewById(R.id.Lenth);
        t4 = (TextView) rootView.findViewById(R.id.rate);
        t5 = (TextView) rootView.findViewById(R.id.review);
        TrailerList = (ListView) rootView.findViewById(R.id.TrailerlistView);
        TrailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer temp = (Trailer) myadp2.getItem(position);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + temp.TrailerKey));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + temp.TrailerKey));
                    startActivity(intent);
                }
            }
        });
        ReviewList = (ListView) rootView.findViewById(R.id.ReviewlistView);
        return rootView;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void ChangeMovies() {
        Uri uri = mUri;
        if (null != uri) {
            getLoaderManager().restartLoader(LOADER, null, this);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst())
            return;
        ContentValues Content_Value = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(data, Content_Value);
        Movie SelectedItem = new Movie(Content_Value.getAsLong(MovieContract.COLUMN_MOVIE_ID),
                Content_Value.getAsString(MovieContract.COLUMN_TITLE),
                Content_Value.getAsString(MovieContract.COLUMN_ORIGINAL_TITLE),
                Content_Value.getAsString(MovieContract.COLUMN_OVERVIEW),
                Content_Value.getAsLong(MovieContract.COLUMN_VOTE_COUNT),
                Content_Value.getAsDouble(MovieContract.COLUMN_VOTE_AVERAGE),
                Content_Value.getAsString(MovieContract.COLUMN_RELEASE_DATE),
                Content_Value.getAsString(MovieContract.COLUMN_POSTER_PATH),
                Content_Value.getAsString(MovieContract.COLUMN_Trailer_JSON_STR),
                Content_Value.getAsString(MovieContract.COLUMN_REVIEW_JSON_STR));
        try {
            ReviewData = getReviewDataFromJson(SelectedItem.REVIEW_JSON_STR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            TrailerData = getTrailerDataFromJson(SelectedItem.Trailer_JSON_STR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myadp1 = new ReviewAdaptor(getActivity(), ReviewData);
        ReviewList.setAdapter(myadp1);
        myadp2 = new TrailerAdaptor(getActivity(), TrailerData);
        TrailerList.setAdapter(myadp2);
        t1.setText(SelectedItem.ORIGINAL_TITLE);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185//" + SelectedItem.POSTER_PATH).into(img);
        String Time[] = SelectedItem.RELEASE_DATE.split("-");
        t2.setText((CharSequence) Time[0]);
        t3.setText("120min");
        t4.setText(SelectedItem.VOTE_AVARAGE + "/10");
        t5.setText(SelectedItem.OVERVIEW);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private ArrayList<Review> getReviewDataFromJson(String forecastJsonStr)
            throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        Log.d(LOG_TAG, "//////////////////////////////////////////////////" + forecastJsonStr);
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
    private ArrayList<Trailer> getTrailerDataFromJson(String forecastJsonStr)
            throws JSONException {
        Log.d(LOG_TAG, "//////////////////////////////////////////////////" + forecastJsonStr);
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray DataArray = forecastJson.getJSONArray("results");
        ArrayList<Trailer> TrailerData = new ArrayList<Trailer>();
        for (int i = 0; i < DataArray.length(); i++) {
            JSONObject object = DataArray.getJSONObject(i);
            String TrailerName = object.getString("name");
            String TrailerKey = object.getString("key");
            Trailer temp = new Trailer(TrailerName, TrailerKey);
            TrailerData.add(temp);
        }
        return TrailerData;
    }
}