package com.example.a5120app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * The DisclaimerFragment program implements an class that
 * show the page of disclaimer of the app when user start
 * the app at the first time
 */

public class DisclaimerFragment extends Activity {

    /**
     * initialize the view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_disclaimer);

        Button continueBtn = findViewById(R.id.bt_continue);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisclaimerFragment.this, CreateUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * back to start page
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DisclaimerFragment.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
