package com.dreammist.popularmovies_stage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment {

        public DetailFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootview = inflater.inflate(R.layout.fragment_detail,container, false);

            final String path = "http://image.tmdb.org/t/p/w185/";
            Intent intent = getActivity().getIntent();

            if (intent != null && intent.hasExtra("com.dreammist.popularmovies_stage1.Movie")) {
                Movie movie = (Movie) intent.getParcelableExtra("com.dreammist.popularmovies_stage1.Movie");

                // Title
                TextView title = (TextView) rootview.findViewById(R.id.movie_title);
                title.setText(movie.title);

                // Poster
                String url = path + movie.getPosterPath();
                View poster = rootview.findViewById(R.id.detail_movie_poster);
                Picasso.with(container.getContext()).load(url).into((ImageView) poster);

                // Year
                TextView year = (TextView) rootview.findViewById(R.id.movie_year);
                String releaseDate = movie.releaseDate;
                year.setText(releaseDate);

                // Rating
                RatingBar rating = (RatingBar) rootview.findViewById(R.id.ratingBar);
                rating.setStepSize((float)0.01);
                Log.v(LOG_TAG, "API RATING: " + Float.toString(movie.voteAverage));
                float ratingScaled = (movie.voteAverage*5)/10;
                Log.v(LOG_TAG, "RATING SCALED: " + Float.toString(ratingScaled));
                rating.setRating(ratingScaled);
                Log.v(LOG_TAG, "SET RATING: " + Float.toString(rating.getRating()));

                // Description
                TextView description = (TextView) rootview.findViewById(R.id.movie_description);
                description.setText(movie.overview);
            }

            return rootview;
        }
    }
}


