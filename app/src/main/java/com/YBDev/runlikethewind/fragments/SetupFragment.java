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

public class SetupFragment extends Fragment {

    protected View view;
    private MaterialButton SetupFragment_BTN_letsRun;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_setup, container, false);

        findViews();

        //move to run fragment
        SetupFragment_BTN_letsRun.setOnClickListener(view ->  NavHostFragment.findNavController(this).navigate(R.id.action_setupFragment_to_run_Fragment));
        return view;
    }

    private void findViews() {
        SetupFragment_BTN_letsRun = view.findViewById(R.id.SetupFragment_BTN_letsRun);

    }

}