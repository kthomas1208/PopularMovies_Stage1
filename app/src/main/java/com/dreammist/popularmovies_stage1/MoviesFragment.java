package com.dreammist.popularmovies_stage1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dreammist.popularmovies_stage1.data.MovieContract;

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    MovieAdapter mPosterAdapter;
    final String[] mSortPreferences = {"popularity.desc","vote_average.desc"};
    AlertDialog mSortDialog;

    private static final int MOVIE_LOADER = 0;

    public static final String[] MOVIE_COLUMNS = {
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
        mPosterAdapter = new MovieAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get the gridview and set the adapter to either MovieAdapter or ArrayAdapter (with image)
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


}


