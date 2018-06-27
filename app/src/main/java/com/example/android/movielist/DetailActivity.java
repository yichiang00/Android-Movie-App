package com.example.android.movielist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.View;

import com.example.android.movielist.Model.AsyncResponse;
import com.example.android.movielist.Model.AsyncResponseV;
import com.example.android.movielist.Model.Movie;
import com.example.android.movielist.Model.Review;
import com.example.android.movielist.Model.Video;
import com.example.android.movielist.data.MovieContract;
import com.example.android.movielist.data.MoviedbHelper;
import com.example.android.movielist.api.ReviewQueryTask;
import com.example.android.movielist.api.VideoQueryTask;
import com.example.android.movielist.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements AsyncResponse, AsyncResponseV {

    public static final String MOVIE_KEY = "cMovie";
    private static final Movie DEFAULT_MOVIE = new Movie();
    ImageView favoriteIconImageView;
    private SQLiteDatabase mDb;
    private Movie cMovie  = new Movie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        favoriteIconImageView = (ImageView) findViewById(R.id.favorite_heart_icon);

        ImageView movie_poster = findViewById(R.id.image_iv);

        Intent intent = getIntent();



        if(getIntent().getExtras().getSerializable(MOVIE_KEY) != null) {
            cMovie = (Movie) getIntent().getSerializableExtra(MOVIE_KEY);
            populateUI(cMovie);
            Picasso.with(this)
                    .load(cMovie.getPoster_pathURL())
                    .into(movie_poster);

            //Get All Review and put in to list view
            ReviewQueryTask reviewQueryTask = new ReviewQueryTask(cMovie.getId());
            reviewQueryTask.delegate = this;
            reviewQueryTask.execute();

            //Get all Video
            VideoQueryTask videoQueryTask = new VideoQueryTask(cMovie.getId());
            videoQueryTask.delegate = this;
            videoQueryTask.execute();



            // Load Favorite Image and create a listener. toggle result.
            initFavoriteImageView();
        }


    }

    public void initFavoriteImageView()
    {
        // if we find this movie id is in database, we set heart-fill.png
        if(isMovieInDB(cMovie.getId())){
            favoriteIconImageView.setImageResource(R.drawable.heart_fill);
        }else{
            favoriteIconImageView.setImageResource(R.drawable.heart);
        }
        //else if we set heart.png


        View.OnClickListener favoriteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // if movie is in db, we delete this id
                if(isMovieInDB(cMovie.getId())){
                    deleteMovieInDB();
                    favoriteIconImageView.setImageResource(R.drawable.heart);
                }
                //else if we add a new entry to db
                else {
                    saveMovieInDB();
                    favoriteIconImageView.setImageResource(R.drawable.heart_fill);
                }
        }};
        favoriteIconImageView.setOnClickListener(favoriteListener);

    }


    /**
     * Query the mDb and get all movie from the movie table
     *
     * @return Cursor containing the list of movie
     */
    private Cursor getMovieByMovieId(int movideId) {
//        return mDb.query(
//                MovieContract.MovietEntry.TABLE_NAME,
//                new String[]{MovieContract.MovietEntry.COLUMN_MOVIE_ID},
//                null,
//                null,
//                null,
//                null,
//                null
//        );
        return mDb.rawQuery("SELECT * FROM "+MovieContract.MovietEntry.TABLE_NAME+" WHERE "+MovieContract.MovietEntry.COLUMN_MOVIE_ID+" = " + movideId, null);

    }
