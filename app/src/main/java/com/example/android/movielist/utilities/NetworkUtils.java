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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String MOVIEDB_BASE_URL =
            "https://api.themoviedb.org/3/";

    final static String PARAM_QUERY = "page";


    /*
        * https://developers.themoviedb.org/3/movies/get-popular-movies
     */
    public final static String POPULAR_ENDPOINT = "movie/popular?";
    public final static String TOPRATED_ENDPOINT = "movie/top_rated?";


    final static String APIKEY_QUERY = "api_key";
    final static String APIKEY = com.example.android.movielist.BuildConfig.ApiKey;

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
}