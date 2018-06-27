package com.example.android.movielist.api;

import android.os.AsyncTask;

import com.example.android.movielist.Model.AsyncResponse;
import com.example.android.movielist.Model.Review;
import com.example.android.movielist.Model.ReviewResult;
import com.example.android.movielist.utilities.JsonUtils;
import com.example.android.movielist.utilities.NetworkUtils;
import com.example.android.movielist.Model.EnumModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewQueryTask extends AsyncTask<URL, Void, ArrayList<Review>> {
    private Integer movieId;
    public AsyncResponse delegate = null;

    public ReviewQueryTask(Integer id) {
        movieId=id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Review> doInBackground(URL... params) {
        URL searchUrl = NetworkUtils.buildUrlByMovieId(movieId, EnumModel.QueryItemType.reviews);
        String resultJson = null;
        try
        {
            resultJson = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            ReviewResult result = JsonUtils.parseReviewResult(resultJson);
            return result.getResults();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Review>();
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        delegate.processFinish(reviews);
    }


}
