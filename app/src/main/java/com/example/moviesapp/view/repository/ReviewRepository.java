package com.example.moviesapp.view.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moviesapp.data.model.moviesReview.MoviesReview;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class ReviewRepository {

    private static ReviewRepository reviewRepository;
   private static MutableLiveData<MoviesReview> moviesReviewMutableLiveData = new MutableLiveData<>();

    public static ReviewRepository getInstance() {
        if (reviewRepository == null) {
            reviewRepository = new ReviewRepository();
        }
        return reviewRepository;
    }
    @SuppressLint("CheckResult")
    private static void getReview(int movieId) {

        Observable<MoviesReview> observable =
                getClient().getMovieReview(movieId, API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(o -> moviesReviewMutableLiveData.postValue(o),
                e -> Log.wtf(TAG, String.valueOf(e)));
    }

    public MutableLiveData<MoviesReview> moviesReviewMutableLiveData(int movieId){
        getReview(movieId);
        return moviesReviewMutableLiveData;
    }

}
