package com.example.a5120app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * The AdapterIndoor program implements an class that
 * can accept lists of indoor activities data
 * to the indoor exercise list screen output.
 */

public class AdapterIndoor extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final String[] type;
    private int[] picture;

    /**
     * Initialize the adapter
     */
    public AdapterIndoor(Activity context, String[] name, String[] type, int[] picture) {
        super(context, R.layout.listview_row, name);
        this.context = context;
        this.name = name;
        this.type = type;
        this.picture = picture;
    }

    /**
     * Set the arrays to the list view's row
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listview_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.exercise_name_tv);
        TextView txtType = (TextView) rowView.findViewById(R.id.exercise_type_tv);
        ImageView exerciseImg = (ImageView) rowView.findViewById(R.id.exercise_image);

        txtTitle.setText(name[position]);
        txtType.setText(type[position]);
        exerciseImg.setImageResource(picture[position]);
        return rowView;
    }
}
