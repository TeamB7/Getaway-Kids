package com.example.a5120app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The StartActivity program class shows the
 * screen when user start the app at the first
 * time
 */

public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
//        try
//        {
//            this.getSupportActionBar().hide();
//        }
//        catch (NullPointerException e){}

        Button startBtn = findViewById(R.id.bt_get_started);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, DisclaimerFragment.class);
                startActivity(intent);
                finish();
            }
        });

            }
        }
