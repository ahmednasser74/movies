package com.example.moviesapp.view.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesCategory.MoviesCategory;
import com.example.moviesapp.data.model.moviesModel.MoviesModel;
import com.example.moviesapp.helper.HelperMethod;
import com.example.moviesapp.view.acitivty.base.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieViewModel extends ViewModel {

    public MutableLiveData<MoviesModel> movieMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<MoviesCategory> moviesCategoryMutableLiveData = new MutableLiveData<>();

    public void getMovies(int page, String list) {

        getClient().getMovie(list, API_KEY, page).enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                try {
                    movieMutableLiveData.setValue(response.body());
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MoviesModel> call, Throwable t) {

            }
        });
    }

    public void getMoviesCategory() {
        getClient().getMovieCategory(API_KEY).enqueue(new Callback<MoviesCategory>() {
            @Override
            public void onResponse(Call<MoviesCategory> call, Response<MoviesCategory> response) {
                moviesCategoryMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MoviesCategory> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<MoviesModel> movieMutableLiveData() {
        return movieMutableLiveData;
    }

    public MutableLiveData<MoviesCategory> movieCategoryMutableLiveData() {
        return moviesCategoryMutableLiveData;
    }

}


//        Observable observable = getClient().getMovie("", page);
//        Observer observer = new Observer() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Object value) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };

