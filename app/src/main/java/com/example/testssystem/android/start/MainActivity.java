package com.example.testssystem.android.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testssystem.R;
import com.example.testssystem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static int userId = -1;
    public static String userName = null;
    public static MainActivity activity;
    private static final NavOptions navOptions = new NavOptions.Builder()
            .setEnterAnim(android.R.animator.fade_in)
            .setPopEnterAnim(android.R.animator.fade_in)
            .setExitAnim(android.R.animator.fade_out)
            .setPopExitAnim(android.R.animator.fade_out)
            .build();

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.navigation_tests, R.id.navigation_profile)
                .build();

        NavController navController = getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navBar, navController);
        binding.navBar.setOnItemSelectedListener(item -> {
            getNavController().navigate(item.getItemId(), null, navOptions);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    private NavController getNavController() {
        return ((NavHostFragment) binding.navHost.getFragment()).getNavController();
    }

    public void navigateTo(int id) {
        getNavController().navigate(id, null, navOptions);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getNavController().navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        userId = -1;
        userName = null;
        SharedPreferences preferences = getSharedPreferences("login_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("login");
        editor.remove("password");
        editor.apply();

        Intent i = new Intent(this, AuthActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
