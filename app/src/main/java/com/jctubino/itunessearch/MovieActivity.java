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
import com.jctubino.itunessearch.util.Resource;
import com.jctubino.itunessearch.viewmodels.MovieViewModel;

import org.w3c.dom.Text;

import java.util.Observable;

import static com.jctubino.itunessearch.util.Resource.Status.LOADING;
import static com.jctubino.itunessearch.util.Resource.Status.SUCCESS;

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

        ivArtworkUrl100Detail = findViewById(R.id.ivArtworkUrl100Detail);
        tvTrackNameDetail = findViewById(R.id.tvTrackNameDetail);
        tvPrimaryGenreNameDetail = findViewById(R.id.tvPrimaryGenreNameDetail);
        tvLongDescriptionDetail = findViewById(R.id.tvLongDescriptionDetail);
        mScrollView = findViewById(R.id.parent);

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("movie")) {
            Movie movie = getIntent().getParcelableExtra("movie");
            Log.d(TAG, "getIncomingIntent: " + movie.getTrackName());
            subscribeObservers(movie.getTrackId());
        }
    }

    private void subscribeObservers(final int movieId){
        mMovieViewModel.searchMovieApi(movieId).observe(this, new Observer<Resource<Movie>>(){
            @Override
            public void onChanged(@Nullable Resource<Movie> movieResource){
                if(movieResource != null){
                    if(movieResource.data != null){
                        switch (movieResource.status){
                            case LOADING:{
                                showProgressBar(true);
                                break;
                            }

                            case ERROR:{
                                Log.e(TAG, "onChanged: status: ERROR, Movie: " + movieResource.data.getTrackName() );
                                Log.e(TAG, "onChanged: ERROR message: " + movieResource.message);
                                showParent();
                                showProgressBar(false);
                                setMovieProperties(movieResource.data);
                                break;
                            }

                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed");
                                Log.d(TAG, "onChanged: status: SUCCESS, Movie " + movieResource.data.getTrackName());
                                //showParent();
                                showProgressBar(false);
                                setMovieProperties(movieResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private void setMovieProperties(Movie movie){
        if (movie != null){
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.white_background)
                    .error(R.drawable.white_background);


            Glide.with(this)
                    .setDefaultRequestOptions(options)
                    .load(movie.getArtworkUrl100())
                    .into(ivArtworkUrl100Detail);


            tvTrackNameDetail.setText(movie.getTrackName());
            tvPrimaryGenreNameDetail.setText(movie.getPrimaryGenreName());
            tvLongDescriptionDetail.setText(movie.getLongDescription());
        }
    }

    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}



