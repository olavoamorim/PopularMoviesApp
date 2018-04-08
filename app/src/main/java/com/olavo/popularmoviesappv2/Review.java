package com.olavo.popularmoviesappv2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Olavo on 4/1/2018.
 */

class Review implements Parcelable{

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

    public Review(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);

        this.key = data[0];
        this.author = data[1];
        this.content = data[2];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.key,
                this.author,
                this.content});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}

