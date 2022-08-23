package com.example.skucise.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class LikeResponse {
    @Min(0)
    @Max(Integer.MAX_VALUE)
    int totalLikes;

    boolean alreadyLiked;

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public boolean isAlreadyLiked() {
        return alreadyLiked;
    }

    public void setAlreadyLiked(boolean alreadyLiked) {
        this.alreadyLiked = alreadyLiked;
    }
}
