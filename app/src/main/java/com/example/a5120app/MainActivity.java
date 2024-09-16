package com.example.a5120app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import android.support.design.widget.NavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

/**
 * The MainActivity program implements an class that
 * handle navigation and redirect of multiple screens
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomPopup.BottomSheetListener {
    private static String userStr = "";
    private static String userName = "";
    private BottomNavigationView navigationView;

    /**
     * initialize the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences exercisesp = getSharedPreferences("Exercise", MODE_PRIVATE);
//        sp.edit().clear().commit();
//        exercisesp.edit().clear().commit();
//        sp.edit().remove("FirstName").commit();
        String firstName = sp.getString("FirstName", null);

//        String lastName = sp.getString("LastName", null);
        if (firstName == null || firstName == "") {

            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        } else {
            userName = firstName;
        }


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        RelativeLayout navLayout = (RelativeLayout) findViewById(R.id.main_layout);

        navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                Bundle args = new Bundle();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new App_Home();
                        break;
                    case R.id.nav_indoor:
                        fragment = new SuggestActivityFragment();
                        break;
                    case R.id.nav_outdoor:
                        fragment = new LocationSelectionFragment();
                        args.putString("page", "amenity");
                        fragment.setArguments(args);
                        break;
                    case R.id.nav_security:
                        fragment = new LocationSelectionFragment();
                        args.putString("page", "safety");
                        fragment.setArguments(args);
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                return true;
            }
        });

//        getSupportActionBar().setTitle("On Your Toes");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new App_Home());
        fragmentTransaction.commit();

    }

    /**
     * back button redirection
     */
    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment instanceof App_Home)
            super.onBackPressed();
//        else if (currentFragment instanceof LocationSelectionFragment) {
//            navigationView.getMenu().getItem(3).setChecked(true);
//            navigationView.getMenu().getItem(1).setChecked(false);
//            navigationView.getMenu().getItem(2).setChecked(false);
//            navigationView.getMenu().getItem(0).setChecked(false);
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, new App_Home());
//            ft.commit();
//        }
        else if (currentFragment instanceof AmenitiesListFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.content_frame, new LocationSelectionFragment());
            ft.commit();
        } else if (currentFragment instanceof SafetyFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment newFragment = new LocationSelectionFragment();
            Bundle args = new Bundle();
            args.putString("page", "safety");
            newFragment.setArguments(args);
            ft.replace(R.id.content_frame, newFragment);
            ft.commit();
        } else if (currentFragment instanceof MapsFragment) {
            Bundle args = currentFragment.getArguments();
            Fragment newFragment = new AmenitiesListFragment();
            newFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, newFragment);
            ft.commit();
        } else if (currentFragment instanceof ExerciseFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new SuggestActivityFragment());
            ft.commit();
        } else {
            navigationView.getMenu().getItem(3).setChecked(true);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            navigationView.getMenu().getItem(0).setChecked(false);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new App_Home());
            ft.commit();
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * bottom navigation
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new App_Home();
        } else if (id == R.id.nav_indoor) {
            fragment = new SuggestActivityFragment();
        } else if (id == R.id.nav_outdoor) {
            fragment = new LocationSelectionFragment();
            Bundle args = new Bundle();
            args.putString("page", "amenity");
            fragment.setArguments(args);
        } else if (id == R.id.nav_security) {
            fragment = new LocationSelectionFragment();
            Bundle args = new Bundle();
            args.putString("page", "safety");
            fragment.setArguments(args);
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
