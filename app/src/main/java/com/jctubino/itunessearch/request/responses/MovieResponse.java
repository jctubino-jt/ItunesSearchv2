package com.jctubino.itunessearch.request.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jctubino.itunessearch.models.Movie;

import java.util.List;


//Cant find a way to implement this since iTunes api doesn't search by trackId
public class MovieResponse {

    @SerializedName("results")
    @Expose()
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movies=" + movie +
                '}';
    }
}
