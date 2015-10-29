package com.dreammist.popularmovies_stage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;


    public ImageAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = mInflater.inflate(R.layout.grid_item_poster, parent, false);

        } else {
            view = convertView;
        }

        final String path = "http://image.tmdb.org/t/p/w185/";
        String url = path + ((Movie)getItem(position)).getPosterPath();

        // Load the image into the ImageView using Picasso
        Picasso.with(parent.getContext()).load(url).into((ImageView) view);

        return view;
    }

}
