package com.YBDev.runlikethewind.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.YBDev.runlikethewind.R;
import com.YBDev.runlikethewind.services.TrackingService;
import com.YBDev.runlikethewind.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setUpNavigation();
        navigateToTrackingFragment(getIntent());
        NavHostFragment.findNavController(navHostFragment).addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.settings_Fragment || destination.getId() == R.id.run_Fragment || destination.getId() == R.id.statistics_Fragment)
                bottomNavigationView.setVisibility(View.VISIBLE);
            else
                bottomNavigationView.setVisibility(View.GONE);

        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        navigateToTrackingFragment(intent);
    }

    private void navigateToTrackingFragment(Intent intent){
        if (intent.getAction().equals(Constants.KEYS.ACTION_SHOW_TRACKING_FRAGMENT)){
            NavHostFragment.findNavController(navHostFragment).navigate(R.id.action_global_trackingFragment);
        }
    }

    public void setUpNavigation(){
        bottomNavigationView =findViewById(R.id.bttm_nav);
        navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, TrackingService.class);
        intent.setAction(Constants.KEYS.ACTION_STOP_SERVICE);
        stopService(intent);

    }
}