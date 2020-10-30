package com.example.moviesapp.view.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieTrailerViewModel extends ViewModel {

    private MutableLiveData<MoviesTrailer> trailerDataMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<MoviesTrailer> trailerDataMutableLiveData(int movieId) {
        getClient().getMovieTrailer(String.valueOf(movieId), API_KEY).enqueue(new Callback<MoviesTrailer>() {
            @Override
            public void onResponse(Call<MoviesTrailer> call, Response<MoviesTrailer> response) {
                try {
                    if (response.body() != null) {
                        trailerDataMutableLiveData.postValue(response.body());
                    }
                } catch (Exception e) {
                    Log.wtf("trailerException", e.toString());
                }
            }

            @Override
            public void onFailure(Call<MoviesTrailer> call, Throwable t) {
                Log.wtf("trailerThrowable", t.toString());
            }
        });
        return trailerDataMutableLiveData;
    }


}