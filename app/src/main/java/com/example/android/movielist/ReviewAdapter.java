package com.example.android.movielist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movielist.Model.Review;

import java.util.ArrayList;
/*
Code sources: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
*/
public class ReviewAdapter extends ArrayAdapter<Review> {

    private static class ViewHolder {
        private TextView itemView;
    }

    public ReviewAdapter(Context context, int textViewResourceId, ArrayList<Review> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.review_list_item, parent, false);

            viewHolder = new ViewHolder();
//            viewHolder.itemView = (TextView) convertView.findViewById(R.id.ItemView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Review item = getItem(position);
        if (item!= null) {
        }

        return convertView;
    }
}