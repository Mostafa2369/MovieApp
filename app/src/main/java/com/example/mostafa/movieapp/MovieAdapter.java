package com.example.mostafa.movieapp;

import android.content.Context;
import com.squareup.picasso.Picasso;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    public Movie[] data;
    private  MovieAdapterOnClickHandler mClickHandler=null;
    public MovieAdapter(MovieAdapterOnClickHandler click) {
        mClickHandler = click;
    }

    public interface MovieAdapterOnClickHandler
    {

        void onClick(int hello);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final   ImageView poster;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.text1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mAdapterPosition = getAdapterPosition();
            mClickHandler.onClick(mAdapterPosition);

        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        Movie new1 = data[position];
if(new1.getPosterPath().length()<90){
       Picasso.get()
              .load(new1.getPosterPath())
               .resize(185, 278)
              .centerCrop()
              .into(holder.poster);}
              else{Picasso.get()
        .load(new1.getPosterPath2())
        .resize(185, 278)
        .centerCrop()
        .into(holder.poster);}


}



    @Override
    public int getItemCount() {
        return data.length;
    }

    public void getData(Movie[] data1) {

        data=data1;

        notifyDataSetChanged();

    }
}
