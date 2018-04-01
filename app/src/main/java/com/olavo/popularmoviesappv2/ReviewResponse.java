package com.olavo.popularmoviesappv2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Olavo on 4/1/2018.
 */

class ReviewResponse {

    @SerializedName("id")
    private int id_review;
    @SerializedName("results")
    private List<Review> results;

    public int getId_review() {
        return id_review;
    }

    public void setId_review(int id_review) {
        this.id_review = id_review;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
