package com.example.mohamedanter.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamed Anter on 10/2/2015.
 */
public class ReviewAdaptor extends BaseAdapter {

    Context context;
    ArrayList<Review> mydata;
    LayoutInflater inflater;

    public ReviewAdaptor(Context context, ArrayList<Review> mydata) {
        this.context = context;
        this.mydata = mydata;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mydata.size();
    }

    @Override
    public Object getItem(int position) {
        return mydata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        vi = inflater.inflate(R.layout.review_item, null);
        TextView t1 = (TextView) vi.findViewById(R.id.name);
        TextView t2 = (TextView) vi.findViewById(R.id.content);
        t1.setText(mydata.get(position).AuthorName);
        t2.setText(mydata.get(position).Textcontent);
        return vi;
    }
}
