package com.jctubino.itunessearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private MovieViewModel mMovieViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        Movie movie = getIntent().getParcelableExtra("movie");

        ivArtworkUrl100Detail = findViewById(R.id.ivArtworkUrl100Detail);
        tvTrackNameDetail = findViewById(R.id.tvTrackNameDetail);
        tvPrimaryGenreNameDetail = findViewById(R.id.tvPrimaryGenreNameDetail);
        tvLongDescriptionDetail = findViewById(R.id.tvLongDescriptionDetail);

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        setMovieProperties(movie);
        //getIncomingIntent();
        //subscribeObservers();

        //testRetroFitRequest();

    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("movie")){
            Movie movie = getIntent().getParcelableExtra("movie");
            Log.d(TAG, "getIncomingIntent: " + movie.getTrackName());
            mMovieViewModel.searchMovieById(movie.getTrackId());
        }
    }


    private void subscribeObservers(){
        mMovieViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if(movie!=null) {
                    if (movie.getTrackId() == (mMovieViewModel.getMovieId())) {
                        setMovieProperties(movie);
                        mMovieViewModel.setRetrievedMovie(true);
                    }
                }
            }
        });
    }


    //This is where we set the value to display
    private void setMovieProperties(Movie movie){
        if(movie != null){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(movie.getArtworkUrl100())
                    .into(ivArtworkUrl100Detail);

            tvTrackNameDetail.setText(movie.getTrackName());
            tvPrimaryGenreNameDetail.setText(movie.getPrimaryGenreName());
            tvLongDescriptionDetail.setText(movie.getLongDescription());
        }

    }

}
