package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class TestCategory extends DB_Record {
    public final static String TABLE_NAME = "test_categories";

    public int id; // PK
    public String name;

    public final static String ID_COLUMN = "id";
    public final static String NAME_COLUMN = "name";

    public TestCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TestCategory() {
    }

    public TestCategory(int id) {
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
        contentValues.put(NAME_COLUMN, name);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            id = (int) map.get(ID_COLUMN);
            name = (String) map.get(NAME_COLUMN);
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
        return "CREATE TABLE `test_categories` (\n" +
                "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "`name` TEXT NOT NULL\n" +
                ");";
    }
}
