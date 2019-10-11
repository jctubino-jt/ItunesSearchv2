package com.jctubino.itunessearch.adapters;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.jctubino.itunessearch.R;
import com.jctubino.itunessearch.models.Movie;
import com.jctubino.itunessearch.util.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
ListPreloader.PreloadModelProvider{

    private static final int MOVIE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Movie> mMovies;
    private OnMovieListener mOnMovieListener;
    private RequestManager requestManager;
    private ViewPreloadSizeProvider<String> preloadSizeProvider;

    public MovieRecyclerAdapter(OnMovieListener mOnMovieListener,
                                RequestManager requestManager,
                                ViewPreloadSizeProvider<String> viewPreloadSizeProvider){
        this.mOnMovieListener = mOnMovieListener;
        this.requestManager = requestManager;
        this.preloadSizeProvider = viewPreloadSizeProvider;
    }

    //Responsible for creating ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch (i){

            case MOVIE_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_list_item, viewGroup, false);
                return new MovieViewHolder(view, mOnMovieListener,requestManager,preloadSizeProvider);
            }

            case LOADING_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }

            case EXHAUSTED_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }

            case CATEGORY_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, mOnMovieListener,requestManager);
            }

            default:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_list_item, viewGroup, false);
                return new MovieViewHolder(view, mOnMovieListener,requestManager,preloadSizeProvider);
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if(itemViewType == MOVIE_TYPE){
            ((MovieViewHolder)viewHolder).onBind(mMovies.get(i));;
        }
        else if(itemViewType == CATEGORY_TYPE){
            ((CategoryViewHolder)viewHolder).onBind(mMovies.get(i));;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mMovies.get(position).getTrackPrice() == -1){
            return CATEGORY_TYPE;
        }
        else if(mMovies.get(position).getTrackName().equals("LOADING...")){
            return LOADING_TYPE;
        }
        else if(mMovies.get(position).getTrackName().equals("EXHAUSTED...")){
            return EXHAUSTED_TYPE;
        }
        else{
            return MOVIE_TYPE;
        }
    }

    //loading during request
    public void displayOnlyLoading(){
        clearMoviesList();
        Movie movie = new Movie();
        movie.setTrackName("LOADING...");
        mMovies.add(movie);
        notifyDataSetChanged();
    }

    private void clearMoviesList(){
        if(mMovies == null){
            mMovies = new ArrayList<>();
        }
        else{
            mMovies.clear();
        }
        notifyDataSetChanged();

    }

    public void setQueryExhausted(){
        hideLoading();
        Movie exhaustedMovie = new Movie();
        exhaustedMovie.setTrackName("EXHAUSTED...");
        mMovies.add(exhaustedMovie);
        notifyDataSetChanged();
    }

    public void hideLoading(){
        if(isLoading()){
            if(mMovies.get(0).getTrackName().equals("LOADING...")){
                mMovies.remove(0);
            }
            else if(mMovies.get(mMovies.size() - 1).equals("LOADING...")){
                mMovies.remove(mMovies.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    //pagination loading
    public void displayLoading(){
        if(mMovies == null){
            mMovies = new ArrayList<>();
        }
        if(isLoading()){
            Movie movie = new Movie();
            movie.setTrackName("LOADING...");
            mMovies.add(movie);
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if(mMovies != null){
            if(mMovies.size() > 0){
                if(mMovies.get(mMovies.size() - 1).getTrackName().equals("LOADING...")){
                    return true;
                }
            }
        }
        return false;
    }

    public void displaySearchCategories(){
        List<Movie> categories = new ArrayList<>();
        for(int i = 0; i< Constants.DEFAULT_SEARCH_CATEGORIES.length; i++){
            Movie movie = new Movie();
            movie.setTrackName(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            movie.setArtworkUrl100(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            movie.setTrackPrice(-1);
            categories.add(movie);
        }
        mMovies = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mMovies != null){
            return mMovies.size();
        }
        return 0;
    }

    public void setMovies(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

    public Movie getSelectedMovie(int position){
        if(mMovies != null){
            if(mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
        return null;
    }

    @NonNull
    @Override
    public List getPreloadItems(int position) {
        String url = mMovies.get(position).getArtworkUrl100();
        if(TextUtils.isEmpty(url)){
            return Collections.emptyList();
        }
        return Collections.singletonList(url);

    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object item) {
        return requestManager.load(item);
    }
}
