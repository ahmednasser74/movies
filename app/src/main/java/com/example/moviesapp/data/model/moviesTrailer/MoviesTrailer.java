
package com.example.moviesapp.data.model.moviesTrailer;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesTrailer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieTrailerData> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieTrailerData> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailerData> results) {
        this.results = results;
    }

}
