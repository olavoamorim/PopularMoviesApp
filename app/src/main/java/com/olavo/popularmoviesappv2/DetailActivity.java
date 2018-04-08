package com.olavo.popularmoviesappv2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
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
    String MovTit;
    public ImageView posterDetail;
    String PosDet;
    public TextView releaseDate;
    String RelDat;
    public TextView rating;
    String Rat;
    public TextView synopsis;
    String Syn;
    public TextView trailer;
    public ImageView favorite_heart;
    public TextView author;
    public TextView review;

    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "54dc330ef61aa67f3cea8663c97489d7";

    public static String FAVORITES = "MyFavMovies";

    private RecyclerView recyclerView;
    private AdapterTrailer adapter;
    private List<Trailer> trailerList;
    List<Review> reviews = new ArrayList<>();
    int movie_id;
    int i;

    DatabaseHelper myDb;

    String autor;
    String comentario;


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

        author = (TextView) findViewById(R.id.author);
        review = (TextView) findViewById(R.id.review);

        MovTit = getIntent().getStringExtra("movieTitle");
        PosDet = getIntent().getStringExtra("posrterDetail");
        RelDat = getIntent().getStringExtra("releaseDate");
        Rat = getIntent().getStringExtra("rating");
        Syn = getIntent().getStringExtra("synopsis");


        movieTitle.setText(getIntent().getStringExtra("movieTitle"));
        releaseDate.setText(getIntent().getStringExtra("releaseDate"));
        rating.setText(getIntent().getStringExtra("rating"));
        synopsis.setText(getIntent().getStringExtra("synopsis"));

            Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w500//" +getIntent().getStringExtra("posterDetail"))
                .into(posterDetail);


        if (savedInstanceState==null){
            loadReviews(movie_id);
        } else {
            MovTit = savedInstanceState.getString("MovTit");
            PosDet = savedInstanceState.getString("PosDet");
            RelDat = savedInstanceState.getString("RelDat");
            Rat = savedInstanceState.getString("Rat");
            Syn = savedInstanceState.getString("Syn");
            autor = savedInstanceState.getString("autor");
            author.setText(autor);
            comentario = savedInstanceState.getString("comentario");
            review.setText(comentario);
        }
        loadJSON();

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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("MovTit", MovTit);
        outState.putString("PosDet", PosDet);
        outState.putString("RelDat", RelDat);
        outState.putString("Rat", Rat);
        outState.putString("Syn", Syn);
        outState.putString("autor", autor);
        outState.putString("comentario", comentario);

        super.onSaveInstanceState(outState);
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
                reviews = response.body().getResults();



                if (reviews.get(0).getContent()==null){
                    author.setText("");
                    review.setText("No reviews yet");
                } else {
                    autor = reviews.get(0).getAuthor();
                    comentario = reviews.get(0).getContent();
                    author.setText(reviews.get(0).getAuthor());
                    review.setText(reviews.get(0).getContent());
                }

            }
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(DetailActivity.this, "Error fetching review", Toast.LENGTH_SHORT).show();
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

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesDb.FavoritesEntry.COL_2, getIntent().getExtras().getInt("id"));
        contentValues.put(FavoritesDb.FavoritesEntry.COL_3, getIntent().getStringExtra("movieTitle"));
        contentValues.put(FavoritesDb.FavoritesEntry.COL_4, getIntent().getStringExtra("posterDetail"));
        contentValues.put(FavoritesDb.FavoritesEntry.COL_5, getIntent().getStringExtra("releaseDate"));
        contentValues.put(FavoritesDb.FavoritesEntry.COL_6, getIntent().getStringExtra("rating"));
        contentValues.put(FavoritesDb.FavoritesEntry.COL_7, getIntent().getStringExtra("synopsis"));

        Uri uri = getContentResolver().insert(FavoritesDb.FavoritesEntry.CONTENT_URI, contentValues);

        if (uri != null){
            //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(DetailActivity.this, "Saved to you favorites", Toast.LENGTH_SHORT).show();
            favorite_heart.setColorFilter(Color.parseColor("#C8232C"), PorterDuff.Mode.SRC_ATOP);
        } else {
        Toast.makeText(DetailActivity.this, "Error saving to favorites", Toast.LENGTH_SHORT).show();
        favorite_heart.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
        }

                /*boolean isInserted = myDb.insertData(getIntent().getExtras().getInt("id"),getIntent().getStringExtra("movieTitle"),
                        getIntent().getStringExtra("posterDetail"),getIntent().getStringExtra("releaseDate"),
                        getIntent().getStringExtra("rating"),getIntent().getStringExtra("synopsis"));
                if (isInserted==true){
                    Toast.makeText(DetailActivity.this, "Saved to you favorites", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#C8232C"), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Toast.makeText(DetailActivity.this, "Error saving to favorites", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
                }*/
    }

    public void DeleteData() {

        Uri uri = FavoritesDb.FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movie_id)).build();

        getContentResolver().delete(uri,null,null);

        if (uri != null){
            Toast.makeText(DetailActivity.this, "Favorite removed", Toast.LENGTH_SHORT).show();
            favorite_heart.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
        } else {
            Toast.makeText(DetailActivity.this, "Error removing favorite", Toast.LENGTH_SHORT).show();
            favorite_heart.setColorFilter(Color.parseColor("#C8232C"), PorterDuff.Mode.SRC_ATOP);
        }

                /*Integer deleteRows = myDb.deleteData(String.valueOf(movie_id));
                if (deleteRows>0){
                    Toast.makeText(DetailActivity.this, "Favorite removed", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Toast.makeText(DetailActivity.this, "Error removing favorite", Toast.LENGTH_SHORT).show();
                    favorite_heart.setColorFilter(Color.parseColor("#C8232C"), PorterDuff.Mode.SRC_ATOP);
                }*/
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
                        if (trailer.get(4).getKey()==null){
                            Toast.makeText(DetailActivity.this, "Sorry, no trailer available", Toast.LENGTH_SHORT).show();
                        } else {
                            String videoId = trailer.get(4).getKey();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                            startActivity(intent);
                        }
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