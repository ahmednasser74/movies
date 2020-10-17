package com.example.moviesapp.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moviesapp.R;
import com.example.moviesapp.data.local.Room.FavoriteItem;
import com.example.moviesapp.data.local.Room.RoomDao;
import com.example.moviesapp.databinding.ItemMovieBinding;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.fragment.FavoriteFragment;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import static com.example.moviesapp.data.local.Room.RoomManger.getInstance;
import static com.example.moviesapp.helper.HelperMethod.onLoadImageFromUrl;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private BaseActivity activity;
    private List<FavoriteItem> favoriteItemsList = new ArrayList<>();
    ViewPager2 viewPager2;
    private RoomDao roomDao;
    private FavoriteFragment favoriteFragment;


    public FavoriteAdapter(BaseActivity activity, List<FavoriteItem> restaurantDataList, FavoriteFragment favoriteFragment, ViewPager2 viewPager2) {
        this.activity = activity;
        this.favoriteItemsList = restaurantDataList;
        this.favoriteFragment = favoriteFragment;
        this.viewPager2 = viewPager2;
        roomDao = getInstance(activity).roomDao();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_movie,
                parent, false);
        ItemMovieBinding itemMovieBinding = DataBindingUtil.inflate
                (LayoutInflater.from(activity), R.layout.item_movie, parent, false);

        return new ViewHolder(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(ViewHolder holder, int position) {
        FavoriteItem favoriteItem = favoriteItemsList.get(position);
        holder.itemMovieBinding.itemMovieTitle.setText(favoriteItem.getMovieName());
        holder.itemMovieBinding.itemMoviePb.setProgress((int) favoriteItem.getMovieRate());
        holder.itemMovieBinding.itemMoviePb.setMax(10);
        onLoadImageFromUrl(holder.itemMovieBinding.itemMovieImg, favoriteItem.getMovieImg(), activity);
        holder.itemMovieBinding.itemMovieDate.setText(favoriteItem.getMovieReleasedDate());
        holder.itemMovieBinding.itemMovieTvPb.setText(favoriteItem.getMovieRate() + "/10");
        holder.itemMovieBinding.itemMovieRelProgressBackground.setBackgroundResource(R.drawable.white_circle);

    }

    private void setAction(ViewHolder holder, int position) {
        FavoriteItem favoriteItem = favoriteItemsList.get(position);
        holder.itemMovieBinding.itemMovieImgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomDao.removeItem(favoriteItem);
                favoriteItemsList.remove(position);
                roomDao.update(favoriteItem);
                notifyDataSetChanged();
                if (favoriteItemsList.size() == 0) {
                    favoriteFragment.fragmentFavoriteBinding.favoriteFragmentTvEmptyList.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.itemMovieBinding.itemMovieImgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

//                Uri imgUri = Uri.parse(favoriteItem.getMovieImg());
//                intent.putExtra(Intent.EXTRA_STREAM, imgUri);
//                intent.setType("image/*");
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String movieName = favoriteItem.getMovieName();
                String movieDate = favoriteItem.getMovieReleasedDate();

                intent.setType("text/plain");

                intent.putExtra(android.content.Intent.EXTRA_TEXT, movieName + "\n" + movieDate);

                activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.no_internet_connection)));

            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMovieBinding itemMovieBinding;

        public ViewHolder(ItemMovieBinding itemView) {
            super(itemView.getRoot());
            itemMovieBinding = itemView;
        }
    }
}
