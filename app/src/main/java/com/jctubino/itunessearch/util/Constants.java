package com.jctubino.itunessearch.util;

import com.jctubino.itunessearch.viewmodels.MovieListViewModel;

public class Constants {

    public static final String BASE_URL = "https://itunes.apple.com";
    public static final int CONNECTION_TIMEOUT = 10;  //10 Seconds
    public static final int READ_TIMEOUT = 2; //2 seconds
    public static final int WRITE_TIMEOUT = 2; //2 seconds

    public static final String[] DEFAULT_SEARCH_CATEGORIES =
            {"STAR"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
                    "star"
            };

    public static final int MOVIE_REFRESH_TIME = 60 * 60 * 24 * 30; //30 days (in seconds)
}
