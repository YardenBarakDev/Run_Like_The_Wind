package com.YBDev.runlikethewind.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.YBDev.runlikethewind.database.Run;
import com.YBDev.runlikethewind.repositorys.RunRepository;

import java.util.List;

public class StatisticsViewModel extends ViewModel {


    private final RunRepository runRepository;

    public StatisticsViewModel() {
        this.runRepository = RunRepository.init();
    }


    public LiveData<Float> getTotalAvgSpeed(){
        return runRepository.getTotalAvgSpeed();
    }

    public LiveData<Integer> getTotalDistance(){
        return runRepository.getTotalDistance();
    }

    public LiveData<Integer> getTotalCaloriesBurned(){
        return runRepository.getTotalCaloriesBurned();
    }

    public LiveData<Long> getTotalTimeInMilliseconds(){
        return runRepository.getTotalTimeInMilliseconds();
    }

    public  LiveData<List<Run>> getAllRunesSortedByDate(){
        return runRepository.getAllRunesSortedByDate();
    }
}
