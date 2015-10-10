package com.dreammist.popularmovies_stage1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context c, Integer[] i) {
        mContext = c;
        mImages = i;
    }

    public int getCount() {
        return mImages.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

            // Get the MATCH_PARENT params to use for the ImageView
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            imageView.setLayoutParams(new GridView.LayoutParams(params));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mImages[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mImages = {
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar,
            R.drawable.interstellar, R.drawable.interstellar
    };
}
