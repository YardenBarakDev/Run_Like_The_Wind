package com.YBDev.runlikethewind.viewModels;

import com.YBDev.runlikethewind.repositorys.RunRepository;

public class StatisticsViewModel {


    private RunRepository runRepository;

    public StatisticsViewModel(RunRepository runRepository) {
        this.runRepository = runRepository;
    }
}