// From: https://developer.android.com/guide/topics/providers/content-provider-basics
    public boolean isMovieInDB(int movieId)
    {
        //Cursor cursor = getMovieByMovieId(movieId);
        Cursor cursor = getContentResolver().query( MovieContract.MovietEntry.CONTENT_URI, null, null, null, null);

        // if cursor is not null, find id
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                int foundMovieId = cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovietEntry.COLUMN_MOVIE_ID));
                if (foundMovieId == movieId) {
                    return true;
                }
            }
        }

        //Close if it is still not null
        if (cursor != null) cursor.close();

        return false;
    }
    // Copy from https://github.com/udacity/ud851-Exercises/tree/student/Lesson07-Waitlist/T07.06-Solution-RemoveGuests
    //https://stackoverflow.com/questions/8708295/how-to-delete-a-row-from-a-table-in-sqlite-android
    public void saveMovieInDB()
    {
        //insert all guests in one transaction
        try
        {
            ContentValues insertValues = new ContentValues();
            insertValues.put(MovieContract.MovietEntry.COLUMN_MOVIE_ID, cMovie.getId());
//            mDb.insert(MovieContract.MovietEntry.TABLE_NAME, null, insertValues);
            Uri uri = getContentResolver().insert( MovieContract.MovietEntry.CONTENT_URI, insertValues);
        }
        catch (Exception e) {
        }


    }
//https://stackoverflow.com/questions/8708295/how-to-delete-a-row-from-a-table-in-sqlite-android
    public boolean deleteMovieInDB()
    {
        Integer changeRow = 0;
        //insert all guests in one transaction
        try
        {
            changeRow = getContentResolver().delete(
                    MovieContract.MovietEntry.CONTENT_URI
                    ,MovieContract.MovietEntry.COLUMN_MOVIE_ID+"=?"
                    , new String[]{Integer.toString(cMovie.getId())}

            );

        }
        catch (Exception e) {
        }

        return  changeRow > 0;
    }
    //From reviewQueryTask. Source: https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    public void processFinish(ArrayList<Review> reviews){
        initReviewLayout(reviews);
    }

    //From videoQueryTask. Source: https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    public void processFinishV(ArrayList<Video> videos){
        initVideoLayout(videos);
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

    /*
    Create ListView for reviews and videos
    Code source: https://developer.android.com/reference/android/widget/ListView
    https://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65
    https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
    */
    private void initReviewLayout(ArrayList<Review> reviews) {
        if (reviews != null && reviews.size() > 0) {
            LinearLayout reviewLayout = (LinearLayout) findViewById(R.id.review_list_view_section);

            for (Review review : reviews) {
                LayoutInflater inflater = getLayoutInflater();
                View reviewLayoutView = inflater.inflate(R.layout.review_list_item, reviewLayout, false);


                // set Review View
                TextView reviewIdTextView = (TextView) reviewLayoutView.findViewById(R.id.review_id);
                reviewIdTextView.setText(review.getId());

                TextView reviewAuthorTextView = (TextView) reviewLayoutView.findViewById(R.id.review_author);
                reviewAuthorTextView.setText(review.getAuthor());

                TextView reviewContentTextView = (TextView) reviewLayoutView.findViewById(R.id.review_content);
                reviewContentTextView.setText(review.getContent());

                TextView reviewUrlTextView = (TextView) reviewLayoutView.findViewById(R.id.review_url);
                reviewUrlTextView.setText(review.getUrl());
                reviewLayout.addView(reviewLayoutView);

            }
        } else { }
    }
    private void initVideoLayout(ArrayList<Video> videos) {
        if (videos != null && videos.size() > 0) {
            LinearLayout videoLayout = (LinearLayout) findViewById(R.id.video_list_view_section);

            for (final Video video : videos) {
                LayoutInflater inflater = getLayoutInflater();
                View layoutView = inflater.inflate(R.layout.video_list_item, videoLayout, false);



                TextView nameTextView = (TextView) layoutView.findViewById(R.id.video_name);
                nameTextView.setText(video.getName());

                TextView siteTextView = (TextView) layoutView.findViewById(R.id.video_sites);
                siteTextView.setText(video.getSite());

                ImageView videoPlayIconImageView = (ImageView) layoutView.findViewById(R.id.video_play_id);

                videoPlayIconImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(NetworkUtils.getVideoUrl(video.getKey())));
                        startActivity(intent);
                    }
                });

                videoLayout.addView(layoutView);

            }
        } else { }
    }
}
