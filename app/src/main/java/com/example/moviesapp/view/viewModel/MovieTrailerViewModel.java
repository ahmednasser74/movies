package com.example.moviesapp.view.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;
import com.example.moviesapp.view.repository.TrailerRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieTrailerViewModel extends ViewModel {

    private MutableLiveData<MoviesTrailer> trailerDataMutableLiveData;
    private TrailerRepository trailerRepository;

    private void init(int movieId) {
        trailerRepository = TrailerRepository.getInstance();
        trailerDataMutableLiveData = trailerRepository.moviesTrailerMutableLiveData(movieId);
    }

    public MutableLiveData<MoviesTrailer> moviesTrailerMutableLiveData(int movieId) {
        init(movieId);
        return trailerDataMutableLiveData;
    }
}