package com.YBDev.runlikethewind.database;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "running_table")
public class Run {

    @PrimaryKey(autoGenerate = true)
    int id;

    Bitmap bitmap = null;

    long startTimeInMilliseconds;

    long totalTimeInMilliseconds;

    float avgSpeedInKMH;

    int distanceInMeters;

    int caloriesBurned;

    public Run() {
    }

}
