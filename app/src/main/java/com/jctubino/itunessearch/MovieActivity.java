package com.jctubino.itunessearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jctubino.itunessearch.adapters.MovieRecyclerAdapter;
import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.viewmodels.MovieViewModel;

import org.w3c.dom.Text;

public class MovieActivity extends BaseActivity {

    private static final String TAG = "MovieActivity";

    private MovieRecyclerAdapter mAdapter;

    //UI Components
    private ImageView ivArtworkUrl100Detail;
    private TextView tvTrackNameDetail;
    private TextView tvPrimaryGenreNameDetail;
    private TextView tvLongDescriptionDetail;
    private ScrollView mScrollView;

    private MovieViewModel mMovieViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        /*
        ivArtworkUrl100Detail = findViewById(R.id.ivArtworkUrl100Detail);
        tvTrackNameDetail = findViewById(R.id.tvTrackNameDetail);
        tvPrimaryGenreNameDetail = findViewById(R.id.tvPrimaryGenreNameDetail);
        tvLongDescriptionDetail = findViewById(R.id.tvLongDescriptionDetail);
        mScrollView = findViewById(R.id.parent);
        */
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("movie")) {
            Movie movie = getIntent().getParcelableExtra("movie");
            Log.d(TAG, "getIncomingIntent: " + movie.getTrackName());
        }
    }

    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}



