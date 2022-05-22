package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class OptionAnswer extends DB_Record {
    public final static String TABLE_NAME = "option_answers";

    public int id; // PK
    public String text;
    public int questionId; // FK to Question.id
    public boolean isRight;

    public final static String ID_COLUMN = "id";
    public final static String TEXT_COLUMN = "text";
    public final static String QUESTION_ID_COLUMN = "question_id";
    public final static String IS_RIGHT_COLUMN = "is_right";

    public OptionAnswer() { }

    public OptionAnswer(int id) {
        this.id = id;
    }

    public OptionAnswer(String text, int questionId, boolean isRight) {
        this.text = text;
        this.questionId = questionId;
        this.isRight = isRight;
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
        contentValues.put(TEXT_COLUMN, text);
        contentValues.put(QUESTION_ID_COLUMN, questionId);
        contentValues.put(IS_RIGHT_COLUMN, isRight);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            id = (int) map.get(ID_COLUMN);
            text = (String) map.get(TEXT_COLUMN);
            questionId = (int) map.get(QUESTION_ID_COLUMN);
            isRight = (boolean) map.get(IS_RIGHT_COLUMN);
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
        return "CREATE TABLE `option_answers` (\n" +
                "  `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  `text` TINYTEXT NOT NULL,\n" +
                "  `question_id` INTEGER NOT NULL,\n" +
                "  `is_right` TINYINT NOT NULL,\n" +
                "  CONSTRAINT `fk_question_id`\n" +
                "    FOREIGN KEY (`question_id`)\n" +
                "    REFERENCES `questions` (`id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE" +
                ");";
    }
}
