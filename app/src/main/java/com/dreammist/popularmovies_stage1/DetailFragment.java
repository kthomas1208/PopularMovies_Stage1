package com.dreammist.popularmovies_stage1;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dreammist.popularmovies_stage1.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private ShareActionProvider mShareActionProvider;
    private String mForecast;
    private Uri mUri;
    private int mPosition;

    private static final int DETAIL_LOADER = 0;

    public static final String[] DETAIL_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_IS_FAVORITE
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    static final int COL_MOVIE_ID_KEY = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_RELEASE_DATE = 3;
    static final int COL_POSTER_PATH = 4;
    static final int COL_VOTE_AVERAGE = 5;
    static final int COL_OVERVIEW = 6;
    static final int COL_IS_FAVORITE = 7;

    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mYearView;
    private RatingBar mRatingBar;
    private TextView mDescriptionView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    public static DetailFragment newInstance(int position, Uri uri) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.mPosition = position;
        fragment.mUri = uri;
        //args.putInt("id", position);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
//        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mTitleView = (TextView) rootView.findViewById(R.id.movie_title);
        mPosterView = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        mYearView = (TextView) rootView.findViewById(R.id.movie_year);
        mRatingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        mDescriptionView = (TextView) rootView.findViewById(R.id.movie_description);

        return rootView;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.detailfragment, menu);
//
//        // Retrieve the share menu item
//        MenuItem menuItem = menu.findItem(R.id.action_share);
//
//        // Get the provider and hold onto it to set/change the share intent.
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
//        if (mForecast != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }
//    }

//    private Intent createShareForecastIntent() {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
//        return shareIntent;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Uri moviesUri = MovieContract.MovieEntry.buildMovieUri(id);
        // Returning all the movies and calling moveToPosition with the position id in
        // onLoadFinished(). If there's a way to map the id to movie_id that would be better
        // Maybe it's possible in setOnItemClickListener() somehow?
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
        if (data != null && data.moveToPosition(mPosition)) {
            final String path = "http://image.tmdb.org/t/p/w185/";

            // Title
            mTitleView.setText(data.getString(COL_TITLE));

            // Poster
            String url = path + data.getString(COL_POSTER_PATH);
            Picasso.with(getContext()).load(url).into(mPosterView);

            // Year
            String releaseDate = data.getString(COL_RELEASE_DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            String releaseYearStr = "";
            try {
                date = sdf.parse(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int releaseYear = cal.get(Calendar.YEAR);
                releaseYearStr = Integer.toString(releaseYear);
            }
            if (!releaseYearStr.isEmpty()) mYearView.setText(releaseYearStr);

            // Rating
            mRatingBar.setStepSize((float)0.01);
            float voteAverage = data.getFloat(COL_VOTE_AVERAGE);
            float ratingScaled = (voteAverage*5)/10;
            mRatingBar.setRating(ratingScaled);

            LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
            stars.getDrawable(0).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);

            // Description
            String overViewText = data.getString(COL_OVERVIEW);
            if(overViewText.equalsIgnoreCase("null")) overViewText = getString(R.string.no_overview);
            mDescriptionView.setText(overViewText);

            // Trailers

            // Reviews
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}