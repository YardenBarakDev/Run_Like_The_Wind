package com.YBDev.runlikethewind.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.YBDev.runlikethewind.R;
import com.bumptech.glide.Glide;

public class StartActivity extends AppCompatActivity {

    private ImageView StartActivity_IMAGE_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewsById();
        setBackgroundImage();
    }

    private void findViewsById() {
        StartActivity_IMAGE_background = findViewById(R.id.StartActivity_IMAGE_background);
    }

    private void setBackgroundImage(){
        Glide.with(StartActivity.this)
                .load(R.drawable.start_page_background)
                .into(StartActivity_IMAGE_background);
    }
}