package app.zoilymontes.com.popularmovies_stage2.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import app.zoilymontes.com.popularmovies_stage2.R;

public class FavoriteMovieDetails extends AppCompatActivity {

    public int intGotPosition;

    private TextView movieName, movieDates, movieVotes, movieSummary;
    private ImageView imageViewInDetailsPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_details);

        movieName = findViewById(R.id.movieTitleValue);
        movieDates = findViewById(R.id.movieReleaseDateValue);
        movieVotes = findViewById(R.id.movieVoteValue);
        movieSummary = findViewById(R.id.movieSummaryValue);
        imageViewInDetailsPoster = findViewById(R.id.imageViewPosterDetails);
        final FloatingActionButton fab2 = findViewById(R.id.button_fav);

        String gotPosition = getIntent().getStringExtra("position");
        int intGotPosition = Integer.parseInt(gotPosition);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(MoviesDB.fav_movies[intGotPosition]);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        movieName.setText(MoviesDB.fav_movies[intGotPosition]);
        movieDates.setText(MoviesDB.fav_dates[intGotPosition]);
        movieVotes.setText(MoviesDB.fav_votes[intGotPosition] + " / 10");
        movieSummary.setText("\t\t" + MoviesDB.fav_summary[intGotPosition]);
        Picasso.with(FavoriteMovieDetails.this)
                .load("https://image.tmdb.org/t/p/w500" + MoviesDB.fav_poster[intGotPosition])
                .into(imageViewInDetailsPoster);
        imageViewInDetailsPoster.setVisibility(View.VISIBLE);

    }

}
