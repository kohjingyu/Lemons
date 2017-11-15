package com.example.kohjingyu.lemons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText nameText;
    EditText passwordText;
    EditText confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText)findViewById(R.id.emailText);
        nameText = (EditText)findViewById(R.id.nameText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        confirmPasswordText = (EditText)findViewById(R.id.confirmPasswordText);
    }

    public void register(View view) {
        // Reset errors
        emailText.setError(null);
        nameText.setError(null);
        passwordText.setError(null);
        confirmPasswordText.setError(null);

        String email = emailText.getText().toString();
        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConfirm = confirmPasswordText.getText().toString();

        if(!password.equals(passwordConfirm)) {
            passwordText.setError("Passwords do not match!");
            return;
        }

        // TODO: Check if email is valid
        // TODO: Check if email is already taken

        Log.i("Lemons", email);
        // TODO: Register user on server side
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        RegisterActivity.this.startActivity(intent);
    }

    public void changeImage(View view) {
        // TODO: Implement changing of display image on register for users
        Log.i("Lemons", "Changing image");
    }
}
