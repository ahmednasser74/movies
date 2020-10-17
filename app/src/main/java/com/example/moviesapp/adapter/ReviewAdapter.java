package com.example.moviesapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.data.model.moviesReview.MovieReviewData;
import com.example.moviesapp.databinding.ItemReviewBinding;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.fragment.MovieDetailsFragment;
import com.example.moviesapp.view.fragment.SeeMoreReviewFragment;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private BaseActivity activity;
    private List<MovieReviewData> movieReviewDataList = new ArrayList<>();
    MovieReviewData movieReviewData;

    public ReviewAdapter(BaseActivity activity, List<MovieReviewData> movieReviewDataList) {
        this.activity = activity;
        this.movieReviewDataList = movieReviewDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemReviewBinding itemReviewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity), R.layout.item_review, parent, false);

        ViewGroup.LayoutParams layoutParams = itemReviewBinding.getRoot().getLayoutParams();
        layoutParams.width = (int) (parent.getWidth() * 0.7);
        itemReviewBinding.getRoot().setLayoutParams(layoutParams);

        return new ViewHolder(itemReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(ViewHolder holder, int position) {
        movieReviewData = movieReviewDataList.get(position);
        holder.itemReviewBinding.setReviewModel(movieReviewData);

//        holder.itemReviewTvAuthor.setText(WRITTEN_BY + movieReviewData.getAuthor());
//        holder.itemReviewTvReview.setText(movieReviewData.getContent());
    }

    private void setAction(ViewHolder holder, int position) {
        holder.itemReviewBinding.itemReviewReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeeMoreReviewFragment seeMoreReviewFragment = new SeeMoreReviewFragment();
                seeMoreReviewFragment.movieReviewData = movieReviewDataList.get(position);
                HelperMethod.replace(seeMoreReviewFragment, activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_container
                        , null, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieReviewDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemReviewBinding itemReviewBinding;

        public ViewHolder(ItemReviewBinding itemView) {
            super(itemView.getRoot());
            itemReviewBinding = itemView;
        }
    }
}
