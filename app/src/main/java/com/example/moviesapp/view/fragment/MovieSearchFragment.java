package com.example.moviesapp.view.fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.moviesapp.R;
import com.example.moviesapp.adapter.MovieAdapter;
import com.example.moviesapp.data.model.moviesModel.MovieData;
import com.example.moviesapp.data.model.moviesWithFilter.MoviesWithFilter;
import com.example.moviesapp.databinding.FragmentSearchMovieBinding;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.acitivty.base.BaseFragments;
import com.example.moviesapp.view.acitivty.HomeCycleActivity;
import com.example.moviesapp.view.viewModel.MovieViewModel;
import com.example.moviesapp.view.viewModel.MovieSearchViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviesapp.helper.Constant.POPULAR;
import static com.example.moviesapp.helper.HelperMethod.isNetworkConnected;


public class MovieSearchFragment extends BaseFragments {
    private List<MovieData> movieDataList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private GridLayoutManager linearLayoutManager;
    private HomeCycleActivity homeCycleActivity;
    private int paginationPage = 1;

    private MovieSearchViewModel searchViewModel;
    private MovieViewModel movieViewModel;
    private FragmentSearchMovieBinding fragmentSearchMovieBinding;

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
        searchViewModel = ViewModelProviders.of(getActivity()).get(MovieSearchViewModel.class);
        movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);

        setLayout();
        onBtnClick();
        queryMovie();
        loadSearchMovie();

        HelperMethod.showKeyboard(getActivity(), fragmentSearchMovieBinding.movieFragmentSvSearch);
        HelperMethod.dismissProgressDialog();
        return view;
    }

    private void setLayout() {
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        fragmentSearchMovieBinding.searchMovieFragmentRvSearch.setLayoutManager(linearLayoutManager);
//        fragmentSearchMovieBinding.searchMovieFragmentRvSearch.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                paginationPage++;
//                movieViewModel.getMovies(paginationPage, POPULAR);
//                searchViewModel.searchMovie(search, paginationPage);
//            }
//        });

        movieAdapter = new MovieAdapter((BaseActivity) getActivity(), movieDataList);
        fragmentSearchMovieBinding.searchMovieFragmentRvSearch.setAdapter(movieAdapter);

//        searchViewModel.searchMovie(search, paginationPage);
    }

    private void queryMovie() {
        fragmentSearchMovieBinding.movieFragmentSvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isNetworkConnected(getActivity())) {
                    searchViewModel.searchMovie(query, 1);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (fragmentSearchMovieBinding.movieFragmentSvSearch.getQuery().length() > 0) {
                    searchViewModel.searchMovie(newText, 1);
//                    Log.wtf("queryLength", "!=" + fragmentSearchMovieBinding.movieFragmentSvSearch.getQuery().length());
//                } else {
//                    movieViewModel.getMovies(1, POPULAR);
//                    Log.wtf("queryLength", "=" +
//                            String.valueOf(fragmentSearchMovieBinding.movieFragmentSvSearch.getQuery().length()));
//                }
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

    private void loadSearchMovie() {
        searchViewModel.searchMutableLiveData.observe(getActivity(), new Observer<MoviesWithFilter>() {
            @Override
            public void onChanged(MoviesWithFilter moviesWithFilter) {
                movieDataList.clear();
                movieDataList.addAll(moviesWithFilter.getResults());

                if (movieDataList.isEmpty()) {
                    fragmentSearchMovieBinding.movieFragmentTvNoMovieMatched.setVisibility(View.VISIBLE);
                } else {
                    fragmentSearchMovieBinding.movieFragmentTvNoMovieMatched.setVisibility(View.GONE);
                }
                movieAdapter.notifyDataSetChanged();
                Log.wtf("listSize", String.valueOf(movieDataList.size()));
                Log.wtf("listSizeFromViewModel", String.valueOf(moviesWithFilter.getResults()));

            }
        });
    }

    private void onBtnClick() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                fragmentSearchMovieBinding.movieFragmentSvSearch.setBackgroundResource(R.drawable.black_stroke_shape);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                fragmentSearchMovieBinding.movieFragmentSvSearch.setBackgroundResource(R.drawable.white_stroke_shape);
                break;
        }


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
