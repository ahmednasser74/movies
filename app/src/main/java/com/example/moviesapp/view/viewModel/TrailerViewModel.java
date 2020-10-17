package com.example.moviesapp.view.viewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesCategory.MoviesCategory;
import com.example.moviesapp.data.model.moviesModel.MoviesModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class TrailerViewModel extends ViewModel {

    public MutableLiveData<MoviesModel> movieMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<MoviesCategory> moviesCategoryMutableLiveData = new MutableLiveData<>();

    public void getTrailer(int page, String list) {

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

    public MutableLiveData<MoviesModel> movieMutableLiveData(){
        return movieMutableLiveData;
    }
    public MutableLiveData<MoviesCategory> movieCategoryMutableLiveData(){
        return moviesCategoryMutableLiveData;
    }

}