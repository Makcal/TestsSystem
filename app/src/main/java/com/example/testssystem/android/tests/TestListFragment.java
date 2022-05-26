package com.example.testssystem.android.tests;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.testssystem.databinding.FragmentTestListBinding;
import com.example.testssystem.java.CategorySubsection;
import com.example.testssystem.java.DB_Controller;
import com.example.testssystem.java.Test;
import com.example.testssystem.java.TestCategory;
import com.example.testssystem.java.TestsHistory;
import com.example.testssystem.java.User;

import com.example.testssystem.R;
import com.example.testssystem.android.start.MainActivity;

public class TestListFragment extends ListFragment {

    TestsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = requireContext();
        DB_Controller dbController = new DB_Controller(context);
        List<Test> tests = dbController.selectAll(Test.class);
        adapter = new TestsAdapter(context, tests);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTestListBinding binding = FragmentTestListBinding.inflate(inflater, container, false);

        binding.list.setAdapter(adapter);

        binding.searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        return binding.getRoot();
    }

    class TestsAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        final List<Test> originalValues;
        List<Test> objects;
        TestFilter filter;

        public TestsAdapter(@NonNull Context context, @NonNull List<Test> objects) {
            this.layoutInflater = LayoutInflater.from(context);
            this.objects = originalValues = new ArrayList<>(objects);
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Test getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public boolean hasStableIds() { return true; }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_test, parent, false);
            }

            Test test = getItem(position);
            TestInfo testInfo = new TestInfo(test);

            TextView testNameView = convertView.findViewById(R.id.test_name);
            TextView testCategoryView = convertView.findViewById(R.id.test_category);
            TextView testSubsectionView = convertView.findViewById(R.id.test_subsection);
            TextView authorView = convertView.findViewById(R.id.author);

            testNameView.setText(test.name);

            testCategoryView.setText("Категория: ");
            testCategoryView.append(setSpan(
                    testInfo.categoryName,
                    new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            // TODO: click to view category
                        }
                    }
            ));

            if (testInfo.subsectionName != null) {
                testSubsectionView.setText("Тема: ");
                testSubsectionView.append(setSpan(
                        testInfo.subsectionName,
                        new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                // TODO: click to view subsection
                            }
                        }
                ));
            }
            else {
                testSubsectionView.setText("");
            }

            if (testInfo.authorName != null) {
                authorView.setText("Автор:\n");
                authorView.append(setSpan(
                        testInfo.authorName,
                        new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                // TODO: click to view subsection
                            }
                        }
                ));
            }
            else {
                authorView.setText("");
            }

            if (testInfo.isPassed) {
                convertView.setBackgroundColor(getResources().getColor(R.color.passed_test_color, null));
            }
            else {
                convertView.setBackgroundResource(0); // Reset
            }

            return convertView;
        }

        @NonNull
        private Spannable setSpan(String string, ClickableSpan onClick) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(string);
            stringBuilder.setSpan(onClick, 0, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            stringBuilder.setSpan(
                    new ForegroundColorSpan(getResources().getColor(R.color.link_color, null)),
                    0, stringBuilder.length(),
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            );
            return stringBuilder;
        }

        @NonNull
        public Filter getFilter() {
            if (filter == null) {
                filter = new TestFilter();
            }
            return filter;
        }

        private class TestInfo {
            boolean isPassed;
            String subsectionName;
            String categoryName;
            String authorName;

            TestInfo(@NonNull Test test) {
                DB_Controller dbController = new DB_Controller(requireContext());

                Cursor cursor = dbController.db.query(
                        TestsHistory.TABLE_NAME,
                        new String[] {"COUNT(*)"},
                        TestsHistory.TEST_ID_COLUMN + " = ? AND " +
                                TestsHistory.USER_ID_COLUMN + " =  ?",
                        new String[] { Integer.toString(test.id), Integer.toString(MainActivity.userId) },
                        null, null, null
                );
                isPassed = cursor.moveToNext() && cursor.getInt(0) != 0;
                cursor.close();

                if (test.subsectionId != null) {
                    CategorySubsection subsection = new CategorySubsection(test.subsectionId);
                    dbController.query(subsection);
                    subsectionName = subsection.name;

                    TestCategory category = new TestCategory(subsection.categoryId);
                    dbController.query(category);
                    categoryName = category.name;
                }
                else {
                    categoryName = "Смешанные";
                    subsectionName = null;
                }

                if (test.authorId != null) {
                    User author = new User(test.authorId);
                    dbController.query(author);
                    authorName = author.login;
                }
                else {
                    authorName = null;
                }
            }
        }

        private class TestFilter extends Filter {
            @NonNull
            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                final FilterResults results = new FilterResults();
                if (prefix == null || prefix.length() == 0) {
                    final ArrayList<Test> list;
                    list = new ArrayList<>(originalValues);
                    results.values = list;
                    results.count = list.size();
                } else {
                    final String prefixString = prefix.toString().trim().toLowerCase();
                    final ArrayList<Test> newValues = new ArrayList<>();

                    for (Test test : originalValues) {
                        final String testName = test.name.toLowerCase();
                        if (testName.startsWith(prefixString)) {
                            newValues.add(test);
                        } else {
                            final String[] words = testName.split(" ");
                            for (String word : words) {
                                if (word.startsWith(prefixString)) {
                                    newValues.add(test);
                                    break;
                                }
                            }
                        }
                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
                //noinspection unchecked
                objects = (List<Test>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
