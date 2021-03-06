package com.YBDev.runlikethewind.repositorys;

import androidx.lifecycle.LiveData;

import com.YBDev.runlikethewind.database.MyRoomDB;
import com.YBDev.runlikethewind.database.Run;
import com.YBDev.runlikethewind.database.RunDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RunRepository {

    private final RunDao runDao;
    private LiveData<List<Run>> allRuns;
    private static RunRepository instance;

    private RunRepository() {
        MyRoomDB database = MyRoomDB.getInstance();
        runDao = database.runDao();
        allRuns = runDao.getAllRunesSortedByDate();
    }

    public static RunRepository getInstance(){ return instance;}


    public static RunRepository init(){
        if (instance == null)
            instance = new RunRepository();
        return instance;
    }

    public RunRepository(RunDao runDao) {
        this.runDao = runDao;
    }

    public void addRun(Run run){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> runDao.insert(run));
    }

    public void deleteRun(Run run){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> runDao.delete(run));
    }

    public LiveData<List<Run>> getAllRunesSortedByDate(){
        return runDao.getAllRunesSortedByDate();
    }

    public LiveData<List<Run>> getAllRunesSortedByDistance(){
        return runDao.getAllRunesSortedByDistance();
    }

    public LiveData<List<Run>> getAllRunesSortedByTimeInMilliseconds(){
        return runDao.getAllRunesSortedByTimeInMilliseconds();
    }

    public LiveData<List<Run>> getAllRunesSortedByAverageSpeed(){
        return runDao.getAllRunesSortedByAverageSpeed();
    }

    public LiveData<List<Run>> getAllRunesSortedByCaloriesBurned(){
        return runDao.getAllRunesSortedByCaloriesBurned();
    }

    public LiveData<Float> getTotalAvgSpeed(){
        return runDao.getTotalAvgSpeed();
    }

    public LiveData<Integer> getTotalDistance(){
        return runDao.getTotalDistance();
    }

    public LiveData<Integer> getTotalCaloriesBurned(){
        return runDao.getTotalCaloriesBurned();
    }

    public LiveData<Long> getTotalTimeInMilliseconds(){
        return runDao.getTotalTimeInMilliseconds();
    }

    public LiveData<List<Run>> getAllRuns() {
        return allRuns;
    }
}
