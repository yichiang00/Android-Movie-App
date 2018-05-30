package com.example.android.movielist;

import android.content.Context;
import android.view.LayoutInflater;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import com.example.android.movielist.Model.Movie;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>  {

     public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    private ArrayList<Movie> mMovies= new ArrayList<>();;
    public MovieAdapter() {
    }
    public void MovieAdapter() { }


    public void setMovieItems(ArrayList<Movie> moviesList) {
        mMovies = moviesList;
        notifyDataSetChanged();
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.moive_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Context context = holder.movieImageView.getContext();

        Picasso.with(context)
                .load(mMovies.get(position).getPoster_pathURL())
                .into(holder.movieImageView);
    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }




    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView listItemNumberView;
        TextView viewHolderIndex;
        ImageView movieImageView;


        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.image_iv);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            Movie cMovie =  mMovies.get(clickedPosition);
            intent.putExtra(DetailActivity.MOVIE_KEY, cMovie );
            view.getContext().startActivity(intent);
        }


    }
}


