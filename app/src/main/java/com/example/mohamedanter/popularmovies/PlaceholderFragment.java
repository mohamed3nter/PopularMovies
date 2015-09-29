package com.example.mohamedanter.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mohamedanter.popularmovies.Data.MovieContract;

/**
 * Created by Mohamed Anter on 8/22/2015.
 */
public class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_ORIGINAL_TITLE = 3;
    static final int COL_OVERVIEW = 4;
    static final int COL_VOTE_COUNT = 5;
    static final int COL_VOTE_AVARAGE = 6;
    static final int COL_RELEASE_DATE = 7;
    static final int COL_POSTER_PATH = 8;
    private static final int FORECAST_LOADER = 0;
    private CustomAdaptor myadp;
    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myadp = new CustomAdaptor(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(myadp);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String sort = prefs.getString(getString(R.string.pref_selection_key),
                            getString(R.string.pref_selection_default));
                    if (sort.equals(getString(R.string.pref_selection_popular))) {
                        ((Callback) getActivity()).onItemSelected(MovieContract.PopularMoviesEntry.buildMoviesUriWithMovieId(
                                cursor.getLong(COL_MOVIE_ID)
                        ));
                    } else if (sort.equals(getString(R.string.pref_selection_highest))) {
                        ((Callback) getActivity()).onItemSelected(MovieContract.HighestMoviesEntry.buildMoviesUriWithMovieId(
                                cursor.getLong(COL_MOVIE_ID)
                        ));
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UpdateMovie();
        setHasOptionsMenu(true);
    }

    private void UpdateMovie() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String URL_STR = "";
        String sort = prefs.getString(getString(R.string.pref_selection_key),
                getString(R.string.pref_selection_default));

        if (sort.equals(getString(R.string.pref_selection_popular))) {
            URL_STR = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=1aa2b2989b33151515585a6ec53e0fa7";
        } else if (sort.equals(getString(R.string.pref_selection_highest))) {
            URL_STR = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=1aa2b2989b33151515585a6ec53e0fa7";
        }
        FetchMovies MovieTask = new FetchMovies(getActivity());
        MovieTask.execute(URL_STR, sort);
    }

    void ChangeSelection() {
        UpdateMovie();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Sort = prefs.getString(getString(R.string.pref_selection_key),
                getString(R.string.pref_selection_default));
        Uri moviesUriBySort = null;
        if (Sort.equals(getString(R.string.pref_selection_popular))) {
            moviesUriBySort = MovieContract.PopularMoviesEntry.CONTENT_URI;

        } else if (Sort.equals(getString(R.string.pref_selection_highest))) {
            moviesUriBySort = MovieContract.HighestMoviesEntry.CONTENT_URI;
        }
        return new CursorLoader(getActivity(),
                moviesUriBySort,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        /*
        if (id == R.id.action_refresh) {
            UpdateMovie();
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myadp.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myadp.swapCursor(null);
    }

    public interface Callback {
        public void onItemSelected(Uri idUri);
    }
}
