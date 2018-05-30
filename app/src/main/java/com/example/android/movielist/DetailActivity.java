package com.example.android.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movielist.Model.Movie;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "cMovie";
    private static final Movie DEFAULT_MOVIE= new Movie();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView movie_poster = findViewById(R.id.image_iv);

        Intent intent = getIntent();

        if(getIntent().getExtras().getSerializable(MOVIE_KEY)  != null) {
            Movie cMovie = (Movie) getIntent().getSerializableExtra(MOVIE_KEY);

            populateUI(cMovie);
            Picasso.with(this)
                    .load(cMovie.getPoster_pathURL())
                    .into(movie_poster);
        }


    }

    private void closeOnError() {
        finish();
    }

    private void populateUI(Movie item) {
        TextView txtTitle = (TextView)findViewById(R.id.movie_title);
        txtTitle.setText(item.getTitle());

        TextView txtLanguage = (TextView)findViewById(R.id.movie_original_language);
        txtLanguage.setText(item.getOriginal_language());

        TextView txtReleaseDate = (TextView)findViewById(R.id.movie_release_date);
        txtReleaseDate.setText(item.getRelease_date());

        TextView txtVoteAvg = (TextView)findViewById(R.id.movie_vote_average);
        txtVoteAvg.setText(Float.toString(item.getVote_average()));


    }

}
