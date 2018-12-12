package com.example.mostafa.movieapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{
    private MovieAdapter mAdapter;
    private RecyclerView mView;
    private Movie[] data = null;
    private Movie[] mFavorate;
    private URL link = null;
    private Movie[] movi;
    private  boolean isConnected;
    private String mSort="popular";
    private FetchJson mFetchJson = new FetchJson();
    private Context con = this;
    private boolean fav=false;
 @Override
    public void onClick(int mMoviePosition) {
        Intent intent = new Intent(MainActivity.this,MovieDetails.class);
        if(fav==false) {
            intent.putExtra("position", data[mMoviePosition]);
        }
        else{
            intent.putExtra("position", mFavorate[mMoviePosition]);
        }
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAdapter = new MovieAdapter(this);
        setContentView(R.layout.activity_main);

        //insert
//        ContentValues contentValues = new ContentValues();
//
//       contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, 218);
////
//
//     getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);


        //delete
//        String stringId = ""+218;
//        Uri uri = TaskContract.TaskEntry.CONTENT_URI;
//        uri = uri.buildUpon().appendPath(stringId).build();
//
//        // COMPLETED (2) Delete a single row of data using a ContentResolver
//        getContentResolver().delete(uri, null, null);

        //query
//       Cursor c= getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
//                null,
//                "movieid="+9,null,
//                null);
//     c.moveToNext();
//        int descriptionIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
//       int description = c.getInt(descriptionIndex);
//        Log.d("MainActivity","hwllo"+description);

        try {
            link = mFetchJson.getUrl(mSort);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetwork =cm.getActiveNetworkInfo();
        isConnected= activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            if (savedInstanceState == null) {
                new getMoviesData().execute(link);

            }
            else {
                Log.d("MainActivity","helloo saved");
                // Get data from local resources
                // Get Movie objects
                Parcelable[] parcelable = savedInstanceState.
                        getParcelableArray("saved");
                if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                movi = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movi[i] = (Movie) parcelable[i];
                }}
                data=movi;
                mView = (RecyclerView) findViewById(R.id.Movie_recycle);
                mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                if(fav)
                    mAdapter.getData(setFavorate());
                    else
                mAdapter.getData(movi);
                mView.setAdapter(mAdapter);

            }
        }
        else{Toast.makeText(con,"Please connect to internet",Toast.LENGTH_LONG).show();
        }
    }

    public class getMoviesData extends AsyncTask<URL, Void, Movie[]> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(URL... urls) {
            URL link = urls[0];

            String mJsonCode = null;
         try {
             mJsonCode = mFetchJson.getJsonStr(link);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                data = mFetchJson.getData(mJsonCode);

            } catch (JSONException e) {
                e.printStackTrace();
            }


           return data;

        }

        @Override
        protected void onPostExecute(Movie[] Movies) {

            mView = (RecyclerView) findViewById(R.id.Movie_recycle);
            mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            mAdapter.getData(Movies);
            mView.setAdapter(mAdapter);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
     if(fav==true)
     {
         mView = (RecyclerView) findViewById(R.id.Movie_recycle);
         mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
         mAdapter.getData(setFavorate());
         mView.setAdapter(mAdapter);

     }
        super.onResume();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {


              if(fav)
            outState.putParcelableArray("saved", setFavorate());
        else
                  outState.putParcelableArray("saved", data);

        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.rated:
                try {
                    fav=false;
                    link = mFetchJson.getUrl("top_rated");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                mSort="top_rated";
                ConnectivityManager cm1 = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo activeNetwork1 =cm1.getActiveNetworkInfo();
                isConnected= activeNetwork1!=null && activeNetwork1.isConnectedOrConnecting();
                if(isConnected)
                    new getMoviesData().execute(link);
                else{Toast.makeText(con,"Please connect to internet",Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.popular:

                try {
                    link = mFetchJson.getUrl("popular");
                    fav=false;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                mSort="popular";
                ConnectivityManager cm = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo activeNetwork =cm.getActiveNetworkInfo();
                isConnected= activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
                if(isConnected)
                    new getMoviesData().execute(link);
                else{Toast.makeText(con,"Please connect to internet",Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.rated2:
                fav=true;
                mView = (RecyclerView) findViewById(R.id.Movie_recycle);
                mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                mAdapter.getData(setFavorate());
                mView.setAdapter(mAdapter);

              return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public Movie[] setFavorate(){
        Cursor c= getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
              null,
               null,null, null);

        int descriptionIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        int overIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_OVERVIEW);
        int titleIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_TITLE);
        int dateIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_RELASE_DATE);
        int voteIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_VOTE_AVRAGE);
        int pathIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_POSTER_PATH);
         int count=0;


        mFavorate=new Movie[c.getCount()];
            for(int i =0;i<c.getCount();++i){
             c.moveToPosition(i);
             mFavorate[i]=new Movie();


               mFavorate[i].setmID(c.getInt(descriptionIndex));
                 mFavorate[i].setOverview(c.getString(overIndex));
                 mFavorate[i].setReleaseDate(c.getString(dateIndex));
                 mFavorate[i].setPosterPath(c.getString(pathIndex));



                 mFavorate[i].setVoteAverage(c.getDouble(voteIndex));
                 mFavorate[i].setOriginalTitle(c.getString(titleIndex));

            }

         c.close();
            return mFavorate;
    }

}
