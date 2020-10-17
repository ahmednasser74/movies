package com.example.moviesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.moviesapp.R;
import com.example.moviesapp.data.model.moviesCategory.MovieCategoryData;
import com.example.moviesapp.databinding.ItemCategoryBinding;
import com.example.moviesapp.view.acitivty.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private BaseActivity activity;
    private List<MovieCategoryData> movieCategoryDataList = new ArrayList<>();
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private MovieCategoryData movieCategoryData;
    private int currentIndex = -1;

    public CategoryAdapter(BaseActivity activity, List<MovieCategoryData> movieCategoryDataList) {
        this.activity = activity;
        this.movieCategoryDataList = movieCategoryDataList;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCategoryBinding itemCategoryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity), R.layout.item_category, parent, false);

        return new ViewHolder(itemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

        //        if (position == 0) {
//            holder.itemCategoryBinding.itemCategoryLin.setBackgroundResource(R.drawable.red_stroke_shape);
//        }
    }

    private void setData(ViewHolder holder, int position) {
        movieCategoryData = movieCategoryDataList.get(position);
        holder.itemCategoryBinding.setModel(movieCategoryData);
    }

    private void setAction(ViewHolder holder, int position) {
        holder.position = position;
        holder.itemCategoryBinding.itemCategoryLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = position;
                notifyDataSetChanged();
                Toast.makeText(activity, movieCategoryDataList.get(position).getId().toString() + " id of category", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, movieCategoryDataList.get(position).getName() + " name", Toast.LENGTH_SHORT).show();
            }
        });
        if (currentIndex == position) {
            holder.itemCategoryBinding.itemCategoryLin.setBackgroundResource(R.drawable.red_stroke_shape);
        } else {
            holder.itemCategoryBinding.itemCategoryLin.setBackgroundResource(R.drawable.black_stroke_shape);
        }
    }

    @Override
    public int getItemCount() {
        return movieCategoryDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding itemCategoryBinding;
        private int position;

        public ViewHolder(ItemCategoryBinding itemView) {
            super(itemView.getRoot());
            itemCategoryBinding = itemView;
        }
    }
}
