package com.olavo.popularmoviesappv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Olavo on 3/5/2018.
 */

public class DetailActivity extends AppCompatActivity {

    public TextView movieTitle;
    public ImageView posterDetail;
    public TextView releaseDate;
    public TextView rating;
    public TextView synopsis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        movieTitle = (TextView) findViewById(R.id.movieTitle);
        posterDetail = (ImageView) findViewById(R.id.posterDetail);
        releaseDate = (TextView) findViewById(R.id.releaseDate);
        rating = (TextView) findViewById(R.id.rating);
        synopsis = (TextView) findViewById(R.id.synopsis);

        movieTitle.setText(getIntent().getStringExtra("movieTitle"));
        releaseDate.setText(getIntent().getStringExtra("releaseDate"));
        rating.setText(getIntent().getStringExtra("rating"));
        synopsis.setText(getIntent().getStringExtra("synopsis"));

            Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w500//" +getIntent().getStringExtra("posterDetail"))
                .into(posterDetail);



    }
}