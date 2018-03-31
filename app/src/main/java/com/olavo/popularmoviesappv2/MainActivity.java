package com.olavo.popularmoviesappv2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static int PAGE = 1;
    public static String API_KEY = "54dc330ef61aa67f3cea8663c97489d7";
    public static String LANGUAGE = "en-US";
    public static String CATEGORY = "popular";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    GridLayoutManager mGridLayoutManager;

    private List<ListMovies> listMovies;
    public static String FAVORITES = "MyFavMovies";

    private Context context;
    public ImageView iv_movieLeft;

    DatabaseHelper myDb;
    ArrayList<String> listFavMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv_left = (ImageView) findViewById(R.id.iv_movieLeft);

        isOnline();
        loadRecyclerViewData();

        myDb = new DatabaseHelper(this);


    }

    public static class Utility {
        public static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (dpWidth / 180);
            return noOfColumns;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.topRated):
                CATEGORY = "top_rated";
                loadRecyclerViewData();
                return true;
            case (R.id.popular):
                CATEGORY = "popular";
                loadRecyclerViewData();
                return true;
            case (R.id.favorites):
                loadFavorites();
                return true;
        }
        return false;
    }

    private void loadFavorites() {

        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));

        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "You have no favorites saved", Toast.LENGTH_SHORT).show();
            loadRecyclerViewData();
            return;
        }

        listFavMovies = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface myInterface = retrofit.create(ApiInterface.class);


        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {

                String name = res.getString(res.getColumnIndex("POSTER_DETAIL"));
                Picasso.with(context)
                        .load("https://image.tmdb.org/t/p/w500//" +name)
                        .into(iv_movieLeft);

                listFavMovies.add(name);
                res.moveToNext();

            }
        }


        //adapter = new AdapterMovies(listFavMovies,MainActivity.this);
        //recyclerView.setAdapter(adapter);

    }

    private void loadRecyclerViewData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));

        listMovies = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface myInterface = retrofit.create(ApiInterface.class);

        Call<MovieResults> call = myInterface.listOfMovies(CATEGORY,API_KEY,LANGUAGE,PAGE);

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<MovieResults.ResultsBean> listOfMovies = results.getResults();

                adapter = new AdapterMovies(listOfMovies,MainActivity.this);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                t.printStackTrace();
            }
        });


        progressDialog.dismiss();

    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}

