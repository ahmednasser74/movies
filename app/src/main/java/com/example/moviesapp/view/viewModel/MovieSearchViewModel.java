package com.example.moviesapp.view.viewModel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesCategory.MoviesCategory;
import com.example.moviesapp.data.model.moviesModel.MoviesModel;
import com.example.moviesapp.data.model.moviesWithFilter.MoviesWithFilter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieSearchViewModel extends ViewModel {

    public MutableLiveData<MoviesWithFilter> searchMutableLiveData = new MutableLiveData<>();

    public void searchMovie(String query, int page) {
        getClient().getMovieWithFilter(API_KEY, query, page).enqueue(new Callback<MoviesWithFilter>() {
            @Override
            public void onResponse(Call<MoviesWithFilter> call, Response<MoviesWithFilter> response) {
                try {
                    if (response.body() != null) {
                        searchMutableLiveData.setValue(response.body());
                        Log.wtf("searchResponse",String.valueOf(response.body()));
                    }
                } catch (Exception e) {
                    Log.wtf("filterException", e.toString());
                }
            }

            @Override
            public void onFailure(Call<MoviesWithFilter> call, Throwable t) {
            }
        });
    }

    public MutableLiveData<MoviesWithFilter> searchMutableLiveData() {
        return searchMutableLiveData;
    }

}