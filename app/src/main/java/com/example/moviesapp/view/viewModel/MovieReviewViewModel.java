package com.example.moviesapp.view.viewModel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesReview.MoviesReview;
import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieReviewViewModel extends ViewModel {


    private MutableLiveData<MoviesReview> moviesReviewMutableLiveData = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public MutableLiveData<MoviesReview> moviesReviewMutableLiveData(int movieId) {

        Observable<MoviesReview> observable =
                getClient().getMovieReview(movieId, API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(o -> moviesReviewMutableLiveData.postValue(o),
                e -> Log.wtf(TAG, String.valueOf(e)));

        return moviesReviewMutableLiveData;
    }

}