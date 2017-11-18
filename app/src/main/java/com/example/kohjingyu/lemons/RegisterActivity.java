package com.example.kohjingyu.lemons;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText nameText;
    EditText passwordText;
    EditText confirmPasswordText;

    UserRegisterTask registerTask;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText)findViewById(R.id.emailText);
        nameText = (EditText)findViewById(R.id.nameText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        confirmPasswordText = (EditText)findViewById(R.id.confirmPasswordText);

        context = this;
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

        registerTask = new UserRegisterTask(email, password, name, name);
        registerTask.execute((Void) null);

        // TODO: Check if email is valid
        // TODO: Check if email is already taken

        Log.i("Lemons", email);
        // TODO: Register user on server side
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        RegisterActivity.this.startActivity(intent);
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String username;
        private final String name;
        private final String password;

        UserRegisterTask(String email, String password, String username, String name) {
            this.email = email;
            this.password = password;
            this.username = username;
            this.name = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            JSONObject postParams = new JSONObject();
            try {
                postParams.put("username", username);
                postParams.put("name", name);
                postParams.put("email", email);
                postParams.put("password", password);
                LoginActivity.performPostCall("http://devostrum.no-ip.info:12345/user", postParams);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finish();
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
            } else {
                // TODO: Display error message
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    public void changeImage(View view) {
        // TODO: Implement changing of display image on register for users
        Log.i("Lemons", "Changing image");
    }
}
