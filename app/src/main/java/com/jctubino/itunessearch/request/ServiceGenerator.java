package com.jctubino.itunessearch.request;

import com.jctubino.itunessearch.util.Constants;
import com.jctubino.itunessearch.util.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    //To access private movieApi from above, we build this public method
    public static MovieApi getMovieApi(){
        return movieApi;
    }
}
