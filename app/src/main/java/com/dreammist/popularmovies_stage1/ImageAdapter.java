package com.dreammist.popularmovies_stage1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter<T> extends BaseAdapter {
    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
        for (String string : dummyStrings)
            this.add(string);
    }

    public ImageAdapter(Context c, Integer[] i) {
        mContext = c;
        mImages = i;
    }

    /**
     * Adds data to the list of objects
     *
     * @param object the list item to be added
     */
    public void add(T object) {
        mObjects.add(object);
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        mObjects.clear();
    }

    public int getCount() {
        return mObjects.size();
    }

    public Object getItem(int position) {
        return mObjects.get(position);
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

        final String path = "http://image.tmdb.org/t/p/w185/";
        //String url = path + getItem(position).toString();
        //String url = path + dummyStrings[position];
        String url = path + getItem(position);

        // TODO: 10/12/15 use grid_item_poster instead  

        // Load the image into the ImageView using Picasso
        Picasso.with(parent.getContext()).load(url).into(imageView);

        //imageView.setImageResource(mImages[position]);
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

    private String[] dummyStrings = {
            "jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
            "AjbENYG3b8lhYSkdrWwlhVLRPKR.jpg",
            "kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
            "qey0tdcOp9kCDdEZuJ87yE3crSe.jpg",
            "5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",
            "69Cz9VNQZy39fUE2g0Ggth6SBTM.jpg",
            "ktyVmIqfoaJ8w0gDSZyjhhOPpD6.jpg",
            "t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg",
            "z3nGs7UED9XlqUkgWeT4jQ80m1N.jpg",
            "q0R4crx2SehcEEQEkYObktdeFy.jpg",
            "aAmfIX3TT40zUHGcCKrlOZRKC7u.jpg",
            "nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"
    };
}
