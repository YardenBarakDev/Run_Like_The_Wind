package com.YBDev.runlikethewind.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Run run);

    @Delete
    void delete(Run run);

    @Query("SELECT * FROM running_table ORDER BY startTimeInMilliseconds DESC")
    LiveData<List<Run>> getAllRunesSortedByDate();

    @Query("SELECT * FROM running_table ORDER BY totalTimeInMilliseconds DESC")
    LiveData<List<Run>> getAllRunesSortedByTimeInMilliseconds();

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC")
    LiveData<List<Run>> getAllRunesSortedByCaloriesBurned();

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH DESC")
    LiveData<List<Run>> getAllRunesSortedByAverageSpeed();

    @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC")
    LiveData<List<Run>> getAllRunesSortedByDistance();

    @Query("SELECT SUM(totalTimeInMilliseconds) FROM running_table")
    LiveData<Long> getTotalTimeInMilliseconds();

    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    LiveData<Integer> getTotalCaloriesBurned();

    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    LiveData<Integer> getTotalDistance();

    @Query("SELECT AVG(avgSpeedInKMH) FROM running_table")
    LiveData<Float> getTotalAvgSpeed();
}
