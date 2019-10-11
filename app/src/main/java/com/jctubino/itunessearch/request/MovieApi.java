package com.jctubino.itunessearch.request;


import androidx.lifecycle.LiveData;

import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.request.responses.ApiResponse;
import com.jctubino.itunessearch.request.responses.MovieResponse;
import com.jctubino.itunessearch.request.responses.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Contains all the request for the API
public interface MovieApi {

    // Normal Search
    @GET("search?media=movie&country=au")
    LiveData<ApiResponse<MovieSearchResponse>> searchMovies(
            @Query("term") String term,
            @Query("limit") int limit
    );

    // For Pagination
    @GET("search?media=movie&country=au")
    LiveData<ApiResponse<MovieSearchResponse>> searchMovies(
            @Query("term") String term,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    // For single id search
    @GET("lookup")
    LiveData<ApiResponse<MovieResponse>> getMovie(
            @Query("id") int id
    );

}
