package com.olavo.popularmoviesappv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olavo on 2/28/2018.
 */

public class AdapterMovies extends RecyclerView.Adapter<AdapterMovies.ViewHolder>  {

    private ArrayList<String> listFavMovies;
    private List<MovieResults.ResultsBean> listMovies;
    private Context context;

    public AdapterMovies(List<MovieResults.ResultsBean> listMovies, Context context) {
        this.listMovies = listMovies;
        this.context = context;
    }

    public AdapterMovies(ArrayList<String> listFavMovies, Context context) {
        this.listFavMovies = listFavMovies;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movies, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieResults.ResultsBean listMovies1 = listMovies.get(position);
        holder.iv_movieLeft.setImageURI(Uri.parse(listMovies1.getPoster_path()));

        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500//" +listMovies1.getPoster_path())
                .into(holder.iv_movieLeft);

    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_movieLeft;
        public TextView movieTitle;
        public ImageView posterDetail;
        public TextView releaseDate;
        public TextView rating;
        public TextView synopsis;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_movieLeft = (ImageView) itemView.findViewById(R.id.iv_movieLeft);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            posterDetail = (ImageView) itemView.findViewById(R.id.posterDetail);
            releaseDate = (TextView) itemView.findViewById(R.id.releaseDate);
            rating = (TextView) itemView.findViewById(R.id.rating);
            synopsis = (TextView) itemView.findViewById(R.id.synopsis);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        MovieResults.ResultsBean clickedDataItem = listMovies.get(pos);
                        Intent intent = new Intent(context, DetailActivity.class);

                        intent.putExtra("movieTitle", clickedDataItem.getOriginal_title());
                        intent.putExtra("posterDetail", clickedDataItem.getPoster_path());
                        intent.putExtra("releaseDate", clickedDataItem.getRelease_date());
                        intent.putExtra("rating", Double.toString(clickedDataItem.getVote_average()));
                        intent.putExtra("synopsis", clickedDataItem.getOverview());
                        intent.putExtra("id", clickedDataItem.getId());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                }
            });
        }
    }
}


