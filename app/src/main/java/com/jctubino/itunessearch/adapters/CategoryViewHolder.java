package com.jctubino.itunessearch.adapters;

import android.net.Uri;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.jctubino.itunessearch.R;
import com.jctubino.itunessearch.models.Movie;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView categoryImage;
    TextView categoryTitle;
    OnMovieListener listener;
    RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView,
                              OnMovieListener listener,
                              RequestManager requestManager) {
        super(itemView);

        this.requestManager = requestManager;
        this.listener = listener;
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    public void onBind(Movie movie){

        Uri path = Uri.parse("android.resource://com.jctubino.itunessearch/drawable/star");
        requestManager
                .load(path)
                .into(categoryImage);

        categoryTitle.setText("Star");
    }

    @Override
    public void onClick(View v) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }
}
