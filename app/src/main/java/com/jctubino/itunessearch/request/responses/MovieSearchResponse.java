package com.jctubino.itunessearch.request.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jctubino.itunessearch.models.Movie;

import java.util.List;

public class MovieSearchResponse {

    //Find resultCount object from the Api response
    @SerializedName("resultCount")
    //Deserialize
    @Expose()
    private int resultCount;

    @SerializedName("results")
    @Expose()
    private List<Movie> movies;

    public List<Movie> getMovies(){
        return movies;
    }

    public int getResultCount() {
        return resultCount;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "resultCount=" + resultCount +
                ", movie=" + movies +
                '}';
    }
}
