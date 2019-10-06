package com.jctubino.itunessearch.repositories;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.request.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;
    private MovieApiClient mMovieApiClient;
    private MediatorLiveData<List<Movie>> mMovies = new MediatorLiveData<>();
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();

    public static MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    public MovieRepository() {
        mMovieApiClient = MovieApiClient.getInstance();
        initMediators();
    }

    private void initMediators(){
        LiveData<List<Movie>> recipeListApiSource = mMovieApiClient.getMovies();
        mMovies.addSource(recipeListApiSource, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> recipes) {

                if(recipes != null){
                    mMovies.setValue(recipes);
                    doneQuery(recipes);
                }
                else{
                    // search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Movie> list){
        if(list != null){
            if (list.size() % 30 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        }
        else{
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<List<Movie>> getMovies(){
        return mMovies;
    }
    public LiveData<Movie> getMovie(){
        return mMovieApiClient.getMovie();
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }


    public void searchMovieById(int id){
        mMovieApiClient.searchMovieById(id);
    }

    public void searchMoviesApi(String term, int limit){
    //if(pageNumber==0){
    mMovieApiClient.searchMoviesApi(term, limit);
    }

    public LiveData<Boolean> isMovieRequestTimedOut(){
        return mMovieApiClient.isMovieRequestTimedOut();
    }
}

