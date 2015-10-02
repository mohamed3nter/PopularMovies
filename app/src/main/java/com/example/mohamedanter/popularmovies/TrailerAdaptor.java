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
public class TrailerAdaptor extends BaseAdapter {

    Context context;
    ArrayList<Trailer> mydata;
    LayoutInflater inflater;

    public TrailerAdaptor(Context context, ArrayList<Trailer> mydata) {
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
        vi = inflater.inflate(R.layout.trailer_item, null);
        TextView t1 = (TextView) vi.findViewById(R.id.Trailername);
        t1.setText(mydata.get(position).TrailerName);
        return vi;
    }
}
