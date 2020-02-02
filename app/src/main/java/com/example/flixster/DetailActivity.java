package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.Models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YoutubeAPIKey = "AIzaSyDQxvND_TDbYQAk0ButJ75Potw_fhkRvWk";
    public static final String VideoURL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        final Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VideoURL, movie.getMovieID()), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                try {

                    JSONArray results = json.jsonObject.getJSONArray("results");

                    if(results.length() == 0){
                        return;
                    }

                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", youtubeKey);

                    initializeYoutube(youtubeKey, movie);

                } catch (JSONException e) {

                    Log.e("DetailActivity", "Failed to parse", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    private void initializeYoutube(final String youtubeKey, final Movie movie) {

        youTubePlayerView.initialize(YoutubeAPIKey, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                Log.d("DetailActivity", "OnInitializationSuccess");

                if(movie.getRating() <= 5){

                    youTubePlayer.cueVideo(youtubeKey);

                } else{

                    youTubePlayer.loadVideo(youtubeKey); // Auto-play video
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                Log.d("DetailActivity", "OnInitializationFailure");
            }
        });
    }
}
