package com.YBDev.runlikethewind.util;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

public class TrackingUtility {

    public static boolean hasLocationPermissions(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
        } else {
            return EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            );
        }
    }

    public static String getFormattedStopWatchTime(long ms, boolean forDialog)  {
        long milliseconds = ms;
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        milliseconds -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);

        milliseconds -= TimeUnit.SECONDS.toMillis(seconds);
        milliseconds /= 10;

        String time = hours >= 10 ? ""+hours : "0"+ hours;
        time += minutes >= 10 ? ":"+minutes : ":0"+ minutes;
        time += seconds >= 10 ? ":"+seconds : ":0"+ seconds;

        if (forDialog)
            return time;
        time += milliseconds >= 10 ? ":"+milliseconds : ":0"+ milliseconds;
        return time;
    }

    public static float calculateAllPolyLinesLength(ArrayList<LatLng> latLngs){
        float distance = 0f;
        LatLng pos1;
        LatLng pos2;
        float[] result = new float[1];

        for (int i = 0; i < latLngs.size() - 2; i++) {
            pos1 = latLngs.get(i);
            pos2 = latLngs.get(i + 1);
            Location.distanceBetween(
                    pos1.latitude,
                    pos1.longitude,
                    pos2.latitude,
                    pos2.longitude,
                    result
            );
            distance += result[0];
        }
        return  distance;
    }
}
