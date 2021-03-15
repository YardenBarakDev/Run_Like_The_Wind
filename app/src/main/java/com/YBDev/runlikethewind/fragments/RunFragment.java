package com.YBDev.runlikethewind.fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.services.TrackingService;
import com.YBDev.runlikethewind.util.Constants;
import com.YBDev.runlikethewind.util.TrackingUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RunFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    protected View view;
    private FloatingActionButton RunFragment_FAB_create_new_run;
    private TrackingUtility trackingUtility;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_runs, container, false);

        trackingUtility = new TrackingUtility();
        requestPermissions();
        findViews();

        //move to run fragment
        RunFragment_FAB_create_new_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trackingUtility.hasLocationPermissions(getContext()))
                    clicked();
                else
                    requestPermissions();
            }
        });
        return view;
    }

    private void clicked(){
        NavHostFragment.findNavController(this).navigate(R.id.action_run_Fragment_to_trackingFragment);
    }





    private void requestPermissions() {
        if(trackingUtility.hasLocationPermissions(getContext())) {
            return;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    Constants.KEYS.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            );
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    Constants.KEYS.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            );
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) { }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void findViews() {
        RunFragment_FAB_create_new_run = view.findViewById(R.id.RunFragment_FAB_create_new_run);

    }
}