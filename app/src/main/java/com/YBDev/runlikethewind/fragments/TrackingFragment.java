package com.YBDev.runlikethewind.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.database.Run;
import com.YBDev.runlikethewind.services.TrackingService;
import com.YBDev.runlikethewind.util.Constants;
import com.YBDev.runlikethewind.util.MySP;
import com.YBDev.runlikethewind.util.TrackingUtility;
import com.YBDev.runlikethewind.viewModels.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TrackingFragment extends Fragment {

    protected View view;
    private MainViewModel mainViewModel;
    private MaterialButton TrackingFragment_BTN_finish_run;
    private MaterialButton TrackingFragment_BTN_start_run;
    private MaterialTextView TrackingFragment_LBL_time;
    private GoogleMap googleMap;
    private MapView TrackingFragment_MapView_map;
    private ArrayList<LatLng> latLngs;
    private ArrayList<LatLng> allLatLngs;
    private boolean isTracking = false;
    private boolean newRun = true;
    private long curTimeInMillis;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_tracking, container, false);

        findViews();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //move to run fragment
        TrackingFragment_BTN_finish_run.setOnClickListener(view -> {
            try {
                zoomOutToSeeAllTrack();
                saveRunInDataBase();
            }catch (IllegalStateException e){
                Toast.makeText(getContext(), "Run canceled", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_trackingFragment_to_run_Fragment);
            }
        });

        TrackingFragment_BTN_start_run.setOnClickListener(view -> toggleRun());
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TrackingFragment_MapView_map.onCreate(savedInstanceState);
        latLngs = new ArrayList<>();
        allLatLngs = new ArrayList<>();

        //init google maps
        TrackingFragment_MapView_map.getMapAsync(map -> googleMap = map);
        subscribeToObservers();
    }



    //get data from the repository and view-model
    private void subscribeToObservers(){
        TrackingService.isTracking.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateTracking(aBoolean);
            }
        });

        TrackingService.pathPoints.observe(getViewLifecycleOwner(), new Observer<ArrayList<ArrayList<LatLng>>>() {
            @Override
            public void onChanged(ArrayList<ArrayList<LatLng>> arrayLists) {
                latLngs.addAll(arrayLists.get(arrayLists.size() -1));
                addLatestPolyline();
                moveCamera();
                for (int i = 0; i < latLngs.size(); i++) {
                    if (!allLatLngs.contains(latLngs.get(i)))
                        allLatLngs.add(latLngs.get(i));
                }
                latLngs.clear();
            }
        });

        TrackingService.timeRunInMillis.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                    curTimeInMillis = aLong;
                    String formatTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, false);
                    TrackingFragment_LBL_time.setText(formatTime);
            }
        });
    }


    private void toggleRun(){
        if (isTracking){
            sendCommandToService(Constants.KEYS.ACTION_PAUSE_SERVICE);
        }else{
            sendCommandToService(Constants.KEYS.ACTION_START_OR_RESUME_SERVICE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateTracking(boolean tracking){
        if (newRun){
            TrackingFragment_BTN_start_run.setText("Start");
            newRun = false;
        }else{
            isTracking = tracking;
            if (!isTracking){
                TrackingFragment_BTN_start_run.setText("resume");
            }else
                TrackingFragment_BTN_start_run.setText("pause");
        }
    }

    //move the camera to the user location
    private void moveCamera(){
        if (latLngs != null && latLngs.size() > 0 && googleMap != null)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(latLngs.size() - 1), 20f));

    }

    //zoom out the map so I can take a screenshot of the whole track
    private void zoomOutToSeeAllTrack(){
        LatLngBounds.Builder latLngBounds = LatLngBounds.builder();
        for(LatLng latLng: allLatLngs){
            latLngBounds.include(latLng);
        }
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds.build(),
                        TrackingFragment_MapView_map.getWidth(),
                        TrackingFragment_MapView_map.getHeight(),
                        Math.round(TrackingFragment_MapView_map.getHeight() * 0.05f))// this is the padding, so the polylines won't be at the edges
        );
    }

    //save the run in RoomDB
    private void saveRunInDataBase(){
        float weight = MySP.getInstance().getFloat(Constants.KEYS.USER_WEIGHT, 100);
        googleMap.snapshot(bitmap -> {
            int distanceInMeters = Math.round(TrackingUtility.calculateAllPolyLinesLength(allLatLngs));
            //to kilometers            convert to hours
            float avgSpeed = Math.round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f;
            long dateTimestamp = Calendar.getInstance().getTimeInMillis();
            int caloriesBurned = Math.round((distanceInMeters / 1000f) * weight);
            mainViewModel.addRun(new Run(bitmap, dateTimestamp, curTimeInMillis, avgSpeed, distanceInMeters, caloriesBurned));
            endRun();
        });
    }

    private void endRun() {
        TrackingFragment_LBL_time.setText("00:00:00:00");
        sendCommandToService(Constants.KEYS.ACTION_STOP_SERVICE);
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_trackingFragment_to_run_Fragment);
    }

    //draw a line between the last 2 polylines
    private void addLatestPolyline(){
        if (latLngs != null && latLngs.size() > 1){
            LatLng lastPolyline = latLngs.get(latLngs.size()-1);
            LatLng secondLastPolyline = latLngs.get(latLngs.size()-2);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(R.color.primary).
                            width(Constants.KEYS.POLYLINE_WIDTH)
                    .add(secondLastPolyline).
                            add(lastPolyline);

            googleMap.addPolyline(polylineOptions);
        }
    }

    private void sendCommandToService(String command){
        Intent intent = new Intent(getActivity(), TrackingService.class);
        intent.setAction(command);
        getActivity().startService(intent);
    }


    private void findViews() {
        TrackingFragment_BTN_finish_run = view.findViewById(R.id.TrackingFragment_BTN_finish_run);
        TrackingFragment_BTN_start_run = view.findViewById(R.id.TrackingFragment_BTN_start_run);
        TrackingFragment_LBL_time = view.findViewById(R.id.TrackingFragment_LBL_time);
        TrackingFragment_MapView_map = view.findViewById(R.id.TrackingFragment_MapView_map);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (TrackingFragment_MapView_map != null)
            TrackingFragment_MapView_map.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (TrackingFragment_MapView_map != null)
            TrackingFragment_MapView_map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (TrackingFragment_MapView_map != null)
            TrackingFragment_MapView_map.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (TrackingFragment_MapView_map != null)
            TrackingFragment_MapView_map.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (TrackingFragment_MapView_map != null)
            TrackingFragment_MapView_map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (TrackingFragment_MapView_map != null)
            TrackingFragment_MapView_map.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        TrackingFragment_MapView_map.onSaveInstanceState(outState);
    }
}