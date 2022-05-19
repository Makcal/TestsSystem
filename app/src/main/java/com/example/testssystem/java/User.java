package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class User extends DB_Record {
    public final static String TABLE_NAME = "users";

    public int id; // PK
    public String login;
    public String password;
    public int accountType; // FK to UserType.id

    public final static String ID_COLUMN = "id";
    public final static String LOGIN_COLUMN = "login";
    public final static String PASSWORD_COLUMN = "password";
    public final static String ACCOUNT_TYPE_COLUMN = "account_type";

    public User(int id, String login, String password, int accountType) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.accountType = accountType;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.accountType = 0;
    }

    public User() { }

    public User(int id) {
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
        contentValues.put(LOGIN_COLUMN, login);
        contentValues.put(PASSWORD_COLUMN, password);
        contentValues.put(ACCOUNT_TYPE_COLUMN, accountType);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            id = (int) map.get(ID_COLUMN);
            login = (String) map.get(LOGIN_COLUMN);
            password = (String) map.get(PASSWORD_COLUMN);
            accountType = (int) map.get(ACCOUNT_TYPE_COLUMN);
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
        return "CREATE TABLE `users` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`login` TEXT NOT NULL UNIQUE, " +
                "`password` TEXT NOT NULL, " +
                "`account_type` INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY (`account_type`) REFERENCES `user_types` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE\n" +
                ");";
    }
}
