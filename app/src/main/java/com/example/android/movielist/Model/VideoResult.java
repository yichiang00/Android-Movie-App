package com.example.android.movielist.Model;

import java.util.ArrayList;

public class VideoResult {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public ArrayList<Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }

    private ArrayList<Video> results;
}
