package com.example.mohamedanter.popularmovies;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamedanter.popularmovies.Data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohamed Anter on 9/29/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int LOADER = 0;
    private Uri mUri;
    private TextView t1;
    private ImageView img;
    private TextView t2;
    private TextView t3;
    private TextView t4;

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
                Content_Value.getAsDouble(MovieContract.COLUMN_VOTE_AVARAGE),
                Content_Value.getAsString(MovieContract.COLUMN_RELEASE_DATE),
                Content_Value.getAsString(MovieContract.COLUMN_POSTER_PATH));
        t1.setText(SelectedItem.ORIGINAL_TITLE);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185//" + SelectedItem.POSTER_PATH).into(img);
        String Time[] = SelectedItem.RELEASE_DATE.split("-");
        t2.setText((CharSequence) Time[0]);
        t3.setText("120min");
        t4.setText(SelectedItem.VOTE_AVARAGE + "/10");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}