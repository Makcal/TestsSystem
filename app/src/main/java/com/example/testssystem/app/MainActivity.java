package com.example.testssystem.app;

import android.os.Bundle;

import com.example.testssystem.R;
import com.example.testssystem.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    static int userId;
    static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.navigation_tests, R.id.navigation_profile)
                .build();

        NavHostFragment fragment = binding.navHostFragmentActivityMain.getFragment();
        if (fragment != null) {
            NavController navController = fragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navBar, navController);
        }
    }
}
