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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.example.moviesapp.view.viewModel.MovieCastViewModel;
import com.example.moviesapp.view.viewModel.MovieReviewViewModel;
import com.example.moviesapp.view.viewModel.MovieTrailerViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviesapp.data.local.Room.RoomManger.getInstance;
import static com.example.moviesapp.helper.Constant.IMAGE_URL;
import static com.example.moviesapp.helper.HelperMethod.onLoadImageFromUrl;
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

    private MovieTrailerViewModel movieTrailerViewModel;
    private MovieCastViewModel movieCastViewModel;
    private MovieReviewViewModel movieReviewViewModel;

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

//        fragmentMovieDetailsBinding.setLifecycleOwner(getActivity());//for LiveData be properly observed

        homeCycleActivity = (HomeCycleActivity) getActivity();
        homeCycleActivity.setVisibilityToolBar(View.GONE);
        homeCycleActivity.cancelDrawerSwipe();

        movieTrailerViewModel = ViewModelProviders.of(getActivity()).get(MovieTrailerViewModel.class);
        movieCastViewModel = ViewModelProviders.of(getActivity()).get(MovieCastViewModel.class);
        movieReviewViewModel = ViewModelProviders.of(getActivity()).get(MovieReviewViewModel.class);

        roomDao = getInstance(getActivity()).roomDao();

        Log.wtf("detailsFragment", "movieId : " + movieData.getId());
        getData();
        setLayout();

        getTrailer();
        getReview();
        getCast();

        Log.wtf("LoadFavorite", String.valueOf(LoadBoolean(getActivity(), FAVORITE_ITEM)));

        fragmentMovieDetailsBinding.movieDetailsFragmentFabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        return view;
    }

    private void setLayout() {
        linearLayoutManagerReview = new LinearLayoutManager(getActivity());
        linearLayoutManagerReview.setOrientation(LinearLayoutManager.HORIZONTAL);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvReview.setLayoutManager(linearLayoutManagerReview);

        reviewAdapter = new ReviewAdapter((BaseActivity) getActivity(), movieReviewList);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvReview.setAdapter(reviewAdapter);


        linearLayoutManagerTrailer = new LinearLayoutManager(getActivity());
        linearLayoutManagerTrailer.setOrientation(LinearLayoutManager.HORIZONTAL);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvTrailer.setLayoutManager(linearLayoutManagerTrailer);

        trailerAdapter = new TrailerAdapter((BaseActivity) getActivity(), movieTrailerDataList);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvTrailer.setAdapter(trailerAdapter);


        linearLayoutManagerCast = new LinearLayoutManager(getActivity());
        linearLayoutManagerCast.setOrientation(LinearLayoutManager.HORIZONTAL);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvCast.setLayoutManager(linearLayoutManagerCast);

        castAdapter = new CastAdapter((BaseActivity) getActivity(),movieCastDataList);
        fragmentMovieDetailsBinding.movieDetailsFragmentRvCast.setAdapter(castAdapter);
    }

    private void getCast() {
        movieCastViewModel.movieCastMutableLiveData(movieData.getId()).observe(getActivity(), new Observer<MovieCast>() {
            @Override
            public void onChanged(MovieCast movieCast) {
                movieCastDataList.clear();
                movieCastDataList.addAll(movieCast.getCast());
                castAdapter.notifyDataSetChanged();

                //castAdapter.updateList(movieCast.getCast());
                Log.d("detailsFragment", "castSize : " + movieCast.getCast().size());
            }
        });
    }

    private void getReview() {
        movieReviewViewModel.moviesReviewMutableLiveData(movieData.getId()).observe(getActivity(), new Observer<MoviesReview>() {
            @Override
            public void onChanged(MoviesReview moviesReview) {
                try {
                    movieReviewList.addAll(moviesReview.getResults());
                    if (moviesReview.getResults().isEmpty()) {
                        fragmentMovieDetailsBinding.movieDetailsFragmentTvReviewEmpty.setVisibility(View.VISIBLE);
                    } else {
                        fragmentMovieDetailsBinding.movieDetailsFragmentTvReviewEmpty.setVisibility(View.GONE);
                    }
                    reviewAdapter.notifyDataSetChanged();
                    Log.wtf("detailsFragment", "reviewSize : " + movieTrailerDataList.size());

                } catch (Exception e) {

                }
            }
        });
    }

    private void getTrailer() {

        movieTrailerViewModel.trailerDataMutableLiveData(movieData.getId()).observe(getActivity(), new Observer<MoviesTrailer>() {
            @Override
            public void onChanged(MoviesTrailer moviesTrailer) {
                movieTrailerDataList.addAll(moviesTrailer.getResults());
                trailerAdapter.notifyDataSetChanged();
                if (movieTrailerDataList.isEmpty()) {
                    fragmentMovieDetailsBinding.movieDetailsFragmentTvTrailerEmpty.setVisibility(View.VISIBLE);
                } else {
                    fragmentMovieDetailsBinding.movieDetailsFragmentTvTrailerEmpty.setVisibility(View.GONE);
                }
                Log.wtf("detailsFragment", "trailerSize : " + movieTrailerDataList.size());
            }
        });
        //        getClient().getMovieTrailer(String.valueOf(movieData.getId()), API_KEY).enqueue(new Callback<MoviesTrailer>() {
//            @Override
//            public void onResponse(Call<MoviesTrailer> call, Response<MoviesTrailer> response) {
//                try {
//                    if (response.body() != null) {
//                        movieTrailerDataList.addAll(response.body().getResults());
//                        trailerAdapter.notifyDataSetChanged();
//                        if (movieTrailerDataList.isEmpty()) {
//                            fragmentMovieDetailsBinding.movieDetailsFragmentTvTrailerEmpty.setVisibility(View.VISIBLE);
//                        } else {
//                            fragmentMovieDetailsBinding.movieDetailsFragmentTvTrailerEmpty.setVisibility(View.GONE);
//                        }
//                        Log.wtf("detailsFragment", "trailerSize : " + movieTrailerDataList.size());
//                    }
//                } catch (Exception e) {
//                    Log.wtf("trailerException", e.toString());
//                }
//            }
//            @Override
//            public void onFailure(Call<MoviesTrailer> call, Throwable t) {
//                Log.wtf("trailerThrowable", t.toString());
//            }
//        });
//
//        TrailerRepository.trailerDataMutableLiveData(String.valueOf(movieData.getId()));

//        movieTrailerViewModel.trailerDataMutableLiveData(movieData.getId().toString()).observe(getActivity(), new Observer<MoviesTrailer>() {
//            @Override
//            public void onChanged(MoviesTrailer moviesTrailer) {
//                movieTrailerDataList.clear();
//                movieTrailerDataList.addAll(moviesTrailer.getResults());
//                if (moviesTrailer.getResults().isEmpty()) {
//                    fragmentMovieDetailsBinding.movieDetailsFragmentTvTrailerEmpty.setVisibility(View.VISIBLE);
//                } else {
//                    fragmentMovieDetailsBinding.movieDetailsFragmentTvTrailerEmpty.setVisibility(View.GONE);
//                }
//                trailerAdapter.notifyDataSetChanged();
//                Log.wtf("detailsFragment", "trailerSize : " + movieTrailerDataList.size());
//            }
//        });
    }

    private void getData() {
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
            Log.wtf("LoadFavoriteInsert", String.valueOf(LoadBoolean(getActivity(), FAVORITE_ITEM)));
        } else {
            removedFavMovie();
            Log.wtf("LoadFavoriteRemove", String.valueOf(LoadBoolean(getActivity(), FAVORITE_ITEM)));
        }
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
}
