package com.YBDev.runlikethewind.fragments;

import android.content.Intent;
import android.location.Location;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class TrackingFragment extends Fragment {

    protected View view;
    private MaterialButton TrackingFragment_BTN_finish_run;
    private MaterialButton TrackingFragment_BTN_start_run;
    private GoogleMap googleMap;
    private MapView TrackingFragment_MapView_map;

    private ArrayList<LatLng> latLngs;
    private boolean isTracking = true;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TrackingFragment_MapView_map.onCreate(savedInstanceState);
        latLngs = new ArrayList<>();
        TrackingFragment_MapView_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                addAllPolylines();
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

        TrackingService.pathPoints.observe(getViewLifecycleOwner(), new Observer<ArrayList<ArrayList<LatLng>>>() {
            @Override
            public void onChanged(ArrayList<ArrayList<LatLng>> arrayLists) {
              for (int i = 0; i < arrayLists.size(); i++){
                  for (int j = 0; j < arrayLists.get(i).size(); j++){
                      Log.d("jjjj", "pathPoints");
                      latLngs.add(arrayLists.get(i).get(j));
                      addLatestPolyline();
                      moveCamera();
                  }
              }
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

    private void updateTracking(boolean tracking){
        isTracking = tracking;
        if (!isTracking){
            //...
            //...
            //...
        }
    }
    private void moveCamera(){
        if (latLngs != null && latLngs.size() > 1){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(latLngs.size()-1), Constants.KEYS.MAP_ZOOM));
        }
    }
    private void addAllPolylines(){
        for (LatLng pl : latLngs){
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions .color(R.color.primary).
                    width(Constants.KEYS.POLYLINE_WIDTH).addAll(latLngs);
        }
    }

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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_tracking, container, false);

        findViews();

        //move to run fragment
        TrackingFragment_BTN_finish_run.setOnClickListener(view ->  NavHostFragment.findNavController(this).navigate(R.id.action_trackingFragment_to_run_Fragment));

        TrackingFragment_BTN_start_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommandToService(Constants.KEYS.ACTION_START_OR_RESUME_SERVICE);
                //toggleRun();
            }
        });
        return view;
    }

    private void sendCommandToService(String command){
        Intent intent = new Intent(getActivity(), TrackingService.class);
        intent.setAction(command);
        getActivity().startService(intent);
    }


    private void findViews() {
        TrackingFragment_BTN_finish_run = view.findViewById(R.id.TrackingFragment_BTN_finish_run);
        TrackingFragment_BTN_start_run = view.findViewById(R.id.TrackingFragment_BTN_start_run);
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


    //@Override
    //public void onDestroy() {
    //    super.onDestroy();
    //    if (TrackingFragment_MapView_map != null)
    //        TrackingFragment_MapView_map.onDestroy();
    //}

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