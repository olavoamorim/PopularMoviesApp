package com.olavo.popularmoviesappv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Olavo on 3/5/2018.
 */

public class DetailActivity extends AppCompatActivity {

    public TextView movieTitle;
    public ImageView posterDetail;
    public TextView releaseDate;
    public TextView rating;
    public TextView synopsis;
    public TextView trailer;
    public ImageView favorite_heart;
    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "";

    public static String FAVORITES = "MyFavMovies";

    private RecyclerView recyclerView;
    private AdapterTrailer adapter;
    private List<Trailer> trailerList;
    int movie_id;
    int i;

    DatabaseHelper myDb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        movie_id = getIntent().getExtras().getInt("id");

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

        loadJSON();
        loadReviews(movie_id);

        myDb = new DatabaseHelper(this);

        favorite_heart = findViewById(R.id.favorite_heart);
        final boolean color  = hasObject(String.valueOf(movie_id));
        favorite_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (color==true){
                    DeleteData();
                } else {
                    AddData();
                }
            }
        });
    }

    private void loadReviews(int movie_id) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface myInterface = retrofit.create(ApiInterface.class);

        Call<ReviewResponse> call = myInterface.getReviews(movie_id,API_KEY);

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                final List<Review> reviews = response.body().getResults();

                TextView author = (TextView) findViewById(R.id.author);
                TextView review = (TextView) findViewById(R.id.review);

                author.setText(reviews.get(1).getAuthor());
                review.setText(reviews.get(1).getContent());


            }
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(DetailActivity.this, "Error fetching trailer", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public boolean hasObject(String id) {
        SQLiteDatabase db = myDb.getWritableDatabase();
        String selectString = "SELECT * FROM " + "favorites_table" + " WHERE " + "MOVIE_ID" + " =?";

        Cursor cursor = db.rawQuery(selectString, new String[] {id});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
            favorite_heart.setColorFilter(Color.parseColor("#C8232C"), PorterDuff.Mode.SRC_ATOP);
            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }


        } else {
            favorite_heart.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
        }

        cursor.close();
        db.close();
        return hasObject;
    }

    public void AddData(){
                boolean isInserted = myDb.insertData(getIntent().getExtras().getInt("id"),getIntent().getStringExtra("movieTitle"),
                        getIntent().getStringExtra("posterDetail"),getIntent().getStringExtra("releaseDate"),
                        getIntent().getStringExtra("rating"),getIntent().getStringExtra("synopsis"));
                if (isInserted==true){
                    Toast.makeText(DetailActivity.this, "Saved to you favorites", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#C8232C"), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Toast.makeText(DetailActivity.this, "Error saving to favorites", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
                }
    }

    public void DeleteData() {
                Integer deleteRows = myDb.deleteData(String.valueOf(movie_id));
                if (deleteRows>0){
                    Toast.makeText(DetailActivity.this, "Favorite removed", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Toast.makeText(DetailActivity.this, "Error removing favorite", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#C8232C"), PorterDuff.Mode.SRC_ATOP);
                }
    }

    private void loadJSON() {
        //final int movie_id = getIntent().getExtras().getInt("id");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface myInterface = retrofit.create(ApiInterface.class);

        Call<TrailerResponse> call = myInterface.getMovieTrailer(movie_id,API_KEY);

        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                final List<Trailer> trailer = response.body().getResults();

                TextView trailer_link = (TextView) findViewById(R.id.trailer_link);

                trailer_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String videoId = trailer.get(4).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.youtube.com/watch?v="+videoId));
                        startActivity(intent);
                    }
                });

            }
            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(DetailActivity.this, "Error fetching trailer", Toast.LENGTH_SHORT).show();
            }
        });



    }
}