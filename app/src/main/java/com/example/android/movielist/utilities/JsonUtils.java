package com.example.android.movielist.utilities;

import com.example.android.movielist.Model.Movie;
import com.example.android.movielist.Model.MovieResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.movielist.Model.ReviewResult;
import com.example.android.movielist.Model.VideoResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static MovieResult parseMovieResult(String json) {

        try {
            Gson gson3 = new Gson();
            MovieResult data = gson3.fromJson(json, MovieResult.class);

            return data;
        } catch (Exception e) {
            // Do something to recover ... or kill the app.
        }
        return null;
    }

    public static Movie parseSingleMovieResult(String json) {

        try {
            Gson gson3 = new Gson();
            Movie data = gson3.fromJson(json, Movie.class);

            return data;
        } catch (Exception e) {
            // Do something to recover ... or kill the app.
        }
        return null;
    }
    public static ReviewResult parseReviewResult(String json) {

        try {
            Gson gson3 = new Gson();
            ReviewResult data = gson3.fromJson(json, ReviewResult.class);

            return data;
        } catch (Exception e) {
            // Do something to recover ... or kill the app.
        }
        return null;
    }

    public static VideoResult parseVideoResult(String json) {

        try {
            Gson gson3 = new Gson();
            VideoResult data = gson3.fromJson(json, VideoResult.class);

            return data;
        } catch (Exception e) {
            // Do something to recover ... or kill the app.
        }
        return null;
    }
}
