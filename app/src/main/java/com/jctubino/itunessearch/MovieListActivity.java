package com.jctubino.itunessearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.jctubino.itunessearch.adapters.MovieRecyclerAdapter;
import com.jctubino.itunessearch.adapters.OnMovieListener;
import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.request.MovieApi;
import com.jctubino.itunessearch.request.ServiceGenerator;
import com.jctubino.itunessearch.request.responses.MovieSearchResponse;
import com.jctubino.itunessearch.util.Testing;
import com.jctubino.itunessearch.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends BaseActivity implements OnMovieListener {

    private static final String TAG = "MovieListActivity";

    private MovieListViewModel mMovieListViewModel;
    private RecyclerView mRecyclerView;
    private MovieRecyclerAdapter mAdapter;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movie_list);
        //RecyclerView to contain data
        mRecyclerView = findViewById(R.id.movie_list);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        testRetroFitRequest();

    }

    //Any changes made in the data will reflect immediately because of this
    private void subscribeObservers(){
        mMovieListViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null) {
                    Testing.printMovies(movies,"Movie test");
                    mAdapter.setMovies(movies);
                }
            }
        });
    }

    private void initRecyclerView(){
        mAdapter = new MovieRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
    }

    private void searchMoviesApi(String term, int limit){
        //if(pageNumber==0){
        mMovieListViewModel.searchMoviesApi(term, limit);
    }

    // API Tester
    private void testRetroFitRequest(){
        searchMoviesApi("star",50);
    }

    @Override
    public void onMovieClick(int movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("movie", mAdapter.getSelectedMovie(movie));
        startActivity(intent);
    }
}
