package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class Question extends DB_Record {
    public static final String TABLE_NAME = "questions";

    public int id; // PK
    public String description;
    public String right_answer;
    public String solution = null;
    public int type; // FK to QuestionType.id

    public final static String ID_COLUMN = "id";
    public final static String DESCRIPTION_COLUMN = "description";
    public final static String RIGHT_ANSWER_COLUMN = "right_answer";
    public final static String SOLUTION_COLUMN = "solution";
    public final static String TYPE_COLUMN = "type";

    public Question(int id, String description, String right_answer, String solution, int type) {
        this.id = id;
        this.description = description;
        this.right_answer = right_answer;
        this.solution = solution;
        this.type = type;
    }

    public Question() {
    }

    public Question(int id) {
        this.id = id;
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
        contentValues.put(ID_COLUMN, id);
        contentValues.put(DESCRIPTION_COLUMN, description);
        contentValues.put(RIGHT_ANSWER_COLUMN, right_answer);
        contentValues.put(SOLUTION_COLUMN, solution);
        contentValues.put(TYPE_COLUMN, type);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            id = (int) map.get(ID_COLUMN);
            description = (String) map.get(DESCRIPTION_COLUMN);
            right_answer = (String) map.get(RIGHT_ANSWER_COLUMN);
            solution = (String) map.get(SOLUTION_COLUMN);
            type = (int) map.get(TYPE_COLUMN);
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
        return "CREATE TABLE `questions` (\n" +
                "  `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  `description` TEXT NOT NULL,\n" +
                "  `right_answer` TEXT NOT NULL,\n" +
                "  `solution` TEXT,\n" +
                "  `type` INTEGER NOT NULL,\n" +
                "  FOREIGN KEY (`type`) REFERENCES `question_types` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE\n" +
                ")";
    }
}
