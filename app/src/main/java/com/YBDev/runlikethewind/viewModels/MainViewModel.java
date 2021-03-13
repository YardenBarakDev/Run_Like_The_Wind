package com.YBDev.runlikethewind.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.YBDev.runlikethewind.database.Run;
import com.YBDev.runlikethewind.repositorys.RunRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private RunRepository runRepository;
    private LiveData<List<Run>> runMutableLiveData;

    public MainViewModel() {
        this.runRepository = RunRepository.init();
        runMutableLiveData = runRepository.getAllRunesSortedByDate();
    }


    public void addRun(Run run){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runRepository.addRun(run);
            }
        });
    }

    public void deleteRun(Run run){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runRepository.deleteRun(run);
            }
        });
    }

    public LiveData<List<Run>> getAllRunesSortedByDate(){
        return runRepository.getAllRunesSortedByDate();
    }

    public LiveData<List<Run>> getAllRunesSortedByDistance(){
        return runRepository.getAllRunesSortedByDistance();
    }

    public LiveData<List<Run>> getAllRunesSortedByTimeInMilliseconds(){
        return runRepository.getAllRunesSortedByTimeInMilliseconds();
    }

    public LiveData<List<Run>> getAllRunesSortedByAverageSpeed(){
        return runRepository.getAllRunesSortedByAverageSpeed();
    }

    public LiveData<List<Run>> getAllRunesSortedByCaloriesBurned(){
        return runRepository.getAllRunesSortedByCaloriesBurned();
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
