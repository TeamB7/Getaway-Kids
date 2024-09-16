package com.example.a5120app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The AmenitiesListFragment program implements an class that
 * show the amenities list by recycler view for user to select
 * on screen
 */

public class AmenitiesListFragment extends Fragment {
    private View amenitiesView;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> dataSet;
    private String address = "";

    /**
     * redirect to map page and pass address and amenity data when user select amenity
     */
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            String select = dataSet.get(position);

            Fragment fragment = new MapsFragment();
            Bundle args = new Bundle();
            args.putString("Address", address);
            args.putString("Amenity", select);

            fragment.setArguments(args);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
    };

    /**
     * receive date from previous program and initialize the page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        amenitiesView = inflater.inflate(R.layout.fragment_amenities, container, false);
        recyclerView = amenitiesView.findViewById(R.id.lv_amenities);

        Bundle args = this.getArguments();
        if (args != null) {
            address = args.getString("Address");
        }

        TextView locTv = amenitiesView.findViewById(R.id.textViewloc);
        String str = "Location: " + address;
        if (address.equals("")) {
            str = "Location: Device Location";
        }
        locTv.setText(str);

        addData();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(dataSet);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);

        return amenitiesView;
    }

    /**
     * add amenities name for the recycler view page
     */
    private void addData() {
        dataSet = new ArrayList<>();
        dataSet.add("Aerobics");
        dataSet.add("Badminton");
        dataSet.add("Calisthenics");
        dataSet.add("Dancing");
        dataSet.add("Fitness");
        dataSet.add("Golf");
        dataSet.add("Open Space");
        dataSet.add("Swimming");
        dataSet.add("Tennis");
        dataSet.add("Table Tennis");
        dataSet.add("Wheelchair Sports");
    }
}
