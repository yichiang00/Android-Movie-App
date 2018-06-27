package com.example.android.movielist.api;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.movielist.Model.Movie;
import com.example.android.movielist.Model.MovieResult;
import com.example.android.movielist.MovieAdapter;
import com.example.android.movielist.utilities.JsonUtils;
import com.example.android.movielist.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FavoriteMovieDBQueryTask extends AsyncTask<ArrayList<Integer>,  Void, ArrayList<Movie>> {


    private MovieAdapter mMovieadapter;

    public FavoriteMovieDBQueryTask(MovieAdapter adapter) {
        this.mMovieadapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Movie> doInBackground(ArrayList<Integer> ... params) {
//        String searchUrl = params[0];
        ArrayList<Integer> movieIds =params[0];
        String resultJson = null;
        try
        {
            ArrayList<Movie> favoriteMovies = new ArrayList<Movie>();
//            ArrayList<Integer> movieIds = new ArrayList<Integer>();

            for(int movieId: movieIds)
            {
                URL movieUri = NetworkUtils.builSingleMoviedUrl(NetworkUtils.SINGLE_MOVIE_ENDPOINT+movieId);
                resultJson = NetworkUtils.getResponseFromHttpUrl(movieUri);
                Movie result = JsonUtils.parseSingleMovieResult(resultJson);
                favoriteMovies.add(result);
            }

            return  favoriteMovies;
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
