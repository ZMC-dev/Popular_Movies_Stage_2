package app.zoilymontes.com.popularmovies_stage2.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import app.zoilymontes.com.popularmovies_stage2.Adapters.MovieTrailerAdapter;
import app.zoilymontes.com.popularmovies_stage2.Models.MovieTrailer;
import app.zoilymontes.com.popularmovies_stage2.R;

public class MovieTrailerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ImageView imageView=findViewById(R.id.image_view_youtube);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+ DetailActivity.keys[0])));
            }
        });


        ArrayList<MovieTrailer> trailers=new ArrayList<>();

        for(int i = 0; i< DetailActivity.video_name.length; i++) {
            trailers.add(new MovieTrailer(DetailActivity.video_name[i]));
        }

        ListView videoListView= findViewById(R.id.listView);

        MovieTrailerAdapter adapter = new MovieTrailerAdapter(MovieTrailerActivity.this, trailers);

        videoListView.setAdapter(adapter);

        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+ DetailActivity.keys[position])));
            }
        });


    }
}
