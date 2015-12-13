package com.dreammist.popularmovies_stage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object for holding a movie's data
 */
public class Movie implements Parcelable{
    String overview;
    String releaseDate;
    private String posterPath;
    String title;
    float voteAverage;
    long movieId;

    String[] trailers;
    String[] reviews;


    public String[] getTrailers() {
        return trailers;
    }

    public void setTrailers(String[] trailers) {
        this.trailers = trailers;
    }

    public String[] getReviews() {
        return reviews;
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public Movie(long movieId, String overview, String releaseDate, String posterPath, String title, float voteAverage) {
        this.movieId = movieId;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    private Movie(Parcel in) {
        this.movieId = in.readLong();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.voteAverage = in.readFloat();
        this.trailers = in.createStringArray();
        this.reviews = in.createStringArray();
    }

    public String getPosterPath() { return posterPath; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(movieId);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(title);
        parcel.writeFloat(voteAverage);
        parcel.writeStringArray(trailers);
        parcel.writeStringArray(reviews);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
