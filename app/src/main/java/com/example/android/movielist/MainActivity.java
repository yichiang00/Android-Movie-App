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

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.android.movielist.Model.Movie;
import com.example.android.movielist.api.FavoriteMovieDBQueryTask;
import com.example.android.movielist.data.MovieContract;
import com.example.android.movielist.data.MoviedbHelper;
import com.example.android.movielist.utilities.NetworkUtils;
import com.example.android.movielist.api.MovieDBQueryTask;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int chosenMenuItem = R.id.action_popular;
    private ArrayList<Movie> moviesA = new ArrayList<Movie>();
    public static int index = -1;
    public static int top = -1;
    public StaggeredGridLayoutManager mLayoutManager;
    private String LIST_STATE_KEY = "LIST_STATE_KEY";
    Parcelable listState;
    private int viewPosition;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        mLayoutManager =
                 new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mAdapter);
        if (savedInstanceState == null)
        {
            PopulateDataByMenuItem(chosenMenuItem);
        }
        else {
            Integer menuInt=savedInstanceState.getInt("chosenMenuItem");
            PopulateDataByMenuItem(menuInt);
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.getInt("chosenMenuItem", chosenMenuItem);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        this.chosenMenuItem=itemId;
        PopulateDataByMenuItem(itemId);
        return super.onOptionsItemSelected(item);
    }

    public boolean PopulateDataByMenuItem(int itemId)
    {
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
            case R.id.action_favorite:
                // Visit https://stackoverflow.com/questions/10111166/get-all-rows-from-sqlite
                // URL movieSearchUrl_SINGLE_MOVIE = NetworkUtils.buildUrl(movieQuery, NetworkUtils.SINGLE_MOVIE_ENDPOINT);
                ArrayList<Integer> movieIds = getAllFavoriteMovies();


                new FavoriteMovieDBQueryTask(mAdapter).execute(movieIds);
                return true;
        }
        return  false;
    }
    // From: https://developer.android.com/guide/topics/providers/content-provider-basics
    public ArrayList<Integer> getAllFavoriteMovies()
    {
        ArrayList<Integer> movieIds = new ArrayList<Integer>();
        //Cursor cursor = getMovieByMovieId(movieId);
        Cursor cursor = getContentResolver().query( MovieContract.MovietEntry.CONTENT_URI, null, null, null, null);

        // if cursor is not null, find id
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                int foundMovieId = cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovietEntry.COLUMN_MOVIE_ID));

                movieIds.add(foundMovieId);
            }
        }

        //Close if it is still not null
        if (cursor != null) cursor.close();

        return movieIds;
    }
    public void onListItemClick(int clickedItemIndex) {
        chosenMenuItem=clickedItemIndex;
        sharedPreferences.edit().putInt("chosenMenuItem", chosenMenuItem);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("chosenMenuItem", chosenMenuItem);
        View firstChild = mRecyclerView.getChildAt(0);
        viewPosition = mRecyclerView.getChildAdapterPosition(firstChild);
        outState.putInt("position", viewPosition);
        outState.putParcelable(LIST_STATE_KEY, mLayoutManager.onSaveInstanceState());

    }
    protected void onRestoreInstanceState(Bundle state) {
        chosenMenuItem = state.getInt("chosenMenuItem");

        listState = state.getParcelable(LIST_STATE_KEY);
        viewPosition = state.getInt("position");
        //restore recycler view at same position
        restorePosition();
        super.onRestoreInstanceState(state);

    }
    @Override
    protected void onResume() {
        super.onResume();
        restorePosition();



    }
    private void restorePosition() {
        if (listState != null) {
            mLayoutManager.onRestoreInstanceState(listState);
            mRecyclerView.smoothScrollToPosition(viewPosition);
            listState=null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
