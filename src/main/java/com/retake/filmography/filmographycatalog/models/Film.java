package com.retake.filmography.filmographycatalog.models;


import lombok.Data;

public @Data
class Film {
    private int id;

    private String title;

    private int year;

    private String genre;

    private boolean watched;

    private String rating;
}
