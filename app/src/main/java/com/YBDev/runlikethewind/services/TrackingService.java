package com.YBDev.runlikethewind.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.activities.StartActivity;
import com.YBDev.runlikethewind.util.Constants;
import com.YBDev.runlikethewind.util.TrackingUtility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class TrackingService extends LifecycleService {

    private boolean isFirstRun = true;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static MutableLiveData<Boolean> isTracking = new MutableLiveData<>();
    public static MutableLiveData<ArrayList<ArrayList<LatLng>>> pathPoints = new MutableLiveData<>(); //polylines
    private ArrayList<ArrayList<LatLng>> polylines;
    private ArrayList<LatLng> polyline;
    //polylines = ArrayList<ArrayList<LatLng>>
    //polyline =  ArrayList<LatLng>
    @Override
    public void onCreate() {
        super.onCreate();
        initValues();
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        isTracking.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateLocationTracking(aBoolean);
            }
        });
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            switch (intent.getAction()) {
                case Constants.KEYS.ACTION_START_OR_RESUME_SERVICE:
                    if (isFirstRun) {
                        isFirstRun = false;
                        startForegroundService();
                    }
                    break;
                case Constants.KEYS.ACTION_PAUSE_SERVICE:
                    isTracking.postValue(false);
                    break;
                case Constants.KEYS.ACTION_STOP_SERVICE:
                    stopSelf();
                    Log.d("jjj", "onStartCommand:3");
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }




    @SuppressLint("MissingPermission")//checked using EasyPermissions
    private void updateLocationTracking(Boolean tracking) {
        if (tracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setFastestInterval(Constants.KEYS.FASTEST_LOCATION_INTERVAL);
                locationRequest.setInterval(Constants.KEYS.LOCATION_UPDATE_INTERVAL);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                );
            }

        }
        else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
   }

   LocationCallback locationCallback = new LocationCallback() {
       @Override
       public void onLocationResult(@NonNull LocationResult locationResult) {
           super.onLocationResult(locationResult);
           if (isTracking.getValue() != null){
               for (Location location : locationResult.getLocations()){
                   addPathPoint(location);
               }
           }
       }
   };

    private void addPathPoint(Location location){
        if (location != null){

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //pathPoints.getValue().get(pathPoints.getValue().size() - 1 ).add(latLng);
            polyline.add(latLng);
            polylines.add(polyline);
            pathPoints.postValue(polylines);


        }
    }

    private void addEmptyPolyline() {
        if (polylines != null){
            polylines.get(polylines.size()).add(new LatLng(0,0));
            pathPoints.postValue(polylines);
        }else{
            initValues();
            addEmptyPolyline();
        }
    }

    private void initValues(){
        //isTracking = new MutableLiveData<>();
        //pathPoints = new MutableLiveData<>();
        polylines = new ArrayList<>();
        polyline = new ArrayList<>();

        isTracking.postValue(true);
        pathPoints.postValue(polylines);
    }

    private void startForegroundService(){
        //addEmptyPolyline();
        isTracking.postValue(true);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, Constants.KEYS.NOTIFICATION_CHANNEL_ID).
                setAutoCancel(false).
                setOngoing(true).
                setSmallIcon(R.drawable.ic_directions_run_black_24dp).
                setContentTitle(Constants.KEYS.NOTIFICATION_APP_NAME).
                setContentText("00:00:00").
                setContentIntent(getMainActivityPendingIntent());

        startForeground(Constants.KEYS.NOTIFICATION_ID, builder.build());
    }

    private PendingIntent getMainActivityPendingIntent(){
        return PendingIntent.getActivity(
                this,
                0,
                new Intent(this, StartActivity.class).setAction(Constants.KEYS.ACTION_SHOW_TRACKING_FRAGMENT),
                PendingIntent.FLAG_UPDATE_CURRENT
                );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager){
        NotificationChannel notificationChannel = new NotificationChannel(
                Constants.KEYS.NOTIFICATION_CHANNEL_ID,
                Constants.KEYS.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW);

        notificationManager.createNotificationChannel(notificationChannel);
    }
}
