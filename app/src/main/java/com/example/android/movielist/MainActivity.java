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
package com.example.android.movielist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.movielist.Model.Movie;
import com.example.android.movielist.utilities.NetworkUtils;
import com.example.android.movielist.api.MovieDBQueryTask;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener {

//    private EditText mSearchBoxEditText;
//
//    private TextView mUrlDisplayTextView;
//
//    private TextView mSearchResultsTextView;
//
//    private TextView mErrorMessageDisplay;
//
//    private ProgressBar mLoadingIndicator;
//    private static final int NUM_LIST_ITEMS = 100;
//

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Movie> moviesA = new ArrayList<Movie>();


    private Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


        mAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mAdapter);
        URL movieSearchUrl = NetworkUtils.buildUrl("1", NetworkUtils.POPULAR_ENDPOINT);
        new MovieDBQueryTask(mAdapter).execute(movieSearchUrl);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String movieQuery = "1";
        switch (itemId) {

            case R.id.action_popular:
                URL movieSearchUrl = NetworkUtils.buildUrl(movieQuery, NetworkUtils.POPULAR_ENDPOINT);
                new MovieDBQueryTask(mAdapter).execute(movieSearchUrl);
                return true;
            case R.id.action_toprated:
                URL movieSearchUrl_TOPRATED = NetworkUtils.buildUrl(movieQuery, NetworkUtils.TOPRATED_ENDPOINT);
                new MovieDBQueryTask(mAdapter).execute(movieSearchUrl_TOPRATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onListItemClick(int clickedItemIndex) {

        if (mToast != null) {
            mToast.cancel();
        }

        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }
}
