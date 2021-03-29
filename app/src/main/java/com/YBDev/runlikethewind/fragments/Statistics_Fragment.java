package com.YBDev.runlikethewind.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.util.CustomMarkerView;
import com.YBDev.runlikethewind.util.TrackingUtility;
import com.YBDev.runlikethewind.viewModels.StatisticsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class Statistics_Fragment extends Fragment {

    protected View view;
    private StatisticsViewModel statisticsViewModel;
    private MaterialTextView statistics_Fragment_LBL_TotalDistance;
    private MaterialTextView statistics_Fragment_LBL_TotalTime;
    private MaterialTextView statistics_Fragment_LBL_AverageSpeed;
    private MaterialTextView statistics_Fragment_LBL_TotalCalories;
    private BarChart statistics_Fragment_barChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_statistics, container, false);
        statisticsViewModel = ViewModelProviders.of(this).get(StatisticsViewModel.class);

        findAllViews();
        setBarChart();
        subscribeToListeners();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void subscribeToListeners() {
        statisticsViewModel.getTotalDistance().observe(getViewLifecycleOwner(), totalDistance -> {
            if (totalDistance != null)
                statistics_Fragment_LBL_TotalDistance.setText(totalDistance/1000f+"km");
        });

        statisticsViewModel.getTotalTimeInMilliseconds().observe(getViewLifecycleOwner(), totalTimeInMilliseconds -> {
            if (totalTimeInMilliseconds != null)
                statistics_Fragment_LBL_TotalTime.setText(
                        TrackingUtility.getFormattedStopWatchTime(
                                totalTimeInMilliseconds, true));
        });

        statisticsViewModel.getTotalAvgSpeed().observe(getViewLifecycleOwner(), avgSpeed -> {
            if (avgSpeed != null)
                statistics_Fragment_LBL_AverageSpeed.setText(avgSpeed + "m/h");
        });

        statisticsViewModel.getTotalCaloriesBurned().observe(getViewLifecycleOwner(), totalCalories -> {
            if (totalCalories != null)
                statistics_Fragment_LBL_TotalCalories.setText(totalCalories+"");
        });

        statisticsViewModel.getAllRunesSortedByDate().observe(getViewLifecycleOwner(), runs -> {
            if (runs != null){
                List<BarEntry> runList = new ArrayList<>();
                for (int i = 0; i < runs.size(); i++)
                    runList.add(new BarEntry(i, runs.get(i).getCaloriesBurned()));

                BarDataSet barDataSet = new BarDataSet(runList, "Calories Burned Over Time");
                barDataSet.setValueTextColor(R.color.primary_dark);
                barDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.primary_dark));

                statistics_Fragment_barChart.setMarker(new CustomMarkerView(getContext(), R.layout.marker_view, runs));
                statistics_Fragment_barChart.setData(new BarData(barDataSet));
                statistics_Fragment_barChart.invalidate();
            }
        });
    }

    private void setBarChart(){
        statistics_Fragment_barChart.getDescription().setText("Calories Burned Over Time");
        statistics_Fragment_barChart.getLegend().setEnabled(false);

        statistics_Fragment_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        statistics_Fragment_barChart.getXAxis().setDrawLabels(false);
        statistics_Fragment_barChart.getXAxis().setAxisLineColor(R.color.primary_dark);
        statistics_Fragment_barChart.getXAxis().setTextColor(R.color.primary_dark);
        statistics_Fragment_barChart.getXAxis().setDrawGridLines(false);

        statistics_Fragment_barChart.getAxisLeft().setAxisLineColor(R.color.primary_dark);
        statistics_Fragment_barChart.getAxisLeft().setTextColor(R.color.primary_dark);
        statistics_Fragment_barChart.getAxisLeft().setDrawGridLines(false);

        statistics_Fragment_barChart.getAxisRight().setAxisLineColor(R.color.primary_dark);
        statistics_Fragment_barChart.getAxisRight().setTextColor(R.color.primary_dark);
        statistics_Fragment_barChart.getAxisRight().setDrawGridLines(false);

    }
    private void findAllViews() {
        statistics_Fragment_LBL_TotalDistance = view.findViewById(R.id.statistics_Fragment_LBL_TotalDistance);
        statistics_Fragment_LBL_TotalTime = view.findViewById(R.id.statistics_Fragment_LBL_TotalTime);
        statistics_Fragment_LBL_AverageSpeed = view.findViewById(R.id.statistics_Fragment_LBL_AverageSpeed);
        statistics_Fragment_LBL_TotalCalories = view.findViewById(R.id.statistics_Fragment_LBL_TotalCalories);
        statistics_Fragment_barChart = view.findViewById(R.id.statistics_Fragment_barChart);
    }
}