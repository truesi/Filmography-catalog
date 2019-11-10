package com.retake.filmography.filmographycatalog.models;

import lombok.Data;

import java.util.List;

public @Data class UserRating {

    private String userId;
    private List<Rating> userRating;


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<Rating> getUserRating() {
        return userRating;
    }

    public void setUserRating(List<Rating> userRating) {
        this.userRating = userRating;
    }
}
