package com.jctubino.itunessearch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.repositories.MovieRepository;

import java.util.List;

//Responsible for getting, holding, displaying and retrieving movies. It keeps an updated list
public class MovieListViewModel extends ViewModel {

    public enum ViewState {MOVIES};

    private MovieRepository mMovieRepository;

    public MovieListViewModel() {
        mMovieRepository=MovieRepository.getInstance();
    };

    public LiveData<List<Movie>> getMovies(){
        return mMovieRepository.getMovies();
    }

    public void searchMoviesApi(String term, int limit){
        //if(pageNumber==0){
        mMovieRepository.searchMoviesApi(term, limit);

    }
}
