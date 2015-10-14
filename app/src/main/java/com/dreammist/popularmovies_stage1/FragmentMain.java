package com.dreammist.popularmovies_stage1;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentMain extends Fragment {

    ImageAdapter mPosterAdapter;

    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        mPosterAdapter = new ArrayAdapter<String>(
//                getActivity(),                      //Context
//                R.layout.grid_item_poster,          //ID of image layout
//                R.id.grid_item_poster_imageview,    //ID of ImageView
//                new ArrayList<String>());           //list of data (initially blank)

       mPosterAdapter = new ImageAdapter<>(rootView.getContext());
        // Get the gridview and set the adapter to either ImageAdapter or ArrayAdapter (with image)
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(rootView.getContext()));

        // TODO: 10/12/15 create method to parse JSON
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();

        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(Void... params) {

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

                // Create the request to TheMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                //Log.v(LOG_TAG, forecastJsonStr); // Printing out the weather data to log

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

            // Parse through JSON and return relevant data
            try {
                String[] strings = getMovieDataFromJSON(moviesJsonStr);
                return strings;
            }
            catch (JSONException e) {
                Log.v(LOG_TAG, e.getMessage(), e);
                return null;
            }


        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null) {
                mPosterAdapter.clear();         // clear any previous data
                for(String string : strings) {
                    mPosterAdapter.add(string); // add new data
                }
            }
        }

        private String[] getMovieDataFromJSON(String moviesJsonStr) throws JSONException {
            JSONObject moviesJSON = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJSON.getJSONArray("results");
            String[] results = new String[resultsArray.length()];

            for(int i=0; i < resultsArray.length(); i++) {
                results[i] = resultsArray.getJSONObject(i).getString("poster_path");
            }

            if(results != null)
                return results;
            else return new String[0];
        }
    }
}


