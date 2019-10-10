package com.jctubino.itunessearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.jctubino.itunessearch.adapters.MovieRecyclerAdapter;
import com.jctubino.itunessearch.adapters.OnMovieListener;
import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.util.Resource;
import com.jctubino.itunessearch.util.Testing;
import com.jctubino.itunessearch.util.VerticalSpacingItemDecorator;
import com.jctubino.itunessearch.viewmodels.MovieListViewModel;

import java.util.List;

public class MovieListActivity extends BaseActivity implements OnMovieListener {

    private static final String TAG = "MovieListActivity";

    public static final String QUERY_EXHAUSTED = "No more results.";
    private MovieListViewModel mMovieListViewModel;
    private RecyclerView mRecyclerView;
    private MovieRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movie_list);
        //RecyclerView to contain data
        mRecyclerView = findViewById(R.id.movie_list);
        mSearchView = findViewById(R.id.search_view);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        initRecyclerView();
        initSearchView();
        subscribeObservers();
        //setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

    }

    //Any changes made in the data will reflect immediately because of this
    private void subscribeObservers(){
        mMovieListViewModel.getMovies().observe(this, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Movie>> listResource) {
                if(listResource != null){
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if(listResource.data != null){
                        switch (listResource.status){
                            case LOADING:{
                                if(mMovieListViewModel.getPageNumber() > 1){
                                    mAdapter.displayLoading();
                                }
                                else{
                                    mAdapter.displayLoading();
                                }
                                break;
                            }
                            case ERROR:{
                                Log.e(TAG, "onChanged: cannot refresh the cache" );
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message );
                                Log.e(TAG, "onChanged: status: ERROR, #movies: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setMovies(listResource.data);
                                Toast.makeText(MovieListActivity.this,listResource.message,Toast.LENGTH_SHORT).show();
                                if(listResource.message.equals(QUERY_EXHAUSTED)){
                                    mAdapter.setQueryExhausted();
                                }

                                break;
                            }
                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed");
                                Log.d(TAG, "onChanged: status: success, #Movies: " +listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setMovies(listResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });

        mMovieListViewModel.getViewstate().observe(this, new Observer<MovieListViewModel.ViewState>() {
            @Override
            public void onChanged(@Nullable MovieListViewModel.ViewState viewState) {
                if(viewState != null){
                    switch (viewState){

                        case MOVIES:{
                            // movies will show automatically from other observer
                            break;
                        }

                        case CATEGORIES:{
                            displaySearchCategories();
                            break;
                        }
                    }
                }
            }
        });
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background);
        return Glide.with(this)
                .setDefaultRequestOptions(options);

    }

    private void searchMovieApi(String term){
        mMovieListViewModel.searchMoviesApi(term, 50);
    }

    private void initRecyclerView(){
        mAdapter = new MovieRecyclerAdapter(this, initGlide());
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchMovieApi(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("movie", mAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        searchMovieApi(category);
    }

    private void displaySearchCategories(){
        mAdapter.displaySearchCategories();
    }

}
