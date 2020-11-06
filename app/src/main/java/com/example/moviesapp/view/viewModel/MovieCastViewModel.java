package com.example.moviesapp.view.viewModel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.movieCast.MovieCast;
import com.example.moviesapp.data.model.moviesReview.MoviesReview;
import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;
import com.example.moviesapp.view.repository.CastRepository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieCastViewModel extends ViewModel {

    private LiveData<MovieCast> movieCastMutableLiveData;
    private CastRepository castRepository;

    public void init(int movieId) {
//        if (movieCastMutableLiveData != null) {
//            return;
//        }
        castRepository = CastRepository.getInstance();
        movieCastMutableLiveData = castRepository.movieCastMutableLiveData(movieId);
    }

    public LiveData<MovieCast> movieCastMutableLiveData(int movieId){
        init(movieId);
        return movieCastMutableLiveData;
    }

}