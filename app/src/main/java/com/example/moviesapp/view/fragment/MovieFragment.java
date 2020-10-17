package com.example.moviesapp.view.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.adapter.MovieAdapter;
import com.example.moviesapp.adapter.CategoryAdapter;
import com.example.moviesapp.data.model.moviesCategory.MovieCategoryData;
import com.example.moviesapp.data.model.moviesCategory.MoviesCategory;
import com.example.moviesapp.data.model.moviesModel.MovieData;
import com.example.moviesapp.data.model.moviesModel.MoviesModel;
import com.example.moviesapp.data.model.moviesWithFilter.MoviesWithFilter;
import com.example.moviesapp.databinding.FragmentMovieBinding;
import com.example.moviesapp.helper.EndlessRecyclerViewScrollListener;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.service.NetworkChangeReceiver;
import com.example.moviesapp.service.OnNetworkListener;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.example.moviesapp.view.acitivty.base.BaseFragments;
import com.example.moviesapp.view.viewModel.MovieViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;
import static com.example.moviesapp.helper.Constant.NOW_PLAYING;
import static com.example.moviesapp.helper.Constant.POPULAR;
import static com.example.moviesapp.helper.Constant.TOP_RATED;
import static com.example.moviesapp.helper.HelperMethod.dismissProgressDialog;
import static com.example.moviesapp.helper.HelperMethod.floatBtnHidden;
import static com.example.moviesapp.helper.HelperMethod.floatMenuBtnHidden;
import static com.example.moviesapp.helper.HelperMethod.imageBtnHidden;
import static com.example.moviesapp.helper.HelperMethod.isNetworkConnected;

public class MovieFragment extends BaseFragments implements OnNetworkListener {

    private List<MovieData> movieDataList = new ArrayList<>();
    private List<MovieCategoryData> movieCategoryDataList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private CategoryAdapter movieCategoryAdapter;
    private GridLayoutManager gridLayoutManagerMovie;
    private LinearLayoutManager linearLayoutManagerCategory;

    private int paginationPage = 1;
    private MovieViewModel movieViewModel;
    private NetworkChangeReceiver mNetworkReceiver;
    private Snackbar snack;
    private Dialog checkDialog;

    private FragmentMovieBinding fragmentMovieBinding;

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.fragment_transition));
        //TODO: save fragment state
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentMovieBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false);
        View view = fragmentMovieBinding.getRoot();

//        HelperMethod.showProgressDialog(getActivity(), "");

        imageBtnHidden(getActivity(), fragmentMovieBinding.movieFragmentRv, fragmentMovieBinding.movieFragmentBtnTop);
        floatBtnHidden(getActivity(), fragmentMovieBinding.movieFragmentRv, fragmentMovieBinding.movieFragmentFragmentFabSearch);
        floatMenuBtnHidden(getActivity(), fragmentMovieBinding.movieFragmentRv, fragmentMovieBinding.movieFragmentBottomFabMenu);

        setLayout();
        initFabMenu();
        onBtnCLickListener();
        loadMovie();
        loadMovieCategory();

        snack = Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE);
        checkDialog = new ProgressDialog(getActivity());
        checkDialog.setContentView(R.layout.dialog_no_internet_connection);

        mNetworkReceiver = new NetworkChangeReceiver();
        mNetworkReceiver.setOnNetworkListener((OnNetworkListener) this);

        return view;
    }


    private void setLayout() {
        gridLayoutManagerMovie = new GridLayoutManager(getActivity(), 2);
        gridLayoutManagerMovie.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentMovieBinding.movieFragmentRv.setLayoutManager(gridLayoutManagerMovie);

        fragmentMovieBinding.movieFragmentRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManagerMovie) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                paginationPage++;
                movieViewModel.getMovies(paginationPage, POPULAR);
                fragmentMovieBinding.movieFragmentLinProgressLoadPages.setVisibility(View.VISIBLE);
            }
        });
        movieAdapter = new MovieAdapter((BaseActivity) getActivity(), movieDataList);
        fragmentMovieBinding.movieFragmentRv.setAdapter(movieAdapter);


        linearLayoutManagerCategory = new LinearLayoutManager(getActivity());
        linearLayoutManagerCategory.setOrientation(RecyclerView.HORIZONTAL);
        fragmentMovieBinding.movieFragmentRvCategory.setLayoutManager(linearLayoutManagerCategory);
        movieCategoryAdapter = new CategoryAdapter((BaseActivity) getActivity(), movieCategoryDataList);
        fragmentMovieBinding.movieFragmentRvCategory.setAdapter(movieCategoryAdapter);
    }

    private void loadMovie() {
        movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);

        movieViewModel.movieMutableLiveData.observe(getActivity(), new Observer<MoviesModel>() {
            @Override
            public void onChanged(MoviesModel moviesModel) {
                HelperMethod.dismissProgressDialog();
                fragmentMovieBinding.movieFragmentLinProgressLoadPages.setVisibility(View.GONE);
                fragmentMovieBinding.movieFragmentPbLoadingList.setVisibility(View.GONE);

                movieDataList.addAll(moviesModel.getResults());
                movieAdapter.notifyDataSetChanged();
            }
        });
        movieViewModel.getMovies(paginationPage, POPULAR);
    }

    private void loadMovieCategory() {
        movieCategoryDataList.add(new MovieCategoryData(0, "ALL"));

        movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);
        movieViewModel.movieCategoryMutableLiveData().observe(getActivity(), new Observer<MoviesCategory>() {
            @Override
            public void onChanged(MoviesCategory moviesCategory) {
                HelperMethod.dismissProgressDialog();
                fragmentMovieBinding.movieFragmentLinProgressLoadPages.setVisibility(View.GONE);
                movieCategoryDataList.addAll(moviesCategory.getGenres());
                movieCategoryAdapter.notifyDataSetChanged();
            }
        });
        movieViewModel.getMoviesCategory();
    }

    @Override
    public void onNetworkConnected() {
        dismissProgressDialog();
        snack.dismiss();
        checkDialog.dismiss();
    }

    @Override
    public void onNetworkDisconnected() {
        showSnackBar();
        showDialog();
    }

    private void showDialog() {
        try {
            checkDialog = new ProgressDialog(getActivity());
            checkDialog.show();
            checkDialog.setContentView(R.layout.dialog_no_internet_connection);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(checkDialog.getWindow()).setBackgroundDrawableResource(R.color.albumTransparent);
            }
            checkDialog.setCancelable(false);
        } catch (Exception e) {
        }
    }

    private void initFabMenu() {
        fragmentMovieBinding.movieFragmentBottomFabMenu.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.fab_nav_popular:
                        filterList(POPULAR);
                        break;
                    case R.id.fab_nav_top_rated:
                        filterList(TOP_RATED);
                        break;
                    case R.id.fab_nav_now_playing:
                        filterList(NOW_PLAYING);
                        break;
                }
                return true;
            }
        });
    }

    private void filterList(String list) {
        if (isNetworkConnected(getActivity())) {
            HelperMethod.showProgressDialog(getActivity(), "");
            movieDataList.clear();
            movieViewModel.getMovies(1, list);
            movieAdapter.notifyDataSetChanged();
        }
    }

    private void onBtnCLickListener() {
        fragmentMovieBinding.movieFragmentBtnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentMovieBinding.movieFragmentRv.smoothScrollToPosition(0);
            }
        });

        fragmentMovieBinding.movieFragmentFragmentFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.replaceFragmentWithAnimation(new MovieSearchFragment(), getActivity().getSupportFragmentManager(),
                        R.id.home_cycle_activity_fl_container, "rr");

            }
        });
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            baseActivity.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            baseActivity.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void showSnackBar() {
        snack.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snack.dismiss();
            }
        });
        snack.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snack.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerNetworkBroadcastForNougat();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mNetworkReceiver);
    }

    @Override
    public void onBack() {
        baseActivity.finish();
    }

}
