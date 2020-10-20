package com.example.moviesapp.data.api;

import com.example.moviesapp.data.model.castDetails.CastDetails;
import com.example.moviesapp.data.model.movieCast.MovieCast;
import com.example.moviesapp.data.model.moviesCategory.MoviesCategory;
import com.example.moviesapp.data.model.moviesModel.MoviesModel;
import com.example.moviesapp.data.model.moviesReview.MoviesReview;
import com.example.moviesapp.data.model.moviesTrailer.MoviesTrailer;
import com.example.moviesapp.data.model.moviesWithFilter.MoviesWithFilter;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //    @GET("movie/{list_type}")
//    Observable<MoviesModel> getMovie(@Path("list_type") String listType,
//                                     @Query("api_key") String apiKey,
//                                     @Query("page") int page);
    @GET("movie/{list_type}")
    Observable<MoviesModel> getMovie(@Path("list_type") String listType,
                                     @Query("api_key") String apiKey,
                                     @Query("page") int page);

    @GET("genre/movie/list")
    Call<MoviesCategory> getMovieCategory(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MoviesWithFilter> getMovieWithFilter(@Query("api_key") String apiKey,
                                              @Query("query") String search,
                                              @Query("page") int page);

    @GET("movie/{movie_id}/reviews")
    Call<MoviesReview> getMovieReview(@Path("movie_id") int movieId,
                                      @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<MovieCast> getMovieCast(@Path("movie_id") int movieId,
                                 @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<MoviesTrailer> getMovieTrailer(@Path("movie_id") String movieId,
                                        @Query("api_key") String apiKey);

    @GET("person/{person_id}")
    Call<CastDetails> getMovieCastDetails(@Path("person_id") int movieId,
                                          @Query("api_key") String apiKey);
}
