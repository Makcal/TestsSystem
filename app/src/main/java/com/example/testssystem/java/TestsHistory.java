package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class TestsHistory extends DB_Record {
    public static final String TABLE_NAME = "tests_history";

    public int id; // PK
    public int testId; // FK to Test.id
    public int userId; // FK to User.id
    public Integer time = null;

    public final static String ID_COLUMN = "id";
    public final static String TEST_ID_COLUMN = "test_id";
    public final static String USER_ID_COLUMN = "user_id";
    public final static String TIME_COLUMN = "time";

    public TestsHistory() { }

    public TestsHistory(int id) {
        this.id = id;
    }

    public TestsHistory(int testId, int userId) {
        this.testId = testId;
        this.userId = userId;
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    @Override
    Map<String, Integer> getPrimaryKeyColumns() {
        Map<String, Integer> map = new HashMap<>();
        map.put(ID_COLUMN, Cursor.FIELD_TYPE_INTEGER);
        return map;
    }

    @Override
    Map<String, Object> getPrimaryKey() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_COLUMN, id);
        return map;
    }

    @Override
    ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEST_ID_COLUMN, testId);
        contentValues.put(USER_ID_COLUMN, userId);
        contentValues.put(TIME_COLUMN, time);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            id = (int) map.get(ID_COLUMN);
            testId = (int) map.get(TEST_ID_COLUMN);
            userId = (int) map.get(USER_ID_COLUMN);
            time = (Integer) map.get(TIME_COLUMN);
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
        return "CREATE TABLE `tests_history` (\n" +
                "  `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  `test_id` INTEGER NOT NULL,\n" +
                "  `user_id` INTEGER NOT NULL,\n" +
                "  `time` DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
                "  FOREIGN KEY (`test_id`)\n" +
                "    REFERENCES `tests` (`id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  FOREIGN KEY (`user_id`)\n" +
                "    REFERENCES `users` (`id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE);";
    }
}
