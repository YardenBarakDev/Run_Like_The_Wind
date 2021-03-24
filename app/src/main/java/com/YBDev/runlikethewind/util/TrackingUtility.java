package com.YBDev.runlikethewind.util;

import android.Manifest;
import android.content.Context;
import android.os.Build;

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
}
