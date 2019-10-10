package com.jctubino.itunessearch.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.repositories.MovieRepository;
import com.jctubino.itunessearch.util.Resource;

import java.util.List;

//Responsible for getting, holding, displaying and retrieving movies. It keeps an updated list
public class MovieListViewModel extends AndroidViewModel{

    private static final String TAG = "MovieListViewModel";

    public static final String QUERY_EXHAUSTED = "No more results.";
    public enum ViewState {CATEGORIES, MOVIES};

    private MutableLiveData<ViewState> viewState;
    private MediatorLiveData<Resource<List<Movie>>> movies = new MediatorLiveData<>();

    private MovieRepository movieRepository;

    // query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private int pageNumber = 1;
    private int limit = 50;
    private String term;
    private boolean cancelRequest;
    private long requestStartTime;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application);
        init();
    }

    private void init(){
        if (viewState == null){
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewstate(){
        return viewState;
    }

    public void setViewCategories(){
        viewState.setValue(ViewState.CATEGORIES);
    }

    public LiveData<ViewState> getViewState(){
        return viewState;
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public LiveData<Resource<List<Movie>>> getMovies(){
        return movies;
    }


    public void searchMoviesApi(String term, int limit){
        if(!isPerformingQuery){
            if(pageNumber == 0){
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            this.term = term;
            isQueryExhausted =false;
            executeSearch();
        }
    }

    public void executeSearch(){
        isPerformingQuery = true;
        viewState.setValue(ViewState.MOVIES);
        final LiveData<Resource<List<Movie>>> repositorySource = movieRepository.searchMoviesApi(term, limit);
        movies.addSource(repositorySource, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    movies.setValue(listResource);
                    //IF SUCCESS
                    if (listResource.status == Resource.Status.SUCCESS){
                        isPerformingQuery = false;
                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                Log.d(TAG, "onChanged: query is exhausted...");
                                movies.setValue(
                                        new Resource<List<Movie>>(
                                                Resource.Status.ERROR,
                                                listResource.data,
                                                QUERY_EXHAUSTED
                                        )
                                );
                            }
                        }
                        movies.removeSource(repositorySource);
                    }
                    //IF ERROR
                    else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        movies.removeSource(repositorySource);
                    }
                }
                else {
                    //remove because observer will continue observing duplicates
                    movies.removeSource(repositorySource);
                }
            }
        });
    }

    public void cancelSearchRequest(){
        if(isPerformingQuery){
            Log.d(TAG, "cancelSearchRequest: canceling the search request.");
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }
}
