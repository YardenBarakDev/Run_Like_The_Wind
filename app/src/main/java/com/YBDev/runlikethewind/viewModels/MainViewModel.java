package com.YBDev.runlikethewind.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.YBDev.runlikethewind.database.Run;
import com.YBDev.runlikethewind.repositorys.RunRepository;
import com.YBDev.runlikethewind.util.SortType;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final RunRepository runRepository;
    SortType sortType = SortType.DATE;

    private final LiveData<List<Run>> runsSortedByDate;
    private final LiveData<List<Run>> runsSortedByDistance;
    private final LiveData<List<Run>> runsSortedByCaloriesBurned;
    private final LiveData<List<Run>> runsSortedByTimeInMillis;
    private final LiveData<List<Run>> runsSortedByAvgSpeed;

    public MediatorLiveData<List<Run>> liveData;

    public MainViewModel() {
        this.runRepository = RunRepository.init();
        liveData = new MediatorLiveData<>();
        runsSortedByDate = runRepository.getAllRunesSortedByDate();
        runsSortedByDistance = runRepository.getAllRunesSortedByDistance();
        runsSortedByCaloriesBurned = runRepository.getAllRunesSortedByCaloriesBurned();
        runsSortedByTimeInMillis = runRepository.getAllRunesSortedByTimeInMilliseconds();
        runsSortedByAvgSpeed = runRepository.getAllRunesSortedByAverageSpeed();
        addSource();
    }

    private void addSource() {
        liveData.addSource(runsSortedByDate, runs -> {
            if (sortType == SortType.DATE)
                liveData.setValue(runs);
        });

        liveData.addSource(runsSortedByDistance, runs -> {
            if (sortType == SortType.DISTANCE)
                liveData.setValue(runs);
        });

        liveData.addSource(runsSortedByCaloriesBurned, runs -> {
            if (sortType == SortType.CALORIES_BURNED)
                liveData.setValue(runs);
        });

        liveData.addSource(runsSortedByTimeInMillis, runs -> {
            if (sortType == SortType.RUNNING_TIME)
                liveData.setValue(runs);
        });

        liveData.addSource(runsSortedByAvgSpeed, runs -> {
            if (sortType == SortType.AVG_SPEED)
                liveData.setValue(runs);
        });

    }

    public void sortRuns(SortType sortType){
        switch (sortType){
            case DATE:
                if (runsSortedByDate != null)
                    liveData.setValue(runsSortedByDate.getValue());
                break;
            case RUNNING_TIME:
                if (runsSortedByTimeInMillis != null)
                    liveData.setValue(runsSortedByTimeInMillis.getValue());
                break;
            case AVG_SPEED:
                if (runsSortedByAvgSpeed != null)
                    liveData.setValue(runsSortedByAvgSpeed.getValue());
                break;
            case DISTANCE:
                if (runsSortedByDistance != null)
                    liveData.setValue(runsSortedByDistance.getValue());
                break;
            default:
                if (runsSortedByCaloriesBurned != null)
                    liveData.setValue(runsSortedByCaloriesBurned.getValue());
        }
        this.sortType = sortType;
    }

    public MediatorLiveData<List<Run>> getLiveData() {
        return liveData;
    }

    public void addRun(Run run){
        runRepository.addRun(run);
    }

    public void deleteRun(Run run){
        new Thread(() -> runRepository.deleteRun(run));
    }
}
