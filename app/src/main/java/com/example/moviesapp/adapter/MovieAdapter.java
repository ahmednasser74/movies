package com.example.moviesapp.adapter;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.data.model.moviesModel.MovieData;
import com.example.moviesapp.databinding.ItemMovieBinding;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.fragment.MovieDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviesapp.helper.Constant.IMAGE_URL;
import static com.example.moviesapp.helper.HelperMethod.onLoadImageFromUrl;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>  {

    private BaseActivity activity;
    private List<MovieData> movieDataList = new ArrayList<>();
    private List<String> movieList = new ArrayList<>();
    private int lastPosition = -1;

    MovieData movieData;

    public MovieAdapter(BaseActivity activity, List<MovieData> movieDataList) {
        this.activity = activity;
        this.movieDataList = movieDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMovieBinding itemMovieBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.item_movie, parent, false);

        return new ViewHolder(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

        GridLayoutManager.LayoutParams layoutParams2 = (GridLayoutManager.LayoutParams) holder.itemMovieBinding.itemMovieLinContainer.getLayoutParams();
        layoutParams2.height = 500;

        setAnimation(holder.itemView, position, holder);

    }

    private void setAnimation(View viewToAnimate, int position, ViewHolder holder) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.animation_down_to_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setData(ViewHolder holder, int position) {
        movieData = movieDataList.get(position);

        holder.itemMovieBinding.itemMovieTitle.setText(movieData.getTitle());
        holder.itemMovieBinding.itemMoviePb.setProgress(movieData.getVoteAverage().intValue());
        holder.itemMovieBinding.itemMoviePb.setMax(10);
        onLoadImageFromUrl(holder.itemMovieBinding.itemMovieImg, IMAGE_URL + movieData.getPosterPath(), activity);
        holder.itemMovieBinding.itemMovieTvPb.setText(movieData.getVoteAverage() + "/10");
        holder.itemMovieBinding.itemMovieImgRemove.setVisibility(View.GONE);
        holder.itemMovieBinding.itemMovieImgShare.setVisibility(View.GONE);
        holder.itemMovieBinding.itemMovieDate.setText(movieData.getReleaseDate());

        switch (activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                holder.itemMovieBinding.itemMovieRelProgressBackground.setBackgroundResource(R.drawable.gray_circle);
                holder.itemMovieBinding.itemMovieTvPb.setTextColor(Color.WHITE);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                holder.itemMovieBinding.itemMovieRelProgressBackground.setBackgroundResource(R.drawable.white_circle);
                holder.itemMovieBinding.itemMovieTvPb.setTextColor(Color.parseColor("#8F0415"));
                break;
        }
    }

    private void setAction(ViewHolder holder, int position) {
        holder.position = position;
        holder.itemMovieBinding.itemMovieLinContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                movieDetailsFragment.movieData = movieDataList.get(position);
                HelperMethod.replace(movieDetailsFragment, activity.getSupportFragmentManager(),
                        R.id.home_cycle_activity_fl_container, null, null);
                HelperMethod.hideKeyboard(activity);
            }
        });
    }

    public void clear() {
        int size = movieDataList.size();
        movieDataList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void add(MovieData movieData) {
        movieDataList.add(movieData);
        notifyItemInserted(movieDataList.size() - 1);
    }

    public void addAll(List<MovieData> movieData) {
        for (MovieData m : movieData) {
            add(m);
        }
    }

    public void addBottomItem() {
        add(new MovieData());
    }

    public void removedLastEmptyItem() {
        int position = movieDataList.size() - 1;
        MovieData item = getItem(position);

        if (item != null) {
            movieDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private MovieData getItem(int position) {
        return movieDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return movieDataList == null ? 0 : movieDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemMovieBinding itemMovieBinding;
        int position;

        public ViewHolder(ItemMovieBinding itemView) {
            super(itemView.getRoot());

            itemMovieBinding = itemView;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
