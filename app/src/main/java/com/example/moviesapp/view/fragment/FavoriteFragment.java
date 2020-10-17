package com.example.moviesapp.view.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moviesapp.R;
import com.example.moviesapp.adapter.FavoriteAdapter;
import com.example.moviesapp.data.local.Room.FavoriteItem;
import com.example.moviesapp.databinding.FragmentFavoriteBinding;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.acitivty.base.BaseFragments;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends BaseFragments {

    public List<FavoriteItem> movieDataList = new ArrayList<>();
    public FragmentFavoriteBinding fragmentFavoriteBinding;

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentFavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        View view = fragmentFavoriteBinding.getRoot();

        initFavorite();

        if (movieDataList.size() == 0) {
            fragmentFavoriteBinding.favoriteFragmentTvEmptyList.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void initFavorite() {
        fragmentFavoriteBinding.favoriteFragmentVp.setAdapter(new FavoriteAdapter((BaseActivity) getActivity(), movieDataList, this, fragmentFavoriteBinding.favoriteFragmentVp));

        fragmentFavoriteBinding.favoriteFragmentVp.setClipToPadding(false);
        fragmentFavoriteBinding.favoriteFragmentVp.setClipChildren(false);
        fragmentFavoriteBinding.favoriteFragmentVp.setOffscreenPageLimit(3);
        fragmentFavoriteBinding.favoriteFragmentVp.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int paddingToSet = width / 7; //set this ratio according to how much of the next and previos screen you want to show.
        fragmentFavoriteBinding.favoriteFragmentVp.setPadding(paddingToSet, 0, paddingToSet, 0);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(1));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position < -1) {
                    page.setScaleY(0.9f);
                    page.setAlpha(1);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(0.9f, 1 - Math.abs(position - 0.04285715f));
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setScaleY(0.9f);
                    page.setAlpha(1);
                }
            }
        });

        fragmentFavoriteBinding.favoriteFragmentVp.setPageTransformer(compositePageTransformer);

    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
