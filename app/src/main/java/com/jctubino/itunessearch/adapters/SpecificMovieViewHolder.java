package com.jctubino.itunessearch.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jctubino.itunessearch.R;

import java.text.BreakIterator;

public class SpecificMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    TextView tvTrackNameDetail, tvPrimaryGenreNameDetail, tvLongDescriptionDetail;
    ImageView ivArtworkUrl100Detail;


    public SpecificMovieViewHolder(@NonNull View itemView){
        super(itemView);


        tvTrackNameDetail = itemView.findViewById(R.id.tvTrackNameDetail);
        tvPrimaryGenreNameDetail = itemView.findViewById(R.id.tvPrimaryGenreNameDetail);
        tvLongDescriptionDetail = itemView.findViewById(R.id.tvLongDescriptionDetail);
        ivArtworkUrl100Detail = itemView.findViewById(R.id.ivArtworkUrl100Detail);

    }

    @Override
    public void onClick(View view) {

    }
}
