package com.jctubino.itunessearch.request.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jctubino.itunessearch.models.Movie;

import java.util.List;


//Cant find a way to implement this since iTunes api doesn't search by trackId
public class MovieResponse {


    //Find resultCount object from the Api response
    @SerializedName("resultCount")
    //Deserialize
    @Expose()
    private int resultCount;
    @SerializedName("results")
    @Expose()
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "resultCount=" + resultCount +
                ", movie=" + movie +
                '}';
    }
}
