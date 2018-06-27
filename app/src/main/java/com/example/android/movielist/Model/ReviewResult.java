package com.example.android.movielist.Model;

import java.util.ArrayList;

public class ReviewResult extends SharedResult {


    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }

    private ArrayList<Review> results;
}
