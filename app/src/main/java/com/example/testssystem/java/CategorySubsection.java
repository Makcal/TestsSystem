package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class CategorySubsection extends DB_Record {
    public final static String TABLE_NAME = "category_subsections";

    public int id; // PK
    public String name;
    public int category_id; // FK to TestCategory.id

    public final static String ID_COLUMN = "id";
    public final static String NAME_COLUMN = "name";
    public final static String CATEGORY_ID_COLUMN = "category_id";

    public CategorySubsection() { }

    public CategorySubsection(int id) {
        this.id = id;
    }

    public CategorySubsection(String name, int category_id) {
        this.name = name;
        this.category_id = category_id;
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
        contentValues.put(NAME_COLUMN, name);
        contentValues.put(CATEGORY_ID_COLUMN, category_id);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            id = (int) map.get(ID_COLUMN);
            name = (String) map.get(NAME_COLUMN);
            category_id = (int) map.get(CATEGORY_ID_COLUMN);
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
        return "CREATE TABLE `category_subsections` (\n" +
                "  `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  `name` TEXT NOT NULL,\n" +
                "  `category_id` INTEGER NOT NULL,\n" +
                "  CONSTRAINT `fk_category_subsections`\n" +
                "   FOREIGN KEY (`category_id`) REFERENCES `test_categories` (`id`)\n" +
                "   ON DELETE RESTRICT\n" +
                "   ON UPDATE CASCADE\n" +
                ");";
    }
}
