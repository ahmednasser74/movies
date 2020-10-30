package com.example.moviesapp.view.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviesapp.data.model.movieCast.MovieCast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class CastRepository {
    private static CastRepository castRepository;
    private static MutableLiveData<MovieCast> castMutableLiveData = new MutableLiveData<>();

    public static CastRepository getInstance() {
        if (castRepository == null) {
            castRepository = new CastRepository();
        }
        return castRepository;
    }

    @SuppressLint("CheckResult")
    private static void getCast(int movieId) {
        Observable<MovieCast> observable =
                getClient().getMovieCast(movieId, API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(movieCast -> castMutableLiveData.postValue(movieCast),
                throwable -> Log.wtf(TAG, String.valueOf(throwable)));

    }

    public LiveData<MovieCast> movieCastMutableLiveData(int movieId) {
        getCast(movieId);
        return castMutableLiveData;
    }
}
