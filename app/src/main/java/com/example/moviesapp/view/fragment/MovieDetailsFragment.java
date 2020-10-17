package com.example.moviesapp.view.fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.adapter.CastAdapter;
import com.example.moviesapp.adapter.ReviewAdapter;
import com.example.moviesapp.adapter.TrailerAdapter;
import com.example.moviesapp.data.local.Room.FavoriteItem;
import com.example.moviesapp.data.local.Room.RoomDao;
import com.example.moviesapp.data.model.movieCast.MovieCast;
import com.example.moviesapp.data.model.movieCast.MovieCastData;
import com.example.moviesapp.data.model.moviesModel.MovieData;
import com.example.moviesapp.data.model.moviesReview.MovieReviewData;
import com.example.moviesapp.data.model.moviesReview.MoviesReview;
import com.example.moviesapp.data.model.moviesTrailer.MovieTrailerData;
import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;
import com.example.moviesapp.databinding.FragmentMovieDetailsBinding;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.acitivty.base.BaseFragments;
import com.example.moviesapp.view.acitivty.HomeCycleActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.data.local.Room.RoomManger.getInstance;
import static com.example.moviesapp.helper.Constant.API_KEY;
import static com.example.moviesapp.helper.Constant.IMAGE_URL;
import static com.example.moviesapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.moviesapp.helper.HelperMethod.showSnackBar;
import static com.example.moviesapp.helper.sharedPreference.LoadBoolean;
import static com.example.moviesapp.helper.sharedPreference.SaveData;
import static com.example.moviesapp.helper.sharedPreference.setSharedPreferences;

public class MovieDetailsFragment extends BaseFragments {

    private LinearLayoutManager linearLayoutManagerReview;
    private LinearLayoutManager linearLayoutManagerTrailer;
    private LinearLayoutManager linearLayoutManagerCast;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private CastAdapter castAdapter;
    private List<MovieReviewData> movieReviewList = new ArrayList<>();
    private List<MovieTrailerData> movieTrailerDataList = new ArrayList<>();
    private List<MovieCastData> movieCastDataList = new ArrayList<>();

    private HomeCycleActivity homeCycleActivity;
    public MovieData movieData;
    private FavoriteItem favoriteItem;
    private RoomDao roomDao;
    private boolean isFavorite = false;
    private static String FAVORITE_ITEM = "FAVORITE_ITEM";

    private FragmentMovieDetailsBinding fragmentMovieDetailsBinding;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentMovieDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false);
        View view = fragmentMovieDetailsBinding.getRoot();

        homeCycleActivity = (HomeCycleActivity) getActivity();
        homeCycleActivity.setVisibilityToolBar(View.GONE);
        homeCycleActivity.cancelDrawerSwipe();

        roomDao = getInstance(getActivity()).roomDao();

        getDate();
        initReview();
        initTrailer();
        initCast();

        Log.wtf( "LoadFavorite", String.valueOf(LoadBoolean(getActivity(), FAVORITE_ITEM)));

        fragmentMovieDetailsBinding.movieDetailsFragmentFabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        return view;
    }

    private void initCast() {
        linearLayoutManagerCast = new LinearLayoutManager(getActivity());
        linearLayoutManagerCast.setOrientation(LinearLayoutManager.HORIZONTAL);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvCast.setLayoutManager(linearLayoutManagerCast);

        castAdapter = new CastAdapter((BaseActivity) getActivity(), movieCastDataList);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvCast.setAdapter(castAdapter);
        getMovieCast();
    }

    private void getMovieCast() {
        getClient().getMovieCast(movieData.getId(), API_KEY).enqueue(new Callback<MovieCast>() {
            @Override
            public void onResponse(Call<MovieCast> call, Response<MovieCast> response) {
                try {
                    if (response.body() != null) {
                        movieCastDataList.addAll(response.body().getCast());

                        if (movieReviewList.size() == 0) {
                            fragmentMovieDetailsBinding.movieDetailsFragmentTvReviewEmpty.setVisibility(View.VISIBLE);
                        } else {
                            fragmentMovieDetailsBinding.movieDetailsFragmentTvReviewEmpty.setVisibility(View.GONE);
                        }

                    }
                    castAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MovieCast> call, Throwable t) {

            }
        });
    }

    private void initReview() {
        linearLayoutManagerReview = new LinearLayoutManager(getActivity());
        linearLayoutManagerReview.setOrientation(RecyclerView.HORIZONTAL);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvReview.setLayoutManager(linearLayoutManagerReview);

        reviewAdapter = new ReviewAdapter((BaseActivity) getActivity(), movieReviewList);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvReview.setAdapter(reviewAdapter);
        getMovieReview();
    }

    private void getMovieReview() {
        getClient().getMovieReview(movieData.getId(), API_KEY).enqueue(new Callback<MoviesReview>() {
            @Override
            public void onResponse(Call<MoviesReview> call, Response<MoviesReview> response) {
                try {
                    movieReviewList.addAll(response.body().getResults());
                    reviewAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MoviesReview> call, Throwable t) {

            }
        });
    }

    private void initTrailer() {
        linearLayoutManagerTrailer = new LinearLayoutManager(getActivity());
        linearLayoutManagerTrailer.setOrientation(RecyclerView.HORIZONTAL);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvTrailer.setLayoutManager(linearLayoutManagerTrailer);

        trailerAdapter = new TrailerAdapter((BaseActivity) getActivity(), movieTrailerDataList);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvTrailer.setAdapter(trailerAdapter);
        getMovieTrailer();
    }

    private void getMovieTrailer() {
        getClient().getMovieTrailer(movieData.getId().toString(), API_KEY).enqueue(new Callback<MoviesTrailer>() {
            @Override
            public void onResponse(Call<MoviesTrailer> call, Response<MoviesTrailer> response) {
                try {
                    movieTrailerDataList.addAll(response.body().getResults());
                    trailerAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MoviesTrailer> call, Throwable t) {

            }
        });
    }

    private void getDate() {
        switch (((AppCompatActivity) getActivity()).getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                fragmentMovieDetailsBinding.movieDetailsFragmentTvOverview.setTextColor(Color.WHITE);
                fragmentMovieDetailsBinding.movieDetailsFragmentTvDate.setTextColor(Color.WHITE);
                fragmentMovieDetailsBinding.movieDetailsFragmentTvOverview.setTextColor(Color.WHITE);
                fragmentMovieDetailsBinding.movieDetailsFragmentTvRecommend.setTextColor(Color.WHITE);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                fragmentMovieDetailsBinding.movieDetailsFragmentTvOverview.setTextColor(Color.BLACK);
                fragmentMovieDetailsBinding.movieDetailsFragmentTvDate.setTextColor(Color.BLACK);
                fragmentMovieDetailsBinding.movieDetailsFragmentTvOverview.setTextColor(Color.BLACK);
                fragmentMovieDetailsBinding.movieDetailsFragmentTvRecommend.setTextColor(Color.BLACK);
                break;
        }

        if (movieData.getBackdropPath() == null) {
            onLoadImageFromUrl(fragmentMovieDetailsBinding.movieDetailsFragmentImg, IMAGE_URL + movieData.getPosterPath(), getActivity());
        } else {
            onLoadImageFromUrl(fragmentMovieDetailsBinding.movieDetailsFragmentImg, IMAGE_URL + movieData.getBackdropPath(), getActivity());
        }
        fragmentMovieDetailsBinding.movieDetailsFragmentTvDate.setText("Release Date : " + movieData.getReleaseDate());
        fragmentMovieDetailsBinding.movieDetailsFragmentTvOverview.setText(movieData.getOverview());
        fragmentMovieDetailsBinding.movieDetailsFragmentTvRecommend.setText("Recommended From : " + movieData.getVoteCount() + " Person");
        fragmentMovieDetailsBinding.movieDetailsFragmentToolBar.setTitle(movieData.getTitle());

        fragmentMovieDetailsBinding.movieDetailsFragmentToolBar.setNavigationIcon(R.drawable.white_back_24dp);
        fragmentMovieDetailsBinding.movieDetailsFragmentToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

    }

    private void toggleFavorite() {
        if (!isFavorite) {
            insertFavMovie();
            Log.wtf( "LoadFavoriteInsert", String.valueOf(LoadBoolean(getActivity(), FAVORITE_ITEM)));

        } else {
            removedFavMovie();
            Log.wtf( "LoadFavoriteRemove", String.valueOf(LoadBoolean(getActivity(), FAVORITE_ITEM)));
        }
        System.out.print(LoadBoolean(getActivity(),FAVORITE_ITEM));
    }

    private void insertFavMovie() {
        HelperMethod.showSnackBar(getActivity(), "Bookmark Added");
        fragmentMovieDetailsBinding.movieDetailsFragmentFabFav.setImageResource(R.drawable.red_favorite_24dp);
        isFavorite = true;
        favoriteItem = new FavoriteItem(movieData.getId(), IMAGE_URL + movieData.getPosterPath(),
                movieData.getVoteAverage(), movieData.getTitle(), movieData.getReleaseDate());
        roomDao.addItem(favoriteItem);
        setSharedPreferences(getActivity());
        SaveData(getActivity(), FAVORITE_ITEM, true);
    }

    private void removedFavMovie() {
        HelperMethod.showSnackBar(getActivity(), "Bookmark Removed");
        fragmentMovieDetailsBinding.movieDetailsFragmentFabFav.setImageResource(R.drawable.red_favorite_outline_24dp);
        isFavorite = false;
        roomDao.removeItem(favoriteItem);
        setSharedPreferences(getActivity());
        SaveData(getActivity(), FAVORITE_ITEM, false);
    }

    @Override
    public void onBack() {
        //TODO: ezay de etzbtt ? :D
//        if(getActivity().getFragmentManager().getBackStackEntryCount() > 0){
//            getActivity(). getFragmentManager().popBackStackImmediate();
//            homeCycleActivity.setVisibilityRadioGroup(View.VISIBLE);
//        }
//        else{
//        }
        homeCycleActivity.setVisibilityToolBar(View.VISIBLE);
        homeCycleActivity.activeDrawerSwipe();
        HelperMethod.hideKeyboard(getActivity());
        super.onBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    private void saveSharedPref(String key, boolean value) {
//        SharedPreferences sp = getActivity().getSharedPreferences("KEY", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(key, value);
//        editor.apply();
//    }

//    private boolean updateSharedPref(String key) {
//        SharedPreferences sp = getActivity().getSharedPreferences("KEY", MODE_PRIVATE);
//        return sp.getBoolean(key, false);
//    }
}
