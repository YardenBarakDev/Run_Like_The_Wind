package com.YBDev.runlikethewind.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.database.Run;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomMarkerView extends MarkerView {

    private final List<Run> runList;
    private int layoutId;

    private TextView marker_Date;
    private TextView marker_Duration;
    private TextView marker_AvgSpeed;
    private TextView marker_Distance;
    private TextView marker_CaloriesBurned;


    public CustomMarkerView(Context context, int layoutResource, List<Run> runs) {
        super(context, layoutResource);
        this.runList = runs;
        this.layoutId = layoutResource;
        findAllViews();
    }

    @Override
    public MPPointF getOffset(){
        return new MPPointF(- this.getWidth() /2f, - this.getHeight()+0f);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        this.getRootView().findViewById(R.id.marker_AvgSpeed);

        int currentRun = (int) e.getX();
        Run run = runList.get(currentRun);


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = formatter.format(new Date(run.getStartTimeInMilliseconds()));

        DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String formatted = df.format(run.getTotalTimeInMilliseconds());

        marker_AvgSpeed.setText("Avg speed "+ run.getAvgSpeedInKMH());
        marker_CaloriesBurned.setText("Calories burned "+ run.getCaloriesBurned());
        marker_Date.setText("date " + dateString);
        marker_Distance.setText("Distance "+run.getDistanceInMeters()+"");
        marker_Duration.setText("Run Time " + formatted);

    }


    private void findAllViews() {
        marker_Date = findViewById(R.id.marker_Date);
        marker_Duration = findViewById(R.id.marker_Duration);
        marker_AvgSpeed = findViewById(R.id.marker_AvgSpeed);
        marker_Distance = findViewById(R.id.marker_Distance);
        marker_CaloriesBurned = findViewById(R.id.marker_CaloriesBurned);
    }
}
