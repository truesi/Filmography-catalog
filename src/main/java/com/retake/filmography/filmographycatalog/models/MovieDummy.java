package com.retake.filmography.filmographycatalog.models;

import lombok.Data;

public @Data
class MovieDummy {
    private String name;
    private String movieID;
    private String description;

    public MovieDummy(){}

    public MovieDummy(String name, String movieID, String description) {
        this.name = name;
        this.movieID = movieID;
        this.description = description;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
