package com.dreammist.popularmovies_stage1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
                if (!releaseYearStr.isEmpty()) year.setText(releaseYearStr);

                // Rating
                RatingBar rating = (RatingBar) rootview.findViewById(R.id.ratingBar);
                rating.setStepSize((float)0.01);
                float ratingScaled = (movie.voteAverage*5)/10;
                rating.setRating(ratingScaled);

                LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
                stars.getDrawable(0).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(2).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);

                // Description
                TextView description = (TextView) rootview.findViewById(R.id.movie_description);
                String overViewText = movie.overview;
                if(overViewText.equalsIgnoreCase("null")) overViewText = "No overview found";
                description.setText(overViewText);
            }

            return rootview;
        }
    }
}


