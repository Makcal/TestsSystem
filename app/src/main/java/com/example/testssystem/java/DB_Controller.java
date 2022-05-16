package com.example.testssystem.java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DB_Controller {
    private static final String DATABASE_NAME = "testssystem.db";
    private static final int DATABASE_VERSION = 3;

    public SQLiteDatabase db;

    public DB_Controller(@NonNull Context context) {
        SQLiteOpenHelper helper = new MyOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    private String makePrimaryKeyWhereClause(DB_Record object) {
        Map<String, Object> primaryKey = object.getPrimaryKey();
        String[] conditions = new String[primaryKey.size()];
        int i = 0;

        for (Map.Entry<String, Object> pair : primaryKey.entrySet()) {
            conditions[i++] = pair.getKey() + " = " + pair.getValue();
        }

        return String.join(" and ", conditions);
    }

    private <T> T instantiateClass(Class<T> tClass) {
        try {
            return tClass.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            return null;
        }
    }

    @SuppressLint("SwitchIntDef")
    private HashMap<String, Object> readRecord(Cursor cursor) {
        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);
            Object object;
            switch (cursor.getType(i)) {
                case Cursor.FIELD_TYPE_INTEGER:
                    object = cursor.getInt(i);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    object = cursor.getDouble(i);
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    object = cursor.getString(i);
                    break;
                default:
                    object = null;
                    break;
            }
            map.put(column, object);
        }
        return map;
    }

    public void insert(DB_Record object) {
        db.insert(object.getTableName(), null, object.toContentValues());
    }

    public void update(DB_Record object) {
        db.update(
                object.getTableName(),
                object.toContentValues(),
                makePrimaryKeyWhereClause(object),
                null
        );
    }

    public void deleteAll(String table) {
        db.delete(table, null, null);
    }

    public void delete(DB_Record object) {
        db.delete(
                object.getTableName(),
                makePrimaryKeyWhereClause(object),
                null
        );
    }

    public <T extends DB_Record> boolean select(T record) {
        Cursor cursor = db.query(
                record.getTableName(),
                null,
                makePrimaryKeyWhereClause(record),
                null, null, null, null
        );

        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }

        HashMap<String, Object> values = readRecord(cursor);
        cursor.close();
        try {
            record.initFromMap(values);
        }
        catch (Exception e) {
            Log.e("DB_Controller.select", "initialization from map " + values.toString() + ": ", e);
            return false;
        }
        return true;
    }

    public <T extends DB_Record> ArrayList<T> selectAll(Class<T> tClass) {
        Cursor cursor = db.query(
                Objects.requireNonNull(instantiateClass(tClass)).getTableName(),
                null, null, null, null, null, null
        );
        ArrayList<T> list = new ArrayList<>();

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                T object = Objects.requireNonNull(instantiateClass(tClass));
                HashMap<String, Object> record = readRecord(cursor);
                try {
                    object.initFromMap(record);
                }
                catch (Exception e) {
                    Log.e("DB_Controller.selectAll", "initialization from map " + record.toString() + ": ", e);
                    continue;
                }
                list.add(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    private class MyOpenHelper extends SQLiteOpenHelper {

        ArrayList<Class<? extends DB_Record>> tables = new ArrayList<>();

        MyOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            tables.add(User.class);
            tables.add(TestCategory.class);
            tables.add(CategorySubsection.class);
            tables.add(Test.class);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (Class<? extends DB_Record> tClass : tables) {
                db.execSQL(Objects.requireNonNull(instantiateClass(tClass)).getCreateStatement());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("DATABASE UPGRADED:", oldVersion + " --> " + newVersion);

            for (Class<? extends DB_Record> tClass : tables) {
                db.execSQL(
                        "DROP TABLE IF EXISTS " +
                        Objects.requireNonNull(instantiateClass(tClass)).getTableName()
                );
            }
            onCreate(db);
        }
    }

}
