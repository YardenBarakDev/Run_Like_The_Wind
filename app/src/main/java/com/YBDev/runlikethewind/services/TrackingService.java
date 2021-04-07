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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TrackingService extends LifecycleService {

    private boolean isFirstRun = true;
    private boolean serviceKilled = false;

    FusedLocationProviderClient fusedLocationProviderClient;

    public static MutableLiveData<Boolean> isTracking = new MutableLiveData<>();
    public static MutableLiveData<ArrayList<ArrayList<LatLng>>> pathPoints = new MutableLiveData<>(); //polylines
    public static MutableLiveData<Long> timeRunInMillis = new MutableLiveData<>();
    public static MutableLiveData<Long> timeRunInSeconds = new MutableLiveData<>();

    private ArrayList<ArrayList<LatLng>> polylines;
    private ArrayList<LatLng> polyline;

    //timer variables
    private boolean isTimerEnabled = false;
    private long lapTime;
    private long timeRun;
    private long timeStarted;
    private long lastSecondTimestamp;

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
                    }else{
                        isTracking.setValue(true);
                        isTracking.postValue(true);
                        startTimer();
                    }
                    break;
                case Constants.KEYS.ACTION_PAUSE_SERVICE:
                    pauseService();
                    break;
                case Constants.KEYS.ACTION_STOP_SERVICE:
                    killService();
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
            if (isTracking.getValue() != null && isTracking.getValue()){
                for (Location location : locationResult.getLocations()){
                    addPathPoint(location);
                }
            }
        }
    };

    private void addPathPoint(Location location){
        if (location != null && isTracking.getValue() != null && isTracking.getValue()) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            polyline.add(latLng);
            pathPoints.postValue(polylines);
        }
    }


    private void startForegroundService(){
        isTracking.setValue(true);
        startTimer();
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

    private void startTimer() {
        timeStarted = System.currentTimeMillis();
        isTimerEnabled = true;
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (isTracking.getValue() != null && timeRunInMillis.getValue() != null && timeRunInSeconds.getValue() != null ){
                while (isTracking.getValue()) {
                    // time difference between now and timeStarted
                    lapTime = System.currentTimeMillis() - timeStarted;
                    // post the new lapTime
                    timeRunInMillis.postValue(timeRun + lapTime);
                    if (timeRunInMillis.getValue()>= lastSecondTimestamp + 1000L) {
                        timeRunInSeconds.postValue(timeRunInSeconds.getValue() + 1);
                        lastSecondTimestamp += 1000L;
                    }
                    try {
                        Thread.sleep(Constants.KEYS.TIMER_UPDATE_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            timeRun += lapTime;
        });
    }

    private void initValues(){
        polylines = new ArrayList<>();
        polyline = new ArrayList<>();
        polylines.add(polyline);
        isTracking.setValue(false);
        pathPoints.setValue(polylines);
        timeRunInSeconds.setValue(0L);
        timeRunInMillis.setValue(0L);
        lapTime = 0L;
        timeRun = 0L;
        timeStarted = 0L;
        lastSecondTimestamp = 0L;
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

    private void killService(){
        serviceKilled = true;
        isFirstRun = true;
        pauseService();
        initValues();
        stopForeground(true);
        stopSelf();
    }

    private void pauseService() {
        isTracking.postValue(false);
        polyline.clear();
        isTimerEnabled = false;
    }
}