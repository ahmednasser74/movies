package com.example.moviesapp.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.movieCast.MovieCast;
import com.example.moviesapp.data.model.moviesCategory.MoviesCategory;
import com.example.moviesapp.data.model.moviesModel.MoviesModel;
import com.example.moviesapp.data.model.moviesReview.MoviesReview;
import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;
import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieDetailsViewModel extends ViewModel {

    public MutableLiveData<MoviesTrailer> trailerDataMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MoviesReview> moviesReviewMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MovieCast> movieCastMutableLiveData = new MutableLiveData<>();

    public void getTrailer(String movieId) {
        getClient().getMovieTrailer(movieId, API_KEY).enqueue(new Callback<MoviesTrailer>() {
            @Override
            public void onResponse(Call<MoviesTrailer> call, Response<MoviesTrailer> response) {
                try {
                    trailerDataMutableLiveData.setValue(response.body());
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MoviesTrailer> call, Throwable t) {

            }
        });
    }

    public void getMoviesReview(int movieId) {
        getClient().getMovieReview(movieId, API_KEY).enqueue(new Callback<MoviesReview>() {
            @Override
            public void onResponse(Call<MoviesReview> call, Response<MoviesReview> response) {
                try {
                    moviesReviewMutableLiveData.setValue(response.body());
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MoviesReview> call, Throwable t) {

            }
        });
    }

    public void getMoviesCast(int movieId) {
        getClient().getMovieCast(movieId, API_KEY).enqueue(new Callback<MovieCast>() {
            @Override
            public void onResponse(Call<MovieCast> call, Response<MovieCast> response) {
                try {
                    movieCastMutableLiveData.setValue(response.body());
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MovieCast> call, Throwable t) {

            }
        });
    }
}