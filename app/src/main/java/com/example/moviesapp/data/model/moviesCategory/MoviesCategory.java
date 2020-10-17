
package com.example.moviesapp.data.model.moviesCategory;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesCategory {

    @SerializedName("genres")
    @Expose
    private List<MovieCategoryData> genres = null;

    public List<MovieCategoryData> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieCategoryData> genres) {
        this.genres = genres;
    }

}
