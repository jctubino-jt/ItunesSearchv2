package com.jctubino.itunessearch.util;

import android.util.Log;

import com.jctubino.itunessearch.models.Movie;

import java.util.List;

public class Testing {

    public static void printMovies(List<Movie> list, String tag){
        for (Movie movie : list) {
            Log.d(tag, "onChanged: " + movie.getTrackName());
        }
    }
}
