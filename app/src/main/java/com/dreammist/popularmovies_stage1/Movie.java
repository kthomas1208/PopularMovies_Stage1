package com.dreammist.popularmovies_stage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevinthomas on 10/26/15.
 */
public class Movie implements Parcelable{
    String overview;
    String releaseDate;
    String posterPath;
    String title;
    double voteAverage;

    public Movie(String overview, String releaseDate, String posterPath, String title, double voteAverage) {
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    private Movie(Parcel in) {
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
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
