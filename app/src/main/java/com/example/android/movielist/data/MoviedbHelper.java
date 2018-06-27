package com.example.android.movielist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
Source: https://github.com/udacity/ud851-Exercises/tree/student/Lesson07-Waitlist/T07.06-Solution-RemoveGuests
 */
public class MoviedbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;

    public MoviedbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MovieContract.MovietEntry.TABLE_NAME + " (" +
                MovieContract.MovietEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovietEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovietEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}