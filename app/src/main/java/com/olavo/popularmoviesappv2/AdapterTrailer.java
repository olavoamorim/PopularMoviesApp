package com.olavo.popularmoviesappv2;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Olavo on 3/21/2018.
 */

public class AdapterTrailer extends RecyclerView.Adapter<AdapterTrailer.MyViewHolder> {

    private Context mContext;
    private List<Trailer> trailerList;


    public AdapterTrailer(List<Trailer> trailerList, Context mContext) {
        this.trailerList = trailerList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("ResourceType") View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.id.trailer_link, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterTrailer.MyViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView trailer_link;

        public MyViewHolder(View view){
            super(view);
            /*trailer_link = (TextView) view.findViewById(R.id.trailer_link);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String videoId = trailerList.get(pos).getKey();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v="+videoId));
                    //intent.putExtra("VIDEO_ID",videoId);
                    mContext.startActivity(intent);
                }
            });*/

        }
    }
}
