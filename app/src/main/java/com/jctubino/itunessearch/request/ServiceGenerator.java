package com.jctubino.itunessearch.request;

import com.jctubino.itunessearch.util.Constants;
import com.jctubino.itunessearch.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jctubino.itunessearch.util.Constants.CONNECTION_TIMEOUT;
import static com.jctubino.itunessearch.util.Constants.READ_TIMEOUT;
import static com.jctubino.itunessearch.util.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()
            //establishing connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            //time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            //time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    //To access private movieApi from above, we build this public method
    public static MovieApi getMovieApi(){
        return movieApi;
    }
}
