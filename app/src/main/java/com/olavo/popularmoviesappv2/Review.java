package com.olavo.popularmoviesappv2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Olavo on 4/1/2018.
 */

class Review {

    @SerializedName("key")
    private String key;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;

    public Review(String key, String author, String content){
        this.key = key;
        this.author = author;
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
