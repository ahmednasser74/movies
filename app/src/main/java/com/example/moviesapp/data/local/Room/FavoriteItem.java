package com.example.moviesapp.data.local.Room;

import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteItem {
    //TODO: ha3ml el favorite item hena heya ely ll room w hashel elmovie data model mn el room
    @PrimaryKey(autoGenerate = true)
    private int movieId;
    private String movieImg;
    private double movieRate;
    private String movieName;
    private String movieReleasedDate;

    public FavoriteItem(int movieId, String movieImg, double movieRate, String movieName, String movieReleasedDate) {
        this.movieId = movieId;
        this.movieImg = movieImg;
        this.movieRate = movieRate;
        this.movieName = movieName;
        this.movieReleasedDate = movieReleasedDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }

    public double getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(double movieRate) {
        this.movieRate = movieRate;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieReleasedDate() {
        return movieReleasedDate;
    }

    public void setMovieReleasedDate(String movieReleasedDate) {
        this.movieReleasedDate = movieReleasedDate;
    }
}
