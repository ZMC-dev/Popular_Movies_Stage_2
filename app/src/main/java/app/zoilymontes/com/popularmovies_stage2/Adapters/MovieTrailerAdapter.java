package app.zoilymontes.com.popularmovies_stage2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import app.zoilymontes.com.popularmovies_stage2.Models.MovieTrailer;
import app.zoilymontes.com.popularmovies_stage2.R;

public class MovieTrailerAdapter extends ArrayAdapter<MovieTrailer> {

    public MovieTrailerAdapter(@NonNull Context context, @NonNull List<MovieTrailer> objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_layout,parent,false
            );

        }

        MovieTrailer currentMovieTrailer = getItem(position);

        TextView dateView = listItemView.findViewById(R.id.v_name);
        dateView.setText(currentMovieTrailer.getmMovieTrailerName());

        return listItemView;
    }

}