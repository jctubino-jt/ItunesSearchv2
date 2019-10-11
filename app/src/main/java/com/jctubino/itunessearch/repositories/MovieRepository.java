package com.jctubino.itunessearch.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.jctubino.itunessearch.AppExecutors;
import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.persistence.MovieDao;
import com.jctubino.itunessearch.persistence.MovieDatabase;
import com.jctubino.itunessearch.request.ServiceGenerator;
import com.jctubino.itunessearch.request.responses.ApiResponse;
import com.jctubino.itunessearch.request.responses.MovieResponse;
import com.jctubino.itunessearch.request.responses.MovieSearchResponse;
import com.jctubino.itunessearch.util.Constants;
import com.jctubino.itunessearch.util.NetworkBoundResource;
import com.jctubino.itunessearch.util.Resource;
import com.jctubino.itunessearch.request.MovieApi;

import java.util.List;

public class MovieRepository {

    private static final String TAG = "MovieRepository";

    private static MovieRepository instance;
    private MovieDao movieDao;

    public static MovieRepository getInstance(Context context){
        if(instance == null){
            instance = new MovieRepository(context);
        }
        return instance;
    }

    private MovieRepository(Context context){
        movieDao = MovieDatabase.getInstance(context).getMovieDao();
    }

    public LiveData<Resource<List<Movie>>> searchMoviesApi(final String term, final int limit){
        return new NetworkBoundResource<List<Movie>, MovieSearchResponse>(AppExecutors.getInstance()){

            //Insert Data into cache
            @Override
            protected void saveCallResult(@NonNull MovieSearchResponse item) {
                if(item.getMovies() != null){
                    Movie[] movies = new Movie[item.getMovies().size()];

                    int index = 0;
                    for(long rowid: movieDao.insertMovies((Movie[])(item.getMovies().toArray(movies)))){
                        if(rowid == -1){ //conflict detected
                            Log.d(TAG, "saveCallResult: CONFLICT...Movie is already in the cache");
                            //If movie already exists, timestamp will not be set because they will be erased
                            movieDao.updateMovie(
                                    movies[index].getTrackId(),
                                    movies[index].getTrackName(),
                                    movies[index].getPrimaryGenreName(),
                                    movies[index].getTrackPrice(),
                                    movies[index].getCurrency(),
                                    movies[index].getArtworkUrl100()
                            );
                        }
                        index++;
                    }
                }
            }

            //determines if a data should be fetched from online
            //refresh cache everytime
            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                return true;
            }

            //retrieving data from cache. WIll access MovieDao
            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                return movieDao.searchMovies(term, limit);
            }

            //returning an object of livedata
            //creating a retrofit call object
            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieSearchResponse>> createCall() {
                return ServiceGenerator.getMovieApi().searchMovies(term, limit);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Movie>> searchMoviesApi(final int movieId){
        return new NetworkBoundResource<Movie, MovieResponse>(AppExecutors.getInstance()){

            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {

                if (item.getMovie() != null){
                    item.getMovie().setTimestamp((int)System.currentTimeMillis()/1000);
                    movieDao.insertMovie(item.getMovie());
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Movie data) {
                Log.d(TAG, "shouldFetch: movie" + data.toString());
                int currentTime = (int)(System.currentTimeMillis()/100);
                Log.d(TAG, "shouldFetch: current time " + currentTime);
                int lastRefresh = data.getTimestamp();
                Log.d(TAG, "shouldFetch: last refresh " + lastRefresh);
                Log.d(TAG, "shouldFetch: it's been " + ((currentTime - lastRefresh) / 60 / 60 / 24) + " days since this recipe was refreshed. " +
                        "30 days must elapse before refreshing");
                if ((currentTime - data.getTimestamp()) >= Constants.MOVIE_REFRESH_TIME){
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH MOVIE? " + true);
                    return true;
                }

                else{
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH MOVIE? " + false);
                    return false;
                }
            }

            @NonNull
            @Override
            protected LiveData<Movie> loadFromDb() {
                return movieDao.getMovie(movieId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieResponse>> createCall() {
                return ServiceGenerator.getMovieApi().getMovie(movieId);
            }
        }.getAsLiveData();
    }
}

