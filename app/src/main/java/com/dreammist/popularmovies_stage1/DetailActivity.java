package com.dreammist.popularmovies_stage1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        public Movie mMovie;

        public DetailFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootview = inflater.inflate(R.layout.fragment_detail,container, false);

            final String path = "http://image.tmdb.org/t/p/w185/";
            Intent intent = getActivity().getIntent();

            if (intent != null && intent.hasExtra("com.dreammist.popularmovies_stage1.Movie")) {
                mMovie = intent.getParcelableExtra("com.dreammist.popularmovies_stage1.Movie");

                // Title
                TextView title = (TextView) rootview.findViewById(R.id.movie_title);
                title.setText(mMovie.title);

                // Poster
                String url = path + mMovie.getPosterPath();
                View poster = rootview.findViewById(R.id.detail_movie_poster);
                Picasso.with(container.getContext()).load(url).into((ImageView) poster);

                // Year
                TextView year = (TextView) rootview.findViewById(R.id.movie_year);
                String releaseDate = mMovie.releaseDate;
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
                float ratingScaled = (mMovie.voteAverage*5)/10;
                rating.setRating(ratingScaled);

                LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
                stars.getDrawable(0).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(2).setColorFilter(Color.parseColor("#c5ae3b"), PorterDuff.Mode.SRC_ATOP);

                // Description
                TextView description = (TextView) rootview.findViewById(R.id.movie_description);
                String overViewText = mMovie.overview;
                if(overViewText.equalsIgnoreCase("null")) overViewText = getString(R.string.no_overview);

                description.setText(overViewText);

                LinearLayout detailLayout = (LinearLayout)rootview.findViewById(R.id.detail_layout);

                // Trailer(s)
                if(mMovie.getTrailers() != null) {
                    String[] trailers = mMovie.getTrailers();
                    if(trailers.length > 1) {
                        ImageView trailer1 = (ImageView) rootview.findViewById(R.id.play_icon1);
                        trailer1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = mMovie.getTrailers()[0];
                                try{
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("vnd.youtube:" + id));
                                    startActivity(intent);
                                }catch (ActivityNotFoundException ex){
                                    Intent intent=new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://www.youtube.com/watch?v="+id));
                                    startActivity(intent);
                                }

                            }
                        });

                        ImageView trailer2 = (ImageView) rootview.findViewById(R.id.play_icon2);
                        trailer2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = mMovie.getTrailers()[1];
                                try{
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("vnd.youtube:" + id));
                                    startActivity(intent);
                                }catch (ActivityNotFoundException ex){
                                    Intent intent=new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://www.youtube.com/watch?v="+id));
                                    startActivity(intent);
                                }

                            }
                        });
                    }
                    else {
                        // If there's only one trailer available, hide the other one
                        LinearLayout trailer2 = (LinearLayout) rootview.findViewById(R.id.trailer_2);
                        trailer2.setVisibility(View.GONE);
                    }
                }

                // Review(s)
                if(mMovie.getReviews() != null) {
                    String[] reviews = mMovie.getReviews();
                    TextView review1 = (TextView) rootview.findViewById(R.id.review1);
                    TextView review2 = (TextView) rootview.findViewById(R.id.review2);

                    if(reviews.length > 1) {
                        review1.setText("\"" + reviews[0] + "\"");
                        review2.setText("\"" + reviews[1] + "\"");
                    }
                    else {
                        // If there's only one review available, hide the other one
                        review2.setVisibility(View.GONE);
                    }
                }
            }

            return rootview;
        }
    }
}


