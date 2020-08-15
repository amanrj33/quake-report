package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Activity activity, ArrayList<Earthquake> earthquakes){
     super(activity,0,earthquakes);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Earthquake current = getItem(position);
        View listItemView = convertView;
        if(listItemView==null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item,parent,false);
        TextView magnitudeTextView = listItemView.findViewById(R.id.mag);
        double magnitude = current.getMagnitude();
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColor = getMagnitudeColor(magnitude);
        magnitudeColor = ContextCompat.getColor(getContext(), magnitudeColor);
        magnitudeCircle.setColor(magnitudeColor);
        magnitudeTextView.setText(formatMagnitude(magnitude));
        TextView locationTextView = listItemView.findViewById(R.id.loc);
        TextView offsetTextView = listItemView.findViewById(R.id.offset);
        String location = current.getLocation();
        String primaryLocation = "",locationOffset = "";
        if(location.contains(LOCATION_SEPARATOR)) {
            String[] parts = location.split(LOCATION_SEPARATOR);
            locationOffset = parts[0]+" of";
            primaryLocation = parts[1];
        }
        else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = location;
        }
        locationTextView.setText(primaryLocation);
        offsetTextView.setText(locationOffset);
        Date date = new Date(current.getDate());
        TextView dateTextView = listItemView.findViewById(R.id.date);
        dateTextView.setText(formatDate(date));
        TextView timeTextView = listItemView.findViewById(R.id.time);
        timeTextView.setText(formatTime(date));
        return listItemView;
    }

    private String formatDate(Date date){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, YYYY");
        return dateFormatter.format(date);
    }
    private String formatTime(Date date){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        return timeFormatter.format(date);
    }
    private String formatMagnitude(Double mag){
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(mag);
    }
    private int getMagnitudeColor(double mag){
        switch ((int)mag){
            case 0:
            case 1:
                return R.color.magnitude1;
            case 2:
                return R.color.magnitude2;
            case 3:
                return R.color.magnitude3;
            case 4:
                return R.color.magnitude4;
            case 5:
                return R.color.magnitude5;
            case 6:
                return R.color.magnitude6;
            case 7:
                return R.color.magnitude7;
            case 8:
                return R.color.magnitude8;
            case 9:
                return R.color.magnitude9;
            default:
                return R.color.magnitude10plus;
        }
    }
}
