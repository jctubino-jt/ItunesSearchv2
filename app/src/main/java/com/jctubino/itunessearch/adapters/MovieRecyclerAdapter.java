package com.jctubino.itunessearch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jctubino.itunessearch.R;
import com.jctubino.itunessearch.models.Movie;

import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MOVIE_LIST_TYPE = 1;
    private static final int MOVIE_SPECIFIC_TYPE = 2;

    private List<Movie> mMovies;
    private OnMovieListener mOnMovieListener;

    public MovieRecyclerAdapter(OnMovieListener mOnMovieListener) {
        this.mOnMovieListener = mOnMovieListener;
    }

    //Responsible for creating ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch(i){
            case MOVIE_LIST_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_list_item, viewGroup,false);
                return new MovieViewHolder(view,mOnMovieListener);
            }

            case MOVIE_SPECIFIC_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_movie, viewGroup, false);
                return new SpecificMovieViewHolder(view);
            }

            default:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_list_item, viewGroup,false);
                return new MovieViewHolder(view,mOnMovieListener);
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        //if (itemViewType == MOVIE_LIST_TYPE){
            ((MovieViewHolder)viewHolder).tvTrackName.setText(mMovies.get(i).getTrackName());
            ((MovieViewHolder)viewHolder).tvPrimaryGenreName.setText(mMovies.get(i).getPrimaryGenreName());
            ((MovieViewHolder)viewHolder).tvTrackPrice.setText((String.valueOf(mMovies.get(i).getTrackPrice())) + mMovies.get(i).getCurrency());

            RequestOptions requestOptions = new RequestOptions().placeholder((R.drawable.ic_launcher_background));
            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(mMovies.get(i).getArtworkUrl100())
                    .into(((MovieViewHolder)viewHolder).ivArtworkUrl100);
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

}
