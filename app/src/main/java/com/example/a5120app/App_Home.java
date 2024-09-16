package com.example.a5120app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;

/**
 * The App_Home program implements an class that
 * show the home page of the app on screen
 */

public class App_Home extends Fragment {
    private View homeView;
    private TextView greetingTv, recentActivityTv, logoutIcon;
    private CardView amenitiesCard, safetyCard, exerciseCard;
    private GridLayout recentGrid;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private SharedPreferences sp;

    /**
     * initialize the home page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeView = inflater.inflate(R.layout.activity_app__home, container, false);
        greetingTv = homeView.findViewById(R.id.tv_greeting);
        amenitiesCard = homeView.findViewById(R.id.amenities_card);
        safetyCard = homeView.findViewById(R.id.safety_card);
        exerciseCard = homeView.findViewById(R.id.exercise_card);

        final BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String firstName = sp.getString("FirstName", null);
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MainActivity", MODE_PRIVATE);
//        String name = sharedPreferences.getString("userName", null);
        String greeting = "";
        if (firstName != null) {
            greeting = "Hi " + firstName;
        }
        greetingTv.setText(greeting);

        logoutIcon = homeView.findViewById(R.id.tv_logout);
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().remove("FirstName").apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();;
            }
        });



//        sp.edit().remove("FirstName").commit();

        String address = sp.getString("Address", "");
        TextView addressTv = homeView.findViewById(R.id.address_home_tv);
        addressTv.setText(address);

        TextView dateTV1 = homeView.findViewById(R.id.date_tv_1);
        TextView dateTV2 = homeView.findViewById(R.id.date_tv_2);
        TextView dateTV3 = homeView.findViewById(R.id.date_tv_3);
        TextView nameTV1 = homeView.findViewById(R.id.name_tv_1);
        TextView nameTV2 = homeView.findViewById(R.id.name_tv_2);
        TextView nameTV3 = homeView.findViewById(R.id.name_tv_3);

        recentActivityTv = homeView.findViewById(R.id.tv_recent_activity);
        recentActivityTv.setVisibility(View.INVISIBLE);
        linearLayout1 = homeView.findViewById(R.id.ly_ac_1);
        linearLayout1.setVisibility(View.INVISIBLE);
        linearLayout2 = homeView.findViewById(R.id.ly_ac_2);
        linearLayout2.setVisibility(View.INVISIBLE);
        linearLayout3 = homeView.findViewById(R.id.ly_ac_3);
        linearLayout3.setVisibility(View.INVISIBLE);

        SharedPreferences exerciseSP = getContext().getSharedPreferences("Exercise", MODE_PRIVATE);
//        exerciseSP.edit().clear().commit();

        int index = exerciseSP.getInt("Index", 0);

        if (index >= 1) {
            TextView nonActivyTv = homeView.findViewById(R.id.empty_activity_tv);
            nonActivyTv.setVisibility(View.GONE);
            recentActivityTv.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.VISIBLE);
            try {
                JSONArray exerciseJson = new JSONArray(exerciseSP.getString(String.valueOf(0), ""));
                String exercise = exerciseJson.getString(0) + " Sets: " + exerciseJson.getString(1) + " Reps: " + exerciseJson.getString(2);
                nameTV1.setText(exercise);
                dateTV1.setText(exerciseJson.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (index >= 2) {
            linearLayout2.setVisibility(View.VISIBLE);
            try {
                JSONArray exerciseJson = new JSONArray(exerciseSP.getString(String.valueOf(1), ""));
                String exercise = exerciseJson.getString(0) + " Sets: " + exerciseJson.getString(1) + " Reps: " + exerciseJson.getString(2);
                nameTV2.setText(exercise);
                dateTV2.setText(exerciseJson.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (index == 3) {
            linearLayout3.setVisibility(View.VISIBLE);
            try {
                JSONArray exerciseJson = new JSONArray(exerciseSP.getString(String.valueOf(2), ""));
                String exercise = exerciseJson.getString(0) + " Sets: " + exerciseJson.getString(1) + " Reps: " + exerciseJson.getString(2);
                nameTV3.setText(exercise);
                dateTV3.setText(exerciseJson.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        amenitiesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(2).setChecked(true);

                Fragment fragment = null;
                fragment = new LocationSelectionFragment();
                Bundle args = new Bundle();
                args.putString("page", "amenity");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
//                DrawerLayout drawer = (DrawerLayout) homeView.findViewById(R.id.main_layout);
            }
        });

        safetyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(3).setChecked(true);

                Fragment fragment = null;
                fragment = new LocationSelectionFragment();
                Bundle args = new Bundle();
                args.putString("page", "safety");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
//                DrawerLayout drawer = (DrawerLayout) homeView.findViewById(R.id.main_layout);
            }
        });

        exerciseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(1).setChecked(true);

                Fragment fragment = null;
                fragment = new SuggestActivityFragment();
                Bundle args = new Bundle();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
//                DrawerLayout drawer = (DrawerLayout) homeView.findViewById(R.id.main_layout);
            }
        });

        TextView changeAddressBtn = homeView.findViewById(R.id.change_address_btn);
        changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new ChangeAddressFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });

        return homeView;
    }
}
