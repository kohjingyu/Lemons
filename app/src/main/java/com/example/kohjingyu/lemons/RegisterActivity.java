package com.example.kohjingyu.lemons;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText displayNameText;
    EditText usernameText;
    EditText passwordText;
    EditText confirmPasswordText;

    UserRegisterTask registerTask;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText)findViewById(R.id.emailText);
        displayNameText = (EditText)findViewById(R.id.displayNameText);
        usernameText = (EditText)findViewById(R.id.usernameText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        confirmPasswordText = (EditText)findViewById(R.id.confirmPasswordText);

        context = this;
    }

    public void register(View view) {
        // Reset errors
        emailText.setError(null);
        displayNameText.setError(null);
        usernameText.setError(null);
        passwordText.setError(null);
        confirmPasswordText.setError(null);

        String email = emailText.getText().toString();
        String displayName = displayNameText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConfirm = confirmPasswordText.getText().toString();

        if(!password.equals(passwordConfirm)) {
            passwordText.setError("Passwords do not match!");
            return;
        }

        if(password.length() <= 4) {
            passwordText.setError("Password must at least be 5 characters long.");
            return;
        }

        // Register user on server side
        registerTask = new UserRegisterTask(email, password, username, displayName);
        registerTask.execute((Void) null);
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String username;
        private final String displayName;
        private final String password;

        UserRegisterTask(String email, String password, String username, String displayName) {
            this.email = email;
            this.password = password;
            this.username = username;
            this.displayName = displayName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject postParams = new JSONObject();
            try {
                postParams.put("username", username);
                postParams.put("name", displayName);
                postParams.put("email", email);
                postParams.put("password", password);
                String response = LoginActivity.performPostCall("http://devostrum.no-ip.info:12345/user", postParams);
                JSONObject jsonObj = new JSONObject(response);

                boolean success = (boolean)jsonObj.get("success");

                if(success) {
                    return true;
                }
                else {
                    final String errorMessage = (String)jsonObj.get("message");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return false;
                }
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
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
