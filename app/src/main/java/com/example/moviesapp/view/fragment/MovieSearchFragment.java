package com.example.moviesapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.adapter.MovieAdapter;
import com.example.moviesapp.data.model.moviesModel.MovieData;
import com.example.moviesapp.data.model.moviesWithFilter.MoviesWithFilter;
import com.example.moviesapp.databinding.FragmentSearchMovieBinding;
import com.example.moviesapp.helper.EndlessRecyclerViewScrollListener;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.acitivty.base.BaseFragments;
import com.example.moviesapp.view.acitivty.HomeCycleActivity;
import com.example.moviesapp.view.viewModel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;
import static com.example.moviesapp.helper.Constant.POPULAR;
import static com.example.moviesapp.helper.HelperMethod.isNetworkConnected;


public class MovieSearchFragment extends BaseFragments {
    private List<MovieData> movieDataList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private GridLayoutManager linearLayoutManager;
    private MovieViewModel movieViewModel;
    private HomeCycleActivity homeCycleActivity;
    private FragmentSearchMovieBinding fragmentSearchMovieBinding;
    private int paginationPage = 1;
    private String search;

    public MovieSearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentSearchMovieBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_movie, container, false);
        View view = fragmentSearchMovieBinding.getRoot();

        homeCycleActivity = (HomeCycleActivity) getActivity();
        homeCycleActivity.setVisibilityToolBar(View.GONE);
        homeCycleActivity.cancelDrawerSwipe();
        movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);

        setLayout();
        onBtnClick();
        queryMovie();
        HelperMethod.showKeyboard(getActivity(), fragmentSearchMovieBinding.movieFragmentSvSearch);
        HelperMethod.dismissProgressDialog();

        return view;
    }

    private void setLayout() {
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        fragmentSearchMovieBinding.searchMovieFragmentRvSearch.setLayoutManager(linearLayoutManager);
        fragmentSearchMovieBinding.searchMovieFragmentRvSearch.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                paginationPage++;
                movieViewModel.getMovies(paginationPage, POPULAR);
                searchMovie(search, paginationPage);
            }
        });

        movieAdapter = new MovieAdapter((BaseActivity) getActivity(), movieDataList);
        fragmentSearchMovieBinding.searchMovieFragmentRvSearch.setAdapter(movieAdapter);

        searchMovie(search, paginationPage);
    }

    private void queryMovie() {
        fragmentSearchMovieBinding.movieFragmentSvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = query;
                fragmentSearchMovieBinding.movieFragmentSvSearch.focusableViewAvailable(fragmentSearchMovieBinding.movieFragmentSvSearch);
                if (isNetworkConnected(getActivity())) {
                    searchMovie(search, paginationPage);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchMovie(newText, paginationPage);
                return false;
            }
        });
        fragmentSearchMovieBinding.movieFragmentSvSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (movieDataList != null) {
                    movieAdapter.clear();
                    movieViewModel.getMovies(1, POPULAR);
                }
                return false;
            }
        });
    }

    private String searchMovie(String query, int page) {
        getClient().getMovieWithFilter(API_KEY, query, page).enqueue(new Callback<MoviesWithFilter>() {
            @Override
            public void onResponse(Call<MoviesWithFilter> call, Response<MoviesWithFilter> response) {
                try {
                    if (response.body() != null) {
                        movieDataList.clear();
                        movieAdapter.addAll(response.body().getResults());

                        if (movieDataList.isEmpty()) {
                            fragmentSearchMovieBinding.movieFragmentTvNoMovieMatched.setVisibility(View.VISIBLE);
                        } else {
                            fragmentSearchMovieBinding.movieFragmentTvNoMovieMatched.setVisibility(View.GONE);
                        }
                        movieAdapter.notifyDataSetChanged();
                        Log.wtf("listSize", String.valueOf(movieDataList.size()));
                        Log.wtf("onResponseSearch ", String.valueOf(response.body()));
                    }
                } catch (Exception e) {
                    Log.wtf("filterException", e.toString());
                }
            }

            @Override
            public void onFailure(Call<MoviesWithFilter> call, Throwable t) {
            }
        });
        return query;
    }

    private void onBtnClick() {
//        fragmentSearchMovieBinding.searchMovieFragmentImgClearEt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HelperMethod.showKeyboard(getActivity());
//                fragmentSearchMovieBinding.searchMovieFragmentEtSearch.getText().clear();
//
//            }
//        });
//        fragmentSearchMovieBinding.searchMovieFragmentEtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (fragmentSearchMovieBinding.searchMovieFragmentEtSearch.getText().length() > 0) {
//                    fragmentSearchMovieBinding.searchMovieFragmentImgClearEt.setVisibility(View.VISIBLE);
//                } else {
//                    fragmentSearchMovieBinding.searchMovieFragmentImgClearEt.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    @Override
    public void onBack() {
        homeCycleActivity.setVisibilityToolBar(View.VISIBLE);
        homeCycleActivity.activeDrawerSwipe();
        super.onBack();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
