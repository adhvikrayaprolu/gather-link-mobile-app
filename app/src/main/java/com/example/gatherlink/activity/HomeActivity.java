package com.example.gatherlink.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.gatherlink.R;
import com.example.gatherlink.fragment.HomeFragment;
import com.example.gatherlink.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeFrontEndElements();
        openFragment(new HomeFragment());
        setupOnClickListeners();
    }

    private void initializeFrontEndElements() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
    private void setupOnClickListeners() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            return openFragment(selectedFragment);
        });
    }

    private boolean openFragment(Fragment selectedFragment) {
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
            return true;
        }
        return false;
    }
}
