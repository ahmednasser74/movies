
package com.example.moviesapp.data.model.movieCast;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieCast {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<MovieCastData> cast = null;
    @SerializedName("crew")
    @Expose
    private List<MovieCrewData> crew = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieCastData> getCast() {
        return cast;
    }

    public void setCast(List<MovieCastData> cast) {
        this.cast = cast;
    }

    public List<MovieCrewData> getCrew() {
        return crew;
    }

    public void setCrew(List<MovieCrewData> crew) {
        this.crew = crew;
    }

}
