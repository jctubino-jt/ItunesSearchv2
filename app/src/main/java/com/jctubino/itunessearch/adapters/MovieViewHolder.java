package com.jctubino.itunessearch.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.jctubino.itunessearch.R;
import com.jctubino.itunessearch.models.Movie;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvTrackName, tvPrimaryGenreName, tvTrackPrice;
    AppCompatImageView ivArtworkUrl100;
    OnMovieListener onMovieListener;
    RequestManager requestManager;

    public MovieViewHolder(@NonNull View itemView,
                           OnMovieListener onMovieListener,
                           RequestManager requestManager) {
        super(itemView);
        this.onMovieListener = onMovieListener;
        this.requestManager = requestManager;
        tvTrackName = itemView.findViewById(R.id.tvTrackName);
        tvPrimaryGenreName = itemView.findViewById(R.id.tvPrimaryGenreName);
        tvTrackPrice = itemView.findViewById(R.id.tvTrackPrice);
        ivArtworkUrl100 = itemView.findViewById(R.id.ivArtworkUrl100);

        itemView.setOnClickListener(this);
    }

    public void onBind(Movie movie){

        requestManager
                .load(movie.getArtworkUrl100())
                .into(ivArtworkUrl100);

        tvTrackName.setText(movie.getTrackName());
        tvPrimaryGenreName.setText(movie.getPrimaryGenreName());
        tvTrackPrice.setText(String.valueOf((movie.getTrackPrice()))
                +(movie.getCurrency()));
    }

    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
