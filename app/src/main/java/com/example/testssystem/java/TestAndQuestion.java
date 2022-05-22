package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class TestAndQuestion extends DB_Record {
    public static final String TABLE_NAME = "test_question";

    public int test_id; // PK, FK to Test.id
    public int question_id; // PK, FK to Question.id

    public final static String TEST_ID_COLUMN = "test_id";
    public final static String QUESTION_ID_COLUMN = "question_id";

    public TestAndQuestion(int test_id, int question_id) {
        this.test_id = test_id;
        this.question_id = question_id;
    }

    public TestAndQuestion() { }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    @Override
    Map<String, Integer> getPrimaryKeyColumns() {
        Map<String, Integer> map = new HashMap<>();
        map.put(TEST_ID_COLUMN, Cursor.FIELD_TYPE_INTEGER);
        map.put(QUESTION_ID_COLUMN, Cursor.FIELD_TYPE_INTEGER);
        return map;
    }

    @Override
    Map<String, Object> getPrimaryKey() {
        Map<String, Object> map = new HashMap<>();
        map.put(TEST_ID_COLUMN, test_id);
        map.put(QUESTION_ID_COLUMN, question_id);
        return map;
    }

    @Override
    ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEST_ID_COLUMN, test_id);
        contentValues.put(QUESTION_ID_COLUMN, question_id);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            test_id = (int) map.get(TEST_ID_COLUMN);
            question_id = (int) map.get(QUESTION_ID_COLUMN);
        }
        catch (NullPointerException e) {
            throw new MissingColumnException(e);
        }
        catch (ClassCastException e) {
            throw new InvalidColumnTypeException(e);
        }
    }

    @Override
    String getCreateStatement() {
        return "CREATE TABLE `test_question` (\n" +
                "  `test_id` INTEGER NOT NULL,\n" +
                "  `question_id` INTEGER NOT NULL,\n" +
                "  PRIMARY KEY (`test_id`, `question_id`),\n" +
                "  FOREIGN KEY (`test_id`)\n" +
                "    REFERENCES `tests` (`id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  FOREIGN KEY (`question_id`)\n" +
                "    REFERENCES `questions` (`id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE);";
    }
}
