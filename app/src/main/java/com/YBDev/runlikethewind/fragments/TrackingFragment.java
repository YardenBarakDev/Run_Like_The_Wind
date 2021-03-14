package com.YBDev.runlikethewind.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.YBDev.runlikethewind.R;
import com.google.android.material.button.MaterialButton;

public class TrackingFragment extends Fragment {

    protected View view;
    private MaterialButton TrackingFragment_BTN_finish_run;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_tracking, container, false);

        findViews();

        //move to run fragment
        TrackingFragment_BTN_finish_run.setOnClickListener(view ->  NavHostFragment.findNavController(this).navigate(R.id.action_trackingFragment_to_run_Fragment));
        return view;
    }

    private void findViews() {
        TrackingFragment_BTN_finish_run = view.findViewById(R.id.TrackingFragment_BTN_finish_run);

    }


}