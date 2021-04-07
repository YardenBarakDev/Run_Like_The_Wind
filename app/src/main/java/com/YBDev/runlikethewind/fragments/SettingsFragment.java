package com.YBDev.runlikethewind.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.util.Constants;
import com.YBDev.runlikethewind.util.MySP;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsFragment extends Fragment {

    protected View view;
    private ImageView SettingsFragment_IMAGE_background;
    private TextInputEditText SettingsFragment_LBL_fullName;
    private TextInputEditText SettingsFragment_LBL_weight;
    private MaterialButton SettingsFragment_BTN_applyChanges;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_settings, container, false);

        findAllViews();
        updateTexts();
        uploadImage();
        SettingsFragment_BTN_applyChanges.setOnClickListener(view -> {
            if (checkInput())
                Toast.makeText(getContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Please add valid information", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    private void updateTexts() {
        MySP mySP = MySP.getInstance();
        SettingsFragment_LBL_fullName.setText(mySP.getString(Constants.KEYS.FULL_NAME, ""));
        float weight = mySP.getFloat(Constants.KEYS.USER_WEIGHT, -1);
        if (weight == -1)
            SettingsFragment_LBL_weight.setText("");
        else
            SettingsFragment_LBL_weight.setText(weight+"");
    }

    private boolean checkInput() {
        if (SettingsFragment_LBL_fullName.getText().toString() == null || SettingsFragment_LBL_weight.getText().toString() == null)
            return false;
        String fullName = SettingsFragment_LBL_fullName.getText().toString();
        String weight = SettingsFragment_LBL_weight.getText().toString();
        float weightToFloat;
        try {
            weightToFloat = Float.parseFloat(weight);
        }catch (NumberFormatException e){
            return false;
        }
        if (weightToFloat <= 0 || weightToFloat > 500 || fullName.length() < 1)
            return  false;
        saveVariables(fullName, weightToFloat);
        return true;
    }

    private void saveVariables(String fullName, Float weight) {
        MySP mySP = MySP.getInstance();
        mySP.putString(Constants.KEYS.FULL_NAME, fullName);
        mySP.putFloat(Constants.KEYS.USER_WEIGHT, weight);
    }


    private void uploadImage() {
        Glide.with(this)
                .load(R.drawable.start_page_background)
                .into(SettingsFragment_IMAGE_background);
    }

    private void findAllViews() {
        SettingsFragment_IMAGE_background = view.findViewById(R.id.SettingsFragment_IMAGE_background);
        SettingsFragment_LBL_fullName = view.findViewById(R.id.SettingsFragment_LBL_fullName);
        SettingsFragment_LBL_weight = view.findViewById(R.id.SettingsFragment_LBL_weight);
        SettingsFragment_BTN_applyChanges = view.findViewById(R.id.SettingsFragment_BTN_applyChanges);
    }
}