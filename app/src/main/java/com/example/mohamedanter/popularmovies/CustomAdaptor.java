package com.example.mohamedanter.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Mohamed Anter on 8/22/2015.
 */
public class CustomAdaptor extends CursorAdapter {
    ViewHolder holder = null;

    public CustomAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        holder = (ViewHolder) view.getTag();
        String x = cursor.getString(PlaceholderFragment.COL_POSTER_PATH);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + cursor.getString(PlaceholderFragment.COL_POSTER_PATH)).into(holder.PosterView);
    }

    public static class ViewHolder {
        private ImageView PosterView;

        public ViewHolder(View view) {
            PosterView = (ImageView) view.findViewById(R.id.image);
        }
    }
}