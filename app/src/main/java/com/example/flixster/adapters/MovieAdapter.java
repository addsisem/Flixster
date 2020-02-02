package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.DetailActivity;
import com.example.flixster.Models.Movie;
import com.example.flixster.R;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {

        this.context = context;
        this.movies = movies;
    }

    // Inflate layout from XML and return to View Holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("MovieAdapter", "OnCreateViewHolder");

        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView);
    }

    // Populates data into item through View Holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d("MovieAdapter", "OnBindViewHolder" + position);

        // Get movie at position
        Movie movie = movies.get(position);

        // Bind movie to RV
        holder.bind(movie);
    }

    // Return total # of items in list
    @Override
    public int getItemCount() {

        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView tvPoster;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvPoster = itemView.findViewById(R.id.tvPoster);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Movie movie) {

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;

            // If phone is in landscape mode
            if (context.getResources(). getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                // Then imageURL = back drop image
                imageURL = movie.getBackdropPath();

            } else {

                // Else imageURL = poster image
                imageURL = movie.getPosterPath();
            }

            Glide.with(context).load(imageURL).into(tvPoster);

            // 1. Register click Listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 2. Navigate to a new activity on tap
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }
    }
}
