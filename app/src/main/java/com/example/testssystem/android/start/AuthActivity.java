package com.example.testssystem.android.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testssystem.R;
import com.example.testssystem.java.DB_Controller;
import com.example.testssystem.java.User;

public class AuthActivity extends AppCompatActivity {

    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        etLogin = findViewById(R.id.login);
        etPassword = findViewById(R.id.password);

        findViewById(R.id.auth_button).setOnClickListener(v -> {
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            auth(login, password);
        });

        findViewById(R.id.register_sign).setOnClickListener(
                v -> startActivity(new Intent(AuthActivity.this, RegActivity.class))
        );

        checkAuthorization();
    }

    private void checkAuthorization() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String login = preferences.getString("login", null);
        String password = preferences.getString("password", null);
        if (login != null && password != null) {
            auth(login, password);
        }
    }

    private void auth(String login, String password) {
        DB_Controller dbController = new DB_Controller(this);
        Cursor cursor = dbController.db.query(
                User.TABLE_NAME,
                null,
                User.LOGIN_COLUMN + " = \"" + login + "\"",
                null, null, null, null
        );

        int id = -1;
        String password2;

        boolean success = cursor.moveToNext();
        if (success) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(User.ID_COLUMN));
            password2 = cursor.getString(cursor.getColumnIndexOrThrow(User.PASSWORD_COLUMN));
            if (!password.equals(password2)) {
                success = false;
            }
        }
        cursor.close();

        if (!success) {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login", login);
            editor.putString("password", password);
            editor.apply();

            MainActivity.userId = id;
            MainActivity.userName = login;

            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
