/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.example.android.movielist.utilities;

import android.net.Uri;
import android.support.graphics.drawable.animated.BuildConfig;
import com.example.android.movielist.Model.EnumModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String MOVIEDB_BASE_URL =
            "https://api.themoviedb.org/3/";

    final static String MOVIEDB_SCHEMA =
            "https";
    final static String MOVIEDB_AUTH =
            "api.themoviedb.org";

    final static String PARAM_QUERY = "page";

    final static String YOUTUBE_BASE_URL =
            "https://www.youtube.com/watch?v=";

    /*
        * https://developers.themoviedb.org/3/movies/get-popular-movies
     */
    public final static String POPULAR_ENDPOINT = "movie/popular?";
    public final static String TOPRATED_ENDPOINT = "movie/top_rated?";
    public final static String SINGLE_MOVIE_ENDPOINT = "movie/";


    final static String APIKEY_QUERY = "api_key";
    final static String APIKEY = "40fdf4049fe8106725ca72d62a1a2dce";

    public static URL buildUrl(String movieSearchQuery, String enpoints) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL + enpoints).buildUpon()
                .appendQueryParameter(PARAM_QUERY, movieSearchQuery)
                .appendQueryParameter(APIKEY_QUERY, APIKEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL builSingleMoviedUrl(String enpoints) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL + enpoints).buildUpon()
                .appendQueryParameter(APIKEY_QUERY, APIKEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /*
        Review: https://developers.themoviedb.org/3/movies/get-movie-reviews
        Trailer:
        Url Builder
        Source: https://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables
     */
    public static URL buildUrlByMovieId(Integer moiveId, EnumModel.QueryItemType movieDetailType) {

        Uri.Builder builtUri = new Uri.Builder();
        builtUri.scheme(MOVIEDB_SCHEMA)
                .authority(MOVIEDB_AUTH)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(moiveId.toString())
                .appendPath(movieDetailType.toString())
                .appendQueryParameter(APIKEY_QUERY, APIKEY);

        URL url = null;
        try {
            url = new URL(builtUri.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getVideoUrl(String videoKey)
    {
        return YOUTUBE_BASE_URL + videoKey;
    }
}