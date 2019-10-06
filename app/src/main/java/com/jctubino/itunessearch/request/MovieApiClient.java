package com.jctubino.itunessearch.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jctubino.itunessearch.AppExecutors;
import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.request.responses.MovieResponse;
import com.jctubino.itunessearch.request.responses.MovieSearchResponse;
import com.jctubino.itunessearch.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.jctubino.itunessearch.util.Constants.NETWORK_TIMEOUT;

// This is the remote data source part of the application
public class MovieApiClient {

    private static final String TAG = "MovieApiClient";

    private static MovieApiClient instance;
    private MutableLiveData<List<Movie>> mMovies;
    private RetrieveMoviesRunnable mRetrieveMoviesRunnable;
    private MutableLiveData<Movie> mMovie;
    private RetrieveMovieRunnable mRetrieveMovieRunnable;
    private MutableLiveData<Boolean> mMovieRequestTimeout = new MutableLiveData<>();

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMovie = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public LiveData<Movie> getMovie() {
        return mMovie;
    }

    public LiveData<Boolean> isMovieRequestTimedOut(){
        return mMovieRequestTimeout;
    }


    public void searchMoviesApi(String term, int limit) {
        if (mRetrieveMoviesRunnable != null) {
            mRetrieveMoviesRunnable = null;
        }
        mRetrieveMoviesRunnable = new RetrieveMoviesRunnable(term, limit);
        //This is to set a timeout for the request
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveMoviesRunnable);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //This will interrupt the background task after the set time
                //This will inform the user that request has timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchMovieById(int id) {
        if (mRetrieveMovieRunnable != null) {
            mRetrieveMovieRunnable = null;
        }
        mRetrieveMovieRunnable = new RetrieveMovieRunnable(id);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveMovieRunnable);
        mMovieRequestTimeout.setValue(false);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
        @Override
        public void run(){
            //This will interrupt the background task after the set time
            //This will inform the user that request has timed out
            mMovieRequestTimeout.postValue(true);
            handler.cancel(true);
        }
    }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
}


    //Responsible for doing the query
    private class RetrieveMoviesRunnable implements Runnable{

        private String term;
        private int limit;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String term,int limit) {
            this.term = term;
            this.limit = limit;
            cancelRequest = false;
        }

        //This is the one that gets the information from the Api
        @Override
        public void run() {
            try {
                Response response = getMovies(term,limit).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    mMovies.postValue(list);
                }
                else {
                    String error = response.errorBody().string();
                    Log.e(TAG,"run " + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }

        }
        private Call<MovieSearchResponse> getMovies(String term, int limit){
            return ServiceGenerator.getMovieApi().searchMovies(
                    term,
                    limit
            );
        }

        private void cancelRequest(){
            cancelRequest = true;
        }
    }

    //Responsible for doing the query
    private class RetrieveMovieRunnable implements Runnable{

        private int id;
        boolean cancelRequest;

        public RetrieveMovieRunnable(int id) {
            this.id = id;
            cancelRequest = false;
        }

        //This is the one that gets the information from the Api
        @Override
        public void run() {
            try {
                Response response = getMovie(id).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    Movie movie = ((MovieResponse)response.body()).getMovie();
                    mMovie.postValue(movie);
                }
                else {
                    String error = response.errorBody().string();
                    Log.e(TAG,"run " + error);
                    mMovie.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovie.postValue(null);
            }

        }
        private Call<MovieSearchResponse> getMovie(int id){
            return ServiceGenerator.getMovieApi().getMovie(id);
        }

        private void cancelRequest(){
            Log.d(TAG,"cancelRequest: canceling the search request.");
            cancelRequest = true;
        }
    }

    private void cancelRequest(){
        if(mRetrieveMoviesRunnable != null){
            mRetrieveMoviesRunnable.cancelRequest();
        }
        if(mRetrieveMovieRunnable != null){
            mRetrieveMovieRunnable.cancelRequest();
        }
    }
}

