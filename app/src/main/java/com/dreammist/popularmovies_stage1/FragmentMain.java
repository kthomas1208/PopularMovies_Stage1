package com.dreammist.popularmovies_stage1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class FragmentMain extends Fragment {

    ArrayAdapter mPosterAdapter;

    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayAdapter mPosterAdapter = new ArrayAdapter<String>(
                getActivity(),                      //Context
                R.layout.grid_item_poster,          //ID of image layout
                R.id.grid_item_poster_imageview,    //ID of ImageView
                new ArrayList<String>());           //list of data (initially blank)

        // Get the gridview and set the adapter to either ImageAdapter or ArrayAdapter (with image)
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(container.getContext()));

        return rootView;
    }

}
