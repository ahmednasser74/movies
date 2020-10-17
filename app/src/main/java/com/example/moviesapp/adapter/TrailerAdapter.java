package com.example.moviesapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.data.model.moviesTrailer.MovieTrailerData;
import com.example.moviesapp.databinding.ItemTrailerBinding;
import com.example.moviesapp.view.acitivty.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviesapp.helper.Constant.FIRST_PART_VIDEO_POSTER_URL;
import static com.example.moviesapp.helper.Constant.LAST_PART_VIDEO_POSTER_URL;
import static com.example.moviesapp.helper.Constant.YOUTUBE_TRAILER_URL;
import static com.example.moviesapp.helper.HelperMethod.onLoadImageFromUrl;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private BaseActivity activity;
    private List<MovieTrailerData> movieTrailerDataList = new ArrayList<>();
    private MovieTrailerData movieTrailerData;

    public TrailerAdapter(BaseActivity activity, List<MovieTrailerData> movieTrailerDataList) {
        this.activity = activity;
        this.movieTrailerDataList = movieTrailerDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemTrailerBinding itemTrailerBinding = DataBindingUtil.inflate
                (LayoutInflater.from(activity), R.layout.item_trailer, parent, false);

        ViewGroup.LayoutParams layoutParams = itemTrailerBinding.getRoot().getLayoutParams();
        layoutParams.width = (int) (parent.getWidth() * 0.7);
        itemTrailerBinding.getRoot().setLayoutParams(layoutParams);

        return new ViewHolder(itemTrailerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        movieTrailerData = movieTrailerDataList.get(position);
        holder.itemTrailerBinding.setModel(movieTrailerData);
        onLoadImageFromUrl(holder.itemTrailerBinding.itemVideoImgBgTrailer, FIRST_PART_VIDEO_POSTER_URL +
                movieTrailerData.getKey() + LAST_PART_VIDEO_POSTER_URL, activity);

    }

    private void setAction(ViewHolder holder, int position) {
        holder.itemTrailerBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieTrailerData = movieTrailerDataList.get(position);
                String key = movieTrailerData.getKey();
                Toast.makeText(activity, "Youtube Opened", Toast.LENGTH_SHORT).show();

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YOUTUBE_TRAILER_URL + key));
                try {
                    activity.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    activity.startActivity(webIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return movieTrailerDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTrailerBinding itemTrailerBinding;

        public ViewHolder(ItemTrailerBinding itemView) {
            super(itemView.getRoot());
            itemTrailerBinding= itemView;
        }
    }
}
