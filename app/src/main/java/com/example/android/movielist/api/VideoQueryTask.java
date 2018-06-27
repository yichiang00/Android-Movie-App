package com.example.android.movielist.api;

import android.os.AsyncTask;

import com.example.android.movielist.Model.AsyncResponse;
import com.example.android.movielist.Model.AsyncResponseV;
import com.example.android.movielist.Model.EnumModel;
import com.example.android.movielist.Model.Review;
import com.example.android.movielist.Model.ReviewResult;
import com.example.android.movielist.Model.Video;
import com.example.android.movielist.Model.VideoResult;
import com.example.android.movielist.utilities.JsonUtils;
import com.example.android.movielist.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class VideoQueryTask extends AsyncTask<URL, Void, ArrayList<Video>> {
    private Integer movieId;
    public VideoQueryTask(Integer id) {
        movieId=id;
    }
    public AsyncResponseV delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Video> doInBackground(URL... params) {
        URL searchUrl = NetworkUtils.buildUrlByMovieId(movieId, EnumModel.QueryItemType.videos);
        String resultJson = null;
        try
        {
            resultJson = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            VideoResult result = JsonUtils.parseVideoResult(resultJson);
            return result.getResults();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Video> video) {
        delegate.processFinishV(video);
    }

}

