package com.dreammist.popularmovies_stage1;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

        mPosterAdapter = new ArrayAdapter<String>(
                getActivity(),                      //Context
                R.layout.grid_item_poster,          //ID of image layout
                R.id.grid_item_poster_imageview,    //ID of ImageView
                new ArrayList<String>());           //list of data (initially blank)

        // Get the gridview and set the adapter to either ImageAdapter or ArrayAdapter (with image)
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(rootView.getContext()));

        // TODO: 10/12/15 create method to parse JSON
//        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
//        fetchMoviesTask.execute();

        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string
            String moviesJsonStr = null;

            final String SORT_PARAM = "sort_by";
            final String API_PARAM = "api_key";

            String sort = "popularity.desc";
            String apiKey = "";

            try {
                // Build the URI for the API call
                // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=3695e86fa2fd999053d25829965eccc4
                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.scheme("http");
                uriBuilder.authority("api.themoviedb.org");
                uriBuilder.appendPath("3");
                uriBuilder.appendPath("discover");
                uriBuilder.appendPath("movie");
                uriBuilder.appendQueryParameter(SORT_PARAM, sort);
                uriBuilder.appendQueryParameter(API_PARAM, apiKey);

                URL url = new URL(uriBuilder.build().toString());

                Log.v(LOG_TAG, url.toString());

            }catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}


