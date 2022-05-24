package com.example.testssystem.android.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.example.testssystem.R;
import com.example.testssystem.android.start.MainActivity;
import com.example.testssystem.databinding.FragmentProfileBinding;

public class ProfileFragment extends ListFragment {
    final static String[] sections = {"Аккаунт", "Безопасность"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.list.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, sections));

        binding.profileLetter.setText(MainActivity.userName.substring(0, 1));
        binding.username.setText(MainActivity.userName);

        return binding.getRoot();
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        MainActivity activity = MainActivity.activity;
        switch (position) {
            case 0:
                activity.navigateTo(R.id.action_navigation_profile_to_accountSettingsFragment);
                break;
            case 1:
                activity.navigateTo(R.id.action_navigation_profile_to_securitySettingsFragment);
                break;
        }
    }
}
