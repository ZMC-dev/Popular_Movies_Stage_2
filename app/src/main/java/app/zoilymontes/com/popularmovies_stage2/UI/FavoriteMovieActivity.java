package app.zoilymontes.com.popularmovies_stage2.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import app.zoilymontes.com.popularmovies_stage2.Adapters.FavoriteMovieAdapter;
import app.zoilymontes.com.popularmovies_stage2.R;

public class FavoriteMovieActivity extends AppCompatActivity {

    GridView favoriteGridView;
    private static final String GRID_VIEW_POSITION="gridViewPos";
    private int gridPos = -1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GRID_VIEW_POSITION, favoriteGridView.getFirstVisiblePosition());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        favoriteGridView=findViewById(R.id.fav_gridview);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(MoviesDB.fav_poster!=null) {
            favoriteGridView.setAdapter(new FavoriteMovieAdapter(FavoriteMovieActivity.this));
            favoriteGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent i = new Intent(FavoriteMovieActivity.this, FavoriteMovieDetails.class);
                    i.putExtra("position", position + "");
                    startActivity(i);
                }

            });
            if (gridPos > -1)
                favoriteGridView.setSelection(gridPos);
        }else{
            favoriteGridView.setVisibility(View.INVISIBLE);
        }
    }
}