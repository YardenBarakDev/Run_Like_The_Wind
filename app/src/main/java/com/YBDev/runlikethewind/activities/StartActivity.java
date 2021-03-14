package com.YBDev.runlikethewind.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.View;

import com.YBDev.runlikethewind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setUpNavigation();

        NavHostFragment.findNavController(navHostFragment).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.settings_Fragment || destination.getId() == R.id.run_Fragment || destination.getId() == R.id.statistics_Fragment)
                    bottomNavigationView.setVisibility(View.VISIBLE);
                else
                    bottomNavigationView.setVisibility(View.GONE);

            }
        });


    }

    public void setUpNavigation(){
        bottomNavigationView =findViewById(R.id.bttm_nav);
        navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

    }


}