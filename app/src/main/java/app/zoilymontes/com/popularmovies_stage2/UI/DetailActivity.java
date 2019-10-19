package app.zoilymontes.com.popularmovies_stage2.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import app.zoilymontes.com.popularmovies_stage2.R;
import app.zoilymontes.com.popularmovies_stage2.Utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private TextView movieName,movieDates,movieVotes,movieSummary,movieReview;
    private ImageView imageViewInDetailsPoster;
    public int intGotPosition;
    private String REVIEW_URL;
    private String VIDEO_URL;
    private URL url_for_review;
    private URL url_for_video;
    private int totalLength;
    public static String[] authors=new String[100];
    public static String[] content=new String[100];
    public static String[] keys;
    public static String[] video_name;
    private int flag=0;
    public static MoviesDB mdb = new MoviesDB();
    public static int scrollX = 0;
    public static int scrollY = -1;
    public static NestedScrollView nestedScrollView;
    public static AppBarLayout appBarLayout;

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION",
                new int[]{ nestedScrollView.getScrollX(), nestedScrollView.getScrollY()});
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(scrollX==0 && scrollY==0){
            appBarLayout.setExpanded(true);
        }else {
            appBarLayout.setExpanded(false);
        }
        final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if(position != null)
            nestedScrollView.post(new Runnable() {
                public void run() {
                    nestedScrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieName= findViewById(R.id.movieTitleValue);
        movieDates= findViewById(R.id.movieReleaseDateValue);
        movieVotes= findViewById(R.id.movieVoteValue);
        movieSummary= findViewById(R.id.movieSummaryValue);
        imageViewInDetailsPoster= findViewById(R.id.imageViewPosterDetails);
        movieReview= findViewById(R.id.movieReviewValue);
        final FloatingActionButton btn_fav = findViewById(R.id.button_fav);
        final FloatingActionButton btn_play = findViewById(R.id.button_play);
        nestedScrollView=findViewById(R.id.nested);
        appBarLayout=findViewById(R.id.app_bar);

        String gotPosition=getIntent().getStringExtra("position");
        intGotPosition=Integer.parseInt(gotPosition);


        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(MainActivity.movies[intGotPosition]);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }



        int idIntValue=Integer.parseInt(MainActivity.id[intGotPosition]);
        if(mdb.isFavoriteMovie(MainActivity.contentResolver, idIntValue)){
            btn_fav.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.ic_favorite_white_24px));
        }else{
            btn_fav.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.ic_favorite_border_white_24px));
        }


        ImageView toolbarImage =  findViewById(R.id.image_id);

        String url = "https://image.tmdb.org/t/p/w1280"+MainActivity.backdrop[intGotPosition];
        picassoLoader(this, toolbarImage, url);

        movieName.setText(MainActivity.movies[intGotPosition]);
        movieDates.setText(MainActivity.dates[intGotPosition]);
        movieVotes.setText(MainActivity.votes[intGotPosition]+" / 10");
        movieSummary.setText("\t\t"+MainActivity.summary[intGotPosition]);
        Picasso.with(DetailActivity.this)
                .load("https://image.tmdb.org/t/p/w500"+MainActivity.poster[intGotPosition])
                .into(imageViewInDetailsPoster);
        imageViewInDetailsPoster.setVisibility(View.VISIBLE);


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DetailActivity.this, MovieTrailerActivity.class);
                startActivity(i);
            }
        });


        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idIntValue=Integer.parseInt(MainActivity.id[intGotPosition]);
                if(mdb.isFavoriteMovie(MainActivity.contentResolver, idIntValue)){
                    mdb.removeMovie(MainActivity.contentResolver, idIntValue);
                    btn_fav.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.ic_favorite_border_white_24px));
                    Snackbar.make(view, "Removed from Favorites", Snackbar.LENGTH_SHORT).show();
                }else{
                    mdb.addMovie(MainActivity.contentResolver, intGotPosition);
                    btn_fav.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.ic_favorite_white_24px));
                    Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_SHORT).show();
                }
                mdb.getFavoriteMovies(MainActivity.contentResolver);
            }
        });

        REVIEW_URL="https://api.themoviedb.org/3/movie/"+MainActivity.id[intGotPosition]+"/reviews?api_key="+getResources().getString(R.string.API_key);
        VIDEO_URL="http://api.themoviedb.org/3/movie/"+MainActivity.id[intGotPosition]+"/videos?api_key="+getResources().getString(R.string.API_key);
        getReviews();

    }

    public void picassoLoader(Context context, ImageView imageView, String url){
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.background_black)
                .error(R.drawable.background_black)
                .into(imageView);
    }

    public void getReviews(){
        try{
            url_for_review=new URL(REVIEW_URL);
            url_for_video=new URL(VIDEO_URL);
        }catch (Exception e){
            Toast.makeText(DetailActivity.this,"Error while building URL..",Toast.LENGTH_LONG).show();
        }
        new ReceiveReviews().execute(url_for_review,url_for_video);
    }

    public class ReceiveReviews extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL gotUrl=urls[0];
            URL viUrl=urls[1];
            String review=null;
            String video=null;
            try{
                review= NetworkUtils.getResponseFromHttpUrl(gotUrl);
                video= NetworkUtils.getResponseFromHttpUrl(viUrl);
                try{
                    JSONObject JO=new JSONObject(review);
                    JSONArray JA= JO.getJSONArray("results");
                    totalLength =JA.length();
                    if(JA.length()==0){
                        flag=1;
                    }
                    for(int i=0;i<=JA.length();i++) {
                        JSONObject Jinside=JA.getJSONObject(i);
                        authors[i]=Jinside.getString("author");
                        content[i]=Jinside.getString("content");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                try{
                    JSONObject JO=new JSONObject(video);
                    JSONArray JA= JO.getJSONArray("results");
                    keys=new String[JA.length()];
                    video_name=new String[JA.length()];
                    for (int i=0;i<=JA.length();i++){
                        keys[i]=JA.getJSONObject(i).getString("key");
                        video_name[i]=JA.getJSONObject(i).getString("name");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }catch(Exception e) {

            }

            return review;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(flag==1){
                movieReview.setText("No Reviews to show..\n");
            }else {
                String temp="";
                for(int i = 0; i< totalLength; i++){
                    temp=temp+"\t\tAuthor: "+authors[i]+"\n\t\tReview: "+content[i]+"\n"+"-------------------------------"+"\n";
                }
                movieReview.setText(temp+"");
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        scrollX = nestedScrollView.getScrollX();
        scrollY = nestedScrollView.getScrollY();
    }
    @Override
    protected void onResume() {
        super.onResume();
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.scrollTo(scrollX, scrollY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detailed, menu);
        return super.onCreateOptionsMenu(menu);
    }

}