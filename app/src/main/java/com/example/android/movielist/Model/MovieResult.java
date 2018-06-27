package com.example.android.movielist.Model;

import java.util.ArrayList;

public class MovieResult extends SharedResult {


    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    private ArrayList<Movie> results;
}
