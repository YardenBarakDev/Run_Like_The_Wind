package com.YBDev.runlikethewind.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.services.TrackingService;
import com.YBDev.runlikethewind.util.Constants;
import com.YBDev.runlikethewind.util.TrackingUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

public class TrackingFragment extends Fragment {

    protected View view;
    private MaterialButton TrackingFragment_BTN_finish_run;
    private MaterialButton TrackingFragment_BTN_start_run;
    private MaterialTextView TrackingFragment_LBL_time;
    private GoogleMap googleMap;
    private MapView TrackingFragment_MapView_map;

    private ArrayList<LatLng> latLngs;
    private boolean isTracking = false;
    private long curTimeInMillis;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_tracking, container, false);

        findViews();

        //move to run fragment
        TrackingFragment_BTN_finish_run.setOnClickListener(view -> {
            sendCommandToService(Constants.KEYS.ACTION_PAUSE_SERVICE);
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_trackingFragment_to_run_Fragment);
        });
        TrackingFragment_BTN_start_run.setOnClickListener(view -> toggleRun());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TrackingFragment_MapView_map.onCreate(savedInstanceState);
        latLngs = new ArrayList<>();

        TrackingFragment_MapView_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
            }
        });
        subscribeToObservers();
    }

    private void subscribeToObservers(){
        TrackingService.isTracking.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateTracking(aBoolean);
            }
        });

        TrackingService.pathPoints.observe(getViewLifecycleOwnerLiveData().getValue(), new Observer<ArrayList<ArrayList<LatLng>>>() {
            @Override
            public void onChanged(ArrayList<ArrayList<LatLng>> arrayLists) {
                latLngs.addAll(arrayLists.get(arrayLists.size() -1));
                addLatestPolyline();
                moveCamera();
                latLngs.clear();
            }
        });

        TrackingService.timeRunInMillis.observe(getViewLifecycleOwnerLiveData().getValue(), new Observer<Long>() {
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
            Log.d("jjjj", "toggleRun: pause");
        }else{
            sendCommandToService(Constants.KEYS.ACTION_START_OR_RESUME_SERVICE);
            Log.d("jjjj", "toggleRun: resume");
        }
    }

    private void updateTracking(boolean tracking){
        isTracking = tracking;
        if (!isTracking){
            TrackingFragment_BTN_start_run.setText("resume");
        }else
            TrackingFragment_BTN_start_run.setText("pause");
    }
    private void moveCamera(){
        if (latLngs != null && latLngs.size() > 0){
            // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(latLngs.size()-1), Constants.KEYS.MAP_ZOOM));
            // googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngs.get(latLngs.size()-1)), Constants.KEYS.MAP_ZOOM);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(latLngs.size() - 1), 20f));

        }
    }
    private void addAllPolylines(){
        for (LatLng pl : latLngs){
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions .color(R.color.primary).
                    width(Constants.KEYS.POLYLINE_WIDTH).addAll(latLngs);
            googleMap.addPolyline(polylineOptions);

        }
    }

    private void addLatestPolyline(){
        if (latLngs != null && latLngs.size() > 1){
            LatLng lastPolyline = latLngs.get(latLngs.size()-1);
            LatLng secondLastPolyline = latLngs.get(latLngs.size()-2);
            //Log.d("jjjjj", "last    lat" + latLngs.get(latLngs.size()-1).latitude +  " lon" + latLngs.get(latLngs.size()-1).longitude);
            //Log.d("jjjjj", "before last    lat" + latLngs.get(latLngs.size()-2).latitude +  " lon" + latLngs.get(latLngs.size()-2).longitude);

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