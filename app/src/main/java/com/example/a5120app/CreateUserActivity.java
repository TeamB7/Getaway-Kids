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

import org.json.JSONException;

import java.security.MessageDigest;

import static android.text.TextUtils.isEmpty;

public class CreateUserActivity extends Activity {
    private EditText edUserName, edPassword, edAddress;
    private View loginView;
    private Button createBtn;
    private String firstName = "", password = "";
    private SharedPreferences sp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        edUserName = findViewById(R.id.ed_UserFirstName);
        edPassword = findViewById(R.id.ed_password);
        edAddress = findViewById(R.id.ed_address);
        createBtn = findViewById(R.id.button);

        edUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!isEmpty(edUserName.getText().toString().trim())) {
                        firstName = edUserName.getText().toString().trim();
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

        createBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEmpty(edUserName.getText().toString()) && !isEmpty(edPassword.getText().toString()) && !isEmpty(edAddress.getText().toString())) {
                            firstName = edUserName.getText().toString().trim();
                            String address = edAddress.getText().toString().trim();
                            CheckExistAsyncTask checkExistAsyncTask = new CheckExistAsyncTask();
                            checkExistAsyncTask.execute(firstName, address);
                        } else {
                            Toast.makeText(getApplicationContext(), "Name, address and password cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        sp = getSharedPreferences("Login", MODE_PRIVATE);
        TextView loginLink = findViewById(R.id.login_link);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
//                                             intent.putExtra("user", (Parcelable) sp);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateUserActivity.this, DisclaimerFragment.class);
        startActivity(intent);
        finish();
    }

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

    private class CheckExistAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String nameCheck = strings[0];
            String addressValid = RestClient.getSuburbByAddress(strings[1]);
            String nameValid = RestClient.getUserPasswordByName(nameCheck);
            String[] result = {addressValid, nameValid};
            return result;
        }

        @Override
        protected void onPostExecute(String[] s) {
            if (s[1].equals("") && !s[0].equals("")) {
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("FirstName", firstName);
                String address = edAddress.getText().toString();
                Ed.putString("Address", address);
                Ed.commit();

                PostUserAsynctack postUserAsynctack = new PostUserAsynctack();
                postUserAsynctack.execute();
                Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if(!s[1].equals("")) {
                Toast.makeText(getApplicationContext(), "Name is existing.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Invalid address, please enter Suburb name in Victoria.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class PostUserAsynctack extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                RestClient.postUser(firstName, convertSHA(password));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}



