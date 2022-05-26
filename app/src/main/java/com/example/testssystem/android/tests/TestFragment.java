package com.example.testssystem.android.tests;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.testssystem.R;
import com.example.testssystem.android.start.MainActivity;
import com.example.testssystem.databinding.FragmentTestBinding;
import com.example.testssystem.java.DB_Controller;
import com.example.testssystem.java.Question;
import com.example.testssystem.java.Test;
import com.example.testssystem.java.TestAndQuestion;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends ListFragment {
    private List<Question> questions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Test test;
        if (getArguments() == null || (test = getArguments().getParcelable("test")) == null) {
            throw new RuntimeException("No test object was passed");
        }

        MainActivity activity = MainActivity.activity;
        activity.getActionBar().setTitle(test.name);

        DB_Controller dbController = new DB_Controller(activity);
        try (Cursor cursor = dbController.db.query(
                TestAndQuestion.TABLE_NAME,
                new String[]{TestAndQuestion.QUESTION_ID_COLUMN},
                TestAndQuestion.TEST_ID_COLUMN + " = " + test.id,
                null, null, null, null
        )) {
            questions = new ArrayList<>();
            while (cursor.moveToNext()) {
                questions.add(new Question(cursor.getInt(0)));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentTestBinding binding = FragmentTestBinding.inflate(inflater, container, false);
        ViewGroup root = binding.getRoot();

        for (Question q : questions) {
            root.addView(inflateQuestionView(inflater, root, q));
        }

        return root;
    }

    private View inflateQuestionView(LayoutInflater inflater, ViewGroup root, Question question) {
        View view = inflater.inflate(R.layout.item_test_question, root, false);

        return view;
    }
}