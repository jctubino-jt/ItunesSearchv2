package com.jctubino.itunessearch.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;

import com.jctubino.itunessearch.models.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Insert(onConflict = IGNORE)
    long[] insertMovies(Movie... movie);

    @Insert(onConflict = REPLACE)
    void insertMovie(Movie movie);

    @Query("UPDATE movies SET track_name = :track_name, track_genre = :track_genre, track_price = :track_price, track_currency = :track_currency, track_image = :track_image " +
    "WHERE trackId = :trackId" )
    void updateMovie(int trackId, String track_name, String track_genre, float track_price, String track_currency, String track_image);

    @Query("SELECT * FROM movies WHERE track_name LIKE '%' || :term || '%' " + "ORDER BY track_name ASC LIMIT(:limit)")
    LiveData<List<Movie>> searchMovies(String term, int limit);

    @Query("SELECT * FROM movies WHERE trackId = :trackId")
    LiveData<Movie> getMovie(int trackId);
}
