package com.example.android.movielist.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/*
Source: https://github.com/udacity/ud851-Exercises/tree/student/Lesson07-Waitlist/T07.06-Solution-RemoveGuests
 */
public class MovieContract {
    public static final String AUTHORITY = "com.example.android.movielist";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIES = "movies";


    public static final class MovietEntry implements BaseColumns {
        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIES;


        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movieId";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
