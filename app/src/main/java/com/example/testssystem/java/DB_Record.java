package com.example.testssystem.java;

import android.content.ContentValues;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import org.intellij.lang.annotations.Language;

import java.util.Map;

public abstract class DB_Record {
    // TODO: вынести информацию о таблице в отдельный класс и составить ассоциативный массив описаний

    public DB_Record() {

    }

    abstract String getTableName();

    abstract Map<String, Integer> getPrimaryKeyColumns();

    abstract Map<String, Object> getPrimaryKey();

    abstract ContentValues toContentValues();

    abstract void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException;

    @Language("RoomSql")
    abstract String getCreateStatement();
}
