package com.dreammist.popularmovies_stage1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
                mMovie = (Movie) intent.getParcelableExtra("com.dreammist.popularmovies_stage1.Movie");

                FetchTrailersAndReviewsTask fetchTrailersAndReviewsTask = new FetchTrailersAndReviewsTask();
                fetchTrailersAndReviewsTask.execute(Long.toString(mMovie.movieId));

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
            }

            return rootview;
        }

        public class FetchTrailersAndReviewsTask extends AsyncTask<String, Void, Void> {

            private final String LOG_TAG = FetchTrailersAndReviewsTask.class.getSimpleName();

            @Override
            protected Void doInBackground(String... params) {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String movieID = params[0];
                //long movieID = Long.getLong(movieIDStr);

                // Will contain the raw JSON response as a string
                String moviesJsonStr = null;

                //final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";
                final String APPEND_RESPONSE = "append_to_response";
                String apiKey = BuildConfig.TMDB_API_KEY;

                try {
                    // Build the URI for the API call
                    // http://api.themoviedb.org/3/movie/102899?api_key=3695e86fa2fd999053d25829965eccc4&append_to_response=trailers,reviews
                    Uri.Builder uriBuilder = new Uri.Builder();
                    uriBuilder.scheme("http");
                    uriBuilder.authority("api.themoviedb.org");
                    uriBuilder.appendPath("3");
                    uriBuilder.appendPath("movie");
                    uriBuilder.appendPath(movieID);
                    uriBuilder.appendQueryParameter(APPEND_RESPONSE, "trailers,reviews");
                    uriBuilder.appendQueryParameter(API_PARAM, apiKey);

                    URL url = new URL(uriBuilder.build().toString());

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
                    getTrailerAndReviewDataFromJSON(moviesJsonStr);
                }
                catch (JSONException e) {
                    Log.v(LOG_TAG, e.getMessage(), e);
                }
                return null;
            }

            /**
             * Creates a Movie object from the movie database API JSON object.
             * @param trailersReviewsJsonStr the string holding the JSON response from the API request
             * @return Returns a Movie object fully loaded from the API call
             * @throws JSONException
             */
            private void getTrailerAndReviewDataFromJSON(String trailersReviewsJsonStr) throws JSONException {


                JSONObject trailersReviewsJSON = new JSONObject(trailersReviewsJsonStr);

                // Get trailers for the movie
                JSONObject trailersObject = trailersReviewsJSON.getJSONObject("trailers");
                JSONArray trailersArray = trailersObject.getJSONArray("youtube");
                String[] trailers = new String[trailersArray.length()];
                for (int i = 0; i < trailersArray.length(); i++) {
                    trailers[i] = trailersArray.getJSONObject(i).getString("source");
                }

                mMovie.setTrailers(trailers);

                // Get reviews for the movie
                JSONObject reviewsObject = trailersReviewsJSON.getJSONObject("reviews");
                JSONArray reviewsArray = reviewsObject.getJSONArray("results");
                String[][] reviews = new String[reviewsArray.length()][2];
                for (int i = 0; i < reviewsArray.length(); i++) {
                    reviews[i][0] = reviewsArray.getJSONObject(i).getString("author");
                    reviews[i][1] = reviewsArray.getJSONObject(i).getString("content");
                }

                mMovie.setReviews(reviews);
            }
        }
    }
}


