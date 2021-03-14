package com.YBDev.runlikethewind.viewModels;

import androidx.lifecycle.LiveData;

import com.YBDev.runlikethewind.repositorys.RunRepository;

public class StatisticsViewModel {


    private RunRepository runRepository;

    public StatisticsViewModel(RunRepository runRepository) {
        this.runRepository = runRepository;
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
}
