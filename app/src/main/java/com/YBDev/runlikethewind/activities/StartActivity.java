package com.YBDev.runlikethewind.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import com.YBDev.runlikethewind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //bottomNavigationView.setItemIconTintList(null);

        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //NavigationUI.setupWithNavController(bottomNavigationView, navController);

        setUpNavigation();

        //NavigationUI.setupWithNavController(bottomNavigationView, Navigation.findNavController(this, R.id.nav_host_fragment));
    }
    public void setUpNavigation(){
        bottomNavigationView =findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment =       (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());
    }

}