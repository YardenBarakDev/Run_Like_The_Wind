package com.YBDev.runlikethewind.fragments;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.adapters.RunsAdapter;
import com.YBDev.runlikethewind.database.Run;
import com.YBDev.runlikethewind.util.Constants;
import com.YBDev.runlikethewind.util.MySP;
import com.YBDev.runlikethewind.util.SortType;
import com.YBDev.runlikethewind.util.TrackingUtility;
import com.YBDev.runlikethewind.viewModels.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RunFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    protected View view;
    private RunsAdapter runsAdapter;
    private MainViewModel mainViewModel;
    private RecyclerView RunFragment_RecyclerView;
    private Spinner RunFragment_Spinner;
    private FloatingActionButton RunFragment_FAB_create_new_run;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_runs, container, false);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        requestPermissions();
        findViews();
        getRunArrayFromDB();
        clickListeners();
        return view;
    }

    private void clickListeners() {
        RunFragment_FAB_create_new_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MySP.getInstance().getFloat(Constants.KEYS.USER_WEIGHT, -1) != -1)
                {
                    if (TrackingUtility.hasLocationPermissions(getContext()))
                        clicked();
                    else
                        requestPermissions();
                }else
                    Toast.makeText(getContext(), "Please fill your weight and name", Toast.LENGTH_SHORT).show();
            }
        });

        RunFragment_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                switch (index){
                    case 0:
                        mainViewModel.sortRuns(SortType.DATE);
                        break;
                    case 1:
                        mainViewModel.sortRuns(SortType.RUNNING_TIME);
                        break;
                    case 2:
                        mainViewModel.sortRuns(SortType.DISTANCE);
                        break;
                    case 3:
                        mainViewModel.sortRuns(SortType.AVG_SPEED);
                        break;
                    case 4:
                        mainViewModel.sortRuns(SortType.CALORIES_BURNED);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mainViewModel.sortRuns(SortType.DATE);
            }
        });
    }

    private void clicked(){
        NavHostFragment.findNavController(this).navigate(R.id.action_run_Fragment_to_trackingFragment);
    }

    private void getRunArrayFromDB(){

        mainViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<List<Run>>() {
            @Override
            public void onChanged(List<Run> runs) {
                if (runs != null) {
                    Log.d("jjjj", "runs observed");
                    setRecyclerViewAdapter(runs);
                }
            }
        });
    }
    private void setRecyclerViewAdapter(List<Run> runs) {
        runsAdapter = new RunsAdapter(getContext(), runs);
        RunFragment_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RunFragment_RecyclerView.setAdapter(runsAdapter);
    }



    private void requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(getContext())) {
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
        RunFragment_RecyclerView = view.findViewById(R.id.RunFragment_RecyclerView);
        RunFragment_Spinner = view.findViewById(R.id.RunFragment_Spinner);
    }
}