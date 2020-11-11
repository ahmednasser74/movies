package com.example.moviesapp.view.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;


import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TrailerRepository {
    private static TrailerRepository trailerRepository;
    private static MutableLiveData<MoviesTrailer> moviesTrailerMutableLiveData = new MutableLiveData<>();

    public static TrailerRepository getInstance() {
        if (trailerRepository == null) {
            trailerRepository = new TrailerRepository();
        }
        return trailerRepository;
    }

    @SuppressLint("CheckResult")
    private void getTrailer(int movieId) {
        Observable<MoviesTrailer> observable =
                getClient().getMovieTrailer(String.valueOf(movieId), API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(o -> moviesTrailerMutableLiveData.postValue(o),
                e -> Log.wtf("trailerRxException", e.toString()));
    }

    public MutableLiveData<MoviesTrailer> moviesTrailerMutableLiveData(int movieId) {
        getTrailer(movieId);
        return moviesTrailerMutableLiveData;
    }
}
