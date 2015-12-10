package com.dreammist.popularmovies_stage1;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_poster, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String path = "http://image.tmdb.org/t/p/w185/";
        String posterPath = cursor.getString(MoviesFragment.COL_POSTER_PATH);
        String url = path + posterPath;

        // Load the image into the ImageView using Picasso
        Picasso.with(context).load(url).into((ImageView) view);
    }

}