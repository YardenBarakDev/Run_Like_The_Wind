package com.YBDev.runlikethewind.database;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "running_table")
public class Run {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Bitmap bitmap;
    private long startTimeInMilliseconds;
    private long totalTimeInMilliseconds;
    private float avgSpeedInKMH;
    private int distanceInMeters;
    private int caloriesBurned;

    public Run() {
    }

    public Run(Bitmap bitmap, long startTimeInMilliseconds, long totalTimeInMilliseconds, float avgSpeedInKMH, int distanceInMeters, int caloriesBurned) {
        this.bitmap = bitmap;
        this.startTimeInMilliseconds = startTimeInMilliseconds;
        this.totalTimeInMilliseconds = totalTimeInMilliseconds;
        this.avgSpeedInKMH = avgSpeedInKMH;
        this.distanceInMeters = distanceInMeters;
        this.caloriesBurned = caloriesBurned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public long getStartTimeInMilliseconds() {
        return startTimeInMilliseconds;
    }

    public void setStartTimeInMilliseconds(long startTimeInMilliseconds) {
        this.startTimeInMilliseconds = startTimeInMilliseconds;
    }

    public long getTotalTimeInMilliseconds() {
        return totalTimeInMilliseconds;
    }

    public void setTotalTimeInMilliseconds(long totalTimeInMilliseconds) {
        this.totalTimeInMilliseconds = totalTimeInMilliseconds;
    }

    public float getAvgSpeedInKMH() {
        return avgSpeedInKMH;
    }

    public void setAvgSpeedInKMH(float avgSpeedInKMH) {
        this.avgSpeedInKMH = avgSpeedInKMH;
    }

    public int getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(int distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
}
