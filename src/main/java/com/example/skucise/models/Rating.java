package com.example.skucise.models;

public class Rating {

    double rating;
    boolean alreadyRated;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isAlreadyRated() {
        return alreadyRated;
    }

    public void setAlreadyRated(boolean alreadyRated) {
        this.alreadyRated = alreadyRated;
    }
}
