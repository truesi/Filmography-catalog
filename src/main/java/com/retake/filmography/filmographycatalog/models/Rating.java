package com.retake.filmography.filmographycatalog.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating {
    private String movieID;
    private int rating;

    public Rating(){
    }

    public Rating(String movieID, int rating){
        this.movieID = movieID;
        this.rating = rating;
    }
}
