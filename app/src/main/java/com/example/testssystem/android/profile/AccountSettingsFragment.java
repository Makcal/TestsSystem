package com.example.testssystem.android.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.testssystem.R;
import com.example.testssystem.android.start.MainActivity;
import com.example.testssystem.databinding.FragmentAccountSettingsBinding;

public class AccountSettingsFragment extends ListFragment {
    private final static String[] sections = {};

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountSettingsBinding binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);

        binding.list.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, sections));
        binding.list.addFooterView(inflater.inflate(R.layout.footer_account_settings, null, false));

        return binding.getRoot();
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        if (id == -1) {
            MainActivity.activity.logout();
        }
    }
}
