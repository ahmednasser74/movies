package com.example.moviesapp.view.viewModel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.model.moviesCategory.MoviesCategory;
import com.example.moviesapp.data.model.moviesModel.MoviesModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;

public class MovieViewModel extends ViewModel {
    private static final String TAG = "MovieViewModel";

    private MutableLiveData<MoviesModel> movieMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<MoviesCategory> moviesCategoryMutableLiveData = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public void getMovies(int page, String list) {
/**
 * hena ana  observable ely a3ed bysm3 ll data ama tegy mn server
 * w ha3ml observe 3aleh w arg3 (down stream) el data de fe Schedulers.io() thread
 * w brg3 el data fe el main thread by .observerOn() 3shanyrg3ly el setValue() ely ana b3mlha
 * fe el main thread
 * (w mmkn ast5dm postValue() bdl setValue() w heya automatic htrg3 el data fe mainThread)
 * */
        Observable<MoviesModel> observable = getClient()
                .getMovie(list, API_KEY, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        observable.subscribe(o -> movieMutableLiveData.setValue(o),
                e -> Log.d(TAG, "onError: " + e));

        /**
         * el line ely fo2 da e5tsar ll observer elkber
         * o-> ana b7ot el value as object bs b3ml observable mn movie model 3shan y3rf howa hyrg3 anhy object fel data
         * w e-> de hyrg3 feha el error lama ykon feh ay errors
         * */

//        Observer<MoviesModel> observer = new Observer<MoviesModel>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(MoviesModel value) {
//                movieMutableLiveData.postValue(value);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.wtf(TAG, "onError" + e);
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
    }

    @SuppressLint("CheckResult")
    public void getMoviesCategory() {
        Observable<MoviesCategory> observable = getClient().getMovieCategory(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(o -> moviesCategoryMutableLiveData.setValue(o)
                , e -> Log.d(TAG, "getMoviesCategory() onError: " + e));
    }

    public MutableLiveData<MoviesModel> movieMutableLiveData() {
        return movieMutableLiveData;
    }

    public MutableLiveData<MoviesCategory> movieCategoryMutableLiveData() {
        return moviesCategoryMutableLiveData;
    }

}
