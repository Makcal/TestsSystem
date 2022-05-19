package com.example.testssystem.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testssystem.R;
import com.example.testssystem.java.DB_Controller;
import com.example.testssystem.java.User;

public class RegActivity extends AppCompatActivity {

    EditText etLogin, etPassword, etPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        etLogin = findViewById(R.id.login);
        etPassword = findViewById(R.id.password);
        etPassword2 = findViewById(R.id.password_repeat);

        findViewById(R.id.register_button).setOnClickListener(v -> try_register());

        findViewById(R.id.auth_sign).setOnClickListener(
                v -> startActivity(new Intent(RegActivity.this, AuthActivity.class))
        );
    }

    private void try_register() {
        String login = etLogin.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();

        if (!password.equals(password2)) {
            Toast.makeText(this, R.string.passwords_not_equal, Toast.LENGTH_SHORT).show();
            return;
        }

        if (login.length() < 3) {
            Toast.makeText(this, R.string.invalid_username, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 5) {
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
            return;
        }

        DB_Controller dbController = new DB_Controller(this);
        Cursor cursor = dbController.db.query(
                User.TABLE_NAME,
                null,
                User.LOGIN_COLUMN + " = \"" + login + "\"",
                null, null, null, null
        );
        if (cursor.getCount() != 0) {
            Toast.makeText(this, R.string.existing_user, Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        register(login, password);
    }

    private void register(String login, String password) {
        User user = new User(login, password);
        DB_Controller dbController = new DB_Controller(this);
        dbController.insert(user);

        startActivity(new Intent(RegActivity.this, AuthActivity.class));
    }
}
