package com.jctubino.itunessearch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.repositories.MovieRepository;

public class MovieViewModel extends ViewModel {

    private MovieRepository mMovieRepository;
    private int mMovieId;
    private boolean mDidRetrieveMovie;

    public MovieViewModel() {
        mMovieRepository = MovieRepository.getInstance();
        mDidRetrieveMovie = false;
    }

    public LiveData<Movie> getMovie(){
        return mMovieRepository.getMovie();
    }

    public void searchMovieById(int id){
        mMovieId = id;
        mMovieRepository.searchMovieById(id);

    }

    public LiveData<Boolean> isMovieRequestTimedOut(){
        return mMovieRepository.isMovieRequestTimedOut();
    }

    public void setRetrievedMovie(boolean retrievedMovie){
        mDidRetrieveMovie = retrievedMovie;
    }

    public boolean didRetrieveRecipe(){
        return mDidRetrieveMovie;
    }

    public int getMovieId() {
        return mMovieId;
    }

}
