package com.jctubino.itunessearch.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.jctubino.itunessearch.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvTrackName, tvPrimaryGenreName, tvTrackPrice;
    AppCompatImageView ivArtworkUrl100;
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView,OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;
        tvTrackName = itemView.findViewById(R.id.tvTrackName);
        tvPrimaryGenreName = itemView.findViewById(R.id.tvPrimaryGenreName);
        tvTrackPrice = itemView.findViewById(R.id.tvTrackPrice);
        ivArtworkUrl100 = itemView.findViewById(R.id.ivArtworkUrl100);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
