package com.jctubino.itunessearch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.repositories.MovieRepository;
import com.jctubino.itunessearch.util.Resource;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application);
    }

    //returning to UI
    public LiveData<Resource<Movie>> searchMovieApi(int movieId){
        return movieRepository.searchMoviesApi(movieId);
    }

}
