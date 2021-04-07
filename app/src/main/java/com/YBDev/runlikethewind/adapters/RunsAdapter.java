package com.YBDev.runlikethewind.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.database.Run;
import com.YBDev.runlikethewind.util.TrackingUtility;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RunsAdapter extends RecyclerView.Adapter<RunsAdapter.ViewHolder> {


    private final Context context;
    private final List<Run> runs;

    // data is passed into the constructor
    public RunsAdapter(Context context, List<Run> runs) {
     this.context = context;
     this.runs = runs;
    }


    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_run, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Run run = runs.get(position);
        Glide.with(context)
                .load(run.getBitmap())
                .into(holder.runImage);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = formatter.format(new Date(run.getStartTimeInMilliseconds()));

        holder.avgSpeed.setText("Avg speed "+ run.getAvgSpeedInKMH());
        holder.calories.setText("Calories burned "+ run.getCaloriesBurned());
        holder.date.setText("Date " + dateString);
        holder.distance.setText("Distance "+run.getDistanceInMeters()+"");
        holder.time.setText("Run Time " +  TrackingUtility.getFormattedStopWatchTime(run.getTotalTimeInMilliseconds(), true));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return runs.size();
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView runImage;
        MaterialTextView date;
        MaterialTextView time;
        MaterialTextView distance;
        MaterialTextView avgSpeed;
        MaterialTextView calories;
        ViewHolder(View view) {
            super(view);

            //find views
            runImage = view.findViewById(R.id.runImage);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            distance = view.findViewById(R.id.distance);
            avgSpeed = view.findViewById(R.id.avgSpeed);
            calories = view.findViewById(R.id.calories);

        }
    }
}
