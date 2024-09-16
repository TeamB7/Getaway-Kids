package com.example.a5120app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * The SuggestActivityFragment program implements an class that
 * show the indoor exercises list for user to select by list
 * view on screen
 */

public class SuggestActivityFragment extends Fragment {
    private String[] name, type;
    private ListView listView;
    private int[] imageId;

    /**
     * initialize the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indoor, container, false);

        addData();
        AdapterIndoor adapter = new AdapterIndoor(getActivity(), name, type, imageId);

        listView = (ListView) view.findViewById(R.id.exercise_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                String value = name[+position];
                Fragment fragment = null;
                fragment = new ExerciseFragment();
                Bundle args = new Bundle();
                args.putString("exercise", value);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    /**
     * add data for the list view
     */
    private void addData() {
        name = new String[]{"Bicycle Crunch", "Bounds", "Burpee", "Butt Kickers", "Deadbug",
                "Flutter Kick", "High Knees", "Jump Lunges", "Jump Rope", "Jumping",
                "Jumping Jacks", "Knee Drive", "Leg Raise", "Lying Leg Raises",
                "Plank", "Pushup", "Reverse Crunches", "Seal Jacks",
                "Side Plank", "Skaters", "Skipping",
                "Touchdown", "Mountain Climbers"};
        type = new String[]{"Strength", "Cardio, Laps", "Plyo", "Cardio", "Strength",
                "Strength", "Cardio", "Plyo, Cardio", "Cardio", "Plyo",
                "Cardio", "Plyo", "Strength", "Strength", "Strength", "Strength",
                "Strength", "Cardio", "Strength", "Cardio", "Cardio, Laps",
                "Plyo", "Cardio"};
        imageId = new int[]{R.drawable.bicycle_crunch, R.drawable.bounds, R.drawable.burpees, R.drawable.butt_kickers,
                R.drawable.deadbug, R.drawable.flutter_kick, R.drawable.high_knees, R.drawable.jump_lunges,
                R.drawable.jump_rope, R.drawable.jumping, R.drawable.jumping_jacks, R.drawable.knee_drive,
                R.drawable.leg_raise, R.drawable.lying_leg_raise, R.drawable.planks, R.drawable.push_up,
                R.drawable.reverse_crunch, R.drawable.seal_jacks, R.drawable.side_planks, R.drawable.skaters,
                R.drawable.skipping, R.drawable.touchdown, R.drawable.mountain_climbers};
    }

}
