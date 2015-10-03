package com.example.mohamedanter.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity implements PlaceholderFragment.Callback {
    private static String DETAIL_TAG = "D_Tag";
    private boolean mTwoPan;
    private String mSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.detail_container) != null) {
            mTwoPan = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment(), DETAIL_TAG)
                        .commit();
            }
        } else {
            mTwoPan = false;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSort = prefs.getString(getString(R.string.pref_selection_key),
                getString(R.string.pref_selection_default));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String Sort = prefs.getString(getString(R.string.pref_selection_key),
                getString(R.string.pref_selection_default));
        if (Sort != null && !Sort.equals(mSort)) {
            PlaceholderFragment PlaceHolder_F = (PlaceholderFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
            if (null != PlaceHolder_F) {
                PlaceHolder_F.ChangeSelection();
            }
            DetailFragment Detail_F = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_TAG);
            if (null != Detail_F) {
                Detail_F.ChangeMovies();
            }
            mSort = Sort;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPan) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, DETAIL_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }
}