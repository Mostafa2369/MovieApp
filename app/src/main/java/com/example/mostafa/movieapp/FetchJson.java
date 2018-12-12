package com.example.mostafa.movieapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class FetchJson {
    //https://api.themoviedb.org/3/movie/<id>/videos?api_key=<key>&language=en-US
    private HttpURLConnection urlConnection = null;

    private String mApiKey = "3f240f7d8a4b186fa413732eeb4146ea";

    public URL getUrl(String sort) throws MalformedURLException {

        final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/"+sort+"?";

        final String API_KEY_PARAM = "api_key";
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, mApiKey)
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("page", "1")
                .build();
        return new URL(builtUri.toString());
    }

    public String getJsonStr(URL Url) throws IOException {
        URL url = Url;

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner mScan = new Scanner(inputStream);
            mScan.useDelimiter("\\A");
            boolean hasIn = mScan.hasNext();
            if (hasIn)
                return mScan.next();
            else
                return null;
        } finally {


            urlConnection.disconnect();
        }

    }

    public Movie[] getData(String JsonStr) throws JSONException {
        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        JSONObject moviesRoot = new JSONObject(JsonStr);
        JSONArray resultsArray = moviesRoot.getJSONArray(RESULTS);
        Movie[] movies = new Movie[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {

            JSONObject movieInfo = resultsArray.getJSONObject(i);
            movies[i] = new Movie();
            movies[i].setmID(movieInfo.getInt("id"));
            movies[i].setOriginalTitle(movieInfo.getString(ORIGINAL_TITLE));
            movies[i].setPosterPath(movieInfo.getString(POSTER_PATH));
            movies[i].setOverview(movieInfo.getString(OVERVIEW));
            movies[i].setVoteAverage(movieInfo.getDouble(VOTE_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(RELEASE_DATE));
        }

        return movies;
    }

}







