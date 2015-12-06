/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dreammist.popularmovies_stage1.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.dreammist.popularmovies_stage1";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_LOCATION = "location";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

//    /* Inner class that defines the table contents of the location table */
//    public static final class LocationEntry implements BaseColumns {
//
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
//
//        // Table name
//        public static final String TABLE_NAME = "location";
//
//        // The location setting string is what will be sent to openweathermap
//        // as the location query.
//        public static final String COLUMN_LOCATION_SETTING = "location_setting";
//
//        // Human readable location string, provided by the API.  Because for styling,
//        // "Mountain View" is more recognizable than 94043.
//        public static final String COLUMN_CITY_NAME = "city_name";
//
//        // In order to uniquely pinpoint the location on the map when we launch the
//        // map intent, we store the latitude and longitude as returned by openweathermap.
//        public static final String COLUMN_COORD_LAT = "coord_lat";
//        public static final String COLUMN_COORD_LONG = "coord_long";
//
//        public static Uri buildLocationUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }

    /* Inner class that defines the table contents of the Movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // ID of the movie within TMDB stored as a long
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Title of the movie stored as a string
        public static final String COLUMN_TITLE = "title";

        // Release date of the movie stored as a string
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // Path to get the poster image for the movie stored as a string
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // Average rating of the movie stored as a float
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // Movie plot description stored as a string
        public static final String COLUMN_OVERVIEW = "overview";

        // Status of movie if favorited by user stored as a boolean
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
