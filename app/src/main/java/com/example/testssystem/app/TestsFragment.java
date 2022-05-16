package com.example.testssystem.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.testssystem.R;
import com.example.testssystem.databinding.FragmentTestsBinding;
import com.example.testssystem.java.DB_Controller;
import com.example.testssystem.java.Test;
import com.example.testssystem.java.TestsHistory;

import java.util.List;

public class TestsFragment extends ListFragment {

    private FragmentTestsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTestsBinding.inflate(inflater, container, false);

        Context context = requireContext();
        DB_Controller db_controller = new DB_Controller(context);

        TestsAdapter adapter = new TestsAdapter(context, db_controller.selectAll(Test.class));
        binding.list.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class TestsAdapter extends ArrayAdapter<Test> {

        List<Test> objects;

        public TestsAdapter(@NonNull Context context, @NonNull List<Test> objects) {
            super(context, R.layout.test_item, objects);
            this.objects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.test_item, parent, false);
            }

            Test test = getItem(position);

            DB_Controller db_controller = new DB_Controller(requireContext());
            db_controller.db.query(
                    TestsHistory.TABLE_NAME,
                    new String[] {"COUNT(*)"},
                    TestsHistory.TEST_ID_COLUMN + " = " + test.id +
                            " AND " + TestsHistory.USER_ID_COLUMN + " = " +
            );

            return convertView;
        }
    }
}
