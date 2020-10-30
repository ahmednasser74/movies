package com.example.moviesapp.view.viewModel;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesWithFilter.MoviesWithFilter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieSearchViewModel extends ViewModel {

    public MutableLiveData<MoviesWithFilter> searchMutableLiveData = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public void searchMovie(String query, int page) {

        Observable<MoviesWithFilter> observable = getClient()
                .getMovieWithFilter(API_KEY, query, page)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(o -> searchMutableLiveData.setValue(o),
                e -> Log.wtf(TAG, "searchMovie: " + e));

    }

    public MutableLiveData<MoviesWithFilter> searchMutableLiveData() {
        return searchMutableLiveData;
    }

}