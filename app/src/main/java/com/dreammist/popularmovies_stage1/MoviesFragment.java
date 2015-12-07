package com.dreammist.popularmovies_stage1;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dreammist.popularmovies_stage1.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    ImageAdapter mPosterAdapter;
    final String[] mSortPreferences = {"popularity.desc","vote_average.desc"};
    AlertDialog mSortDialog;

    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_IS_FAVORITE
    };

    static final int COL_MOVIE_ID_KEY = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_RELEASE_DATE = 3;
    static final int COL_POSTER_PATH = 4;
    static final int COL_VOTE_AVERAGE = 5;
    static final int COL_OVERVIEW = 6;
    static final int COL_IS_FAVORITE = 7;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri moviesUri = MovieContract.MovieEntry.buildMovieUri(id);

        Loader<Cursor> loader = new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPosterAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPosterAdapter.swapCursor(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public MoviesFragment() {}

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setHasOptionsMenu(true);

        // Create dialog for changing sort option
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort");

        builder.setSingleChoiceItems(R.array.sort_preferences, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save sort order as a SharedPreference
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.sort_key), mSortPreferences[which]);
                        editor.commit();
                    }
                });

        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateMovies();
            }
        });

        // Create the AlertDialog
        mSortDialog = builder.create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.sort) {
            mSortDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calls the movie API and updates the list of movies. Can be called anywhere safely.
     */
    private void updateMovies(){
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity());

        // Get the sort order set by the user by getting the SharedPreferences
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String sortPreference = sharedPref.getString(getString(R.string.sort_key),
                getString(R.string.sort_default));
        fetchMoviesTask.execute(sortPreference);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosterAdapter = new ImageAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get the gridview and set the adapter to either ImageAdapter or ArrayAdapter (with image)
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mPosterAdapter);

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Movie movie = (Movie) mPosterAdapter.getItem(position);
//                Intent intent = new Intent(getActivity(),DetailActivity.class);
//                intent.putExtra("com.dreammist.popularmovies_stage1.Movie", movie);
//                startActivity(intent);
//            }
//        });

        updateMovies();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private final Context mContext;

        public FetchMoviesTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String sortPreference = params[0];
            // Will contain the raw JSON response as a string
            String moviesJsonStr = null;

            final String SORT_PARAM = "sort_by";
            final String API_PARAM = "api_key";
            String sort = sortPreference;
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

                //Log.v(LOG_TAG, url.toString());

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

                // Parse through JSON and return relevant data
                moviesJsonStr = buffer.toString();
                getMovieDataFromJSON(moviesJsonStr);
                //Log.v(LOG_TAG, forecastJsonStr); // Printing out the weather data to log

            }catch (JSONException e) {
                Log.v(LOG_TAG, e.getMessage(), e);
                return null;
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

        /**
         * Creates a Movie object from the movie database API JSON object.
         * @param moviesJsonStr the string holding the JSON response from the API request
         * @return Returns a Movie object fully loaded from the API call
         * @throws JSONException
         */
        private void getMovieDataFromJSON(String moviesJsonStr) throws JSONException {
            JSONObject moviesJSON = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJSON.getJSONArray("results");
            Movie[] movies = new Movie[resultsArray.length()];

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(resultsArray.length());

            for(int i=0; i < resultsArray.length(); i++) {
                long id = resultsArray.getJSONObject(i).getLong("id");
                String overview  = resultsArray.getJSONObject(i).getString("overview");
                String releaseDate = resultsArray.getJSONObject(i).getString("release_date");
                String posterPath = resultsArray.getJSONObject(i).getString("poster_path");
                String title = resultsArray.getJSONObject(i).getString("title");
                float voteAverage = (float)resultsArray.getJSONObject(i).getDouble("vote_average");

                ContentValues movieValues = new ContentValues();

                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
                movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
                movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
                movieValues.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 0);

                cVVector.add(movieValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchMovieTask Complete. " + inserted + " Inserted");
        }
    }
}


