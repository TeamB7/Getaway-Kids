package com.example.a5120app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.MessageDigest;

import static android.text.TextUtils.isEmpty;

/**
 * The LoginActivity program implements an class that
 * show the page for user to login with their registered
 * account
 */

public class LoginActivity extends Activity {
    private EditText edUserName, edPassword;
    private String name = "", password = "";
    private SharedPreferences sp;

    /**
     * initialize the view
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signupLink = findViewById(R.id.signup_link);

        edUserName = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password2);

        edUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!isEmpty(edUserName.getText().toString().trim())) {
                        name = edUserName.getText().toString().trim();
                    }
                }
            }
        });

        edPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!isEmpty(edPassword.getText().toString())) {
                        password = edPassword.getText().toString();
                    }
                }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button loginBtn = findViewById(R.id.login_button);

        loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEmpty(edUserName.getText().toString()) && !isEmpty(edPassword.getText().toString())) {
                            name = edUserName.getText().toString().trim();
                            password = edPassword.getText().toString().trim();
                            String passwordHash = convertSHA(password);
                            CheckExistAsyncTask checkExistAsyncTask = new CheckExistAsyncTask();
                            checkExistAsyncTask.execute(name, passwordHash);

                        } else {
                            Toast.makeText(getApplicationContext(), "Name and password cannot be empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    /**
     * get password hash
     */
    private String convertSHA(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * check if username is existing, if existing get password hash
     * by username and validate the password, if valid go to home
     * page
     */
    private class CheckExistAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            String nameCheck = strings[0];
            String passwordCheck = strings[1];
            try {
                JSONArray passwordJson = new JSONArray(RestClient.getUserPasswordByName(nameCheck));
                String existHash = passwordJson.getJSONObject(0).getString("PASSWORD_HASH");
                if (existHash.equals(passwordCheck)) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (b) {
                sp = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("FirstName", name);
                if (sp.getString("Address", "").equals("")) {
                    Ed.putString("Address", "Melbourne");
                }
                Ed.apply();

                SharedPreferences exerciseSP = getSharedPreferences("Exercise", MODE_PRIVATE);
                exerciseSP.edit().clear().apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Name or password is invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
