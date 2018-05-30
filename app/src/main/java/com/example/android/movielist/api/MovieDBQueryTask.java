package com.example.android.movielist.api;

import android.os.AsyncTask;
import android.view.View;

import com.example.android.movielist.Model.Movie;
import com.example.android.movielist.Model.MovieResult;
import com.example.android.movielist.MovieAdapter;
import com.example.android.movielist.utilities.JsonUtils;
import com.example.android.movielist.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDBQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {


    private MovieAdapter mMovieadapter;

    public MovieDBQueryTask(MovieAdapter adapter) {
        this.mMovieadapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... params) {
        URL searchUrl = params[0];
        String movieDBSearchResultsJSON = null;
        try {
            movieDBSearchResultsJSON = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            MovieResult movieR = JsonUtils.parseMovieResult(movieDBSearchResultsJSON);
            return movieR.getResults();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviesResult) {
        if (moviesResult != null) {
            mMovieadapter.setMovieItems(moviesResult);
        } else {
        }
    }
}
