package com.example.testssystem.app;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            TestInfo testInfo = new TestInfo(test);

            TextView testNameView = convertView.findViewById(R.id.test_name);
            TextView testCategoryView = convertView.findViewById(R.id.test_category);
            TextView testSubsectionView = convertView.findViewById(R.id.test_subsection);

            testNameView.setText(test.name);
            testCategoryView.setText("Категория: ");
            SpannableStringBuilder category = new SpannableStringBuilder(testInfo.categoryName);
            category.setSpan();

            if (testInfo.isPassed) {
                convertView.setBackgroundColor(getResources().getColor(R.color.passed_test_color, null));
            }
            else {
                convertView.setBackgroundResource(0); // Reset
            }

            return convertView;
        }

        private class TestInfo {
            boolean isPassed;
            String subsectionName;
            String categoryName;

            TestInfo(Test test) {
                DB_Controller db_controller = new DB_Controller(requireContext());
                Cursor cursor = db_controller.db.query(
                        TestsHistory.TABLE_NAME,
                        new String[] {"COUNT(*)"},
                        TestsHistory.TEST_ID_COLUMN + " = ? AND " +
                                TestsHistory.USER_ID_COLUMN + " =  ?",
                        new String[] { Integer.toString(test.id), Integer.toString(MainActivity.userId) },
                        null, null, null
                );

                boolean value = cursor.moveToNext() && cursor.getInt(0) != 0;

                cursor.close();
            }
        }
    }
}
