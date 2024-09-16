package com.example.a5120app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;

/**
 * The LocationSelectionFragment program implements an class that
 * show the page for user to select the address for input of
 * safety map page and amenities map page
 */

public class LocationSelectionFragment extends Fragment {
    private Button myLocBtn, submitLocBtn;
    private AutoCompleteTextView suburbACText;
    private EditText suburbEd;
    private String address = "", page = "";
    private View view;

    /**
     * initialize the view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_location, container, false);
        myLocBtn = view.findViewById(R.id.use_my_loc_btn);
        submitLocBtn = view.findViewById(R.id.submit_loc_btn);
        suburbEd = view.findViewById(R.id.ed_suburb);

        view.setFocusable(false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            page = bundle.getString("page", null);
        }

        if (page.equals("safety")) {
            TextView textView = view.findViewById(R.id.textView10);
            String str = "Concerned about safety? Tell us your location to find out the suburb risk rating.";
            textView.setText(str);
        }else{
            TextView textView = view.findViewById(R.id.textView9);
            String str = "or manually enter your suburb or postcode";
            textView.setText(str);
        }

        suburbEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!suburbEd.getText().toString().isEmpty()) {
                    address = suburbEd.getText().toString();
                } else {
                   //Toast.makeText(getContext(), "Invalid Suburb", Toast.LENGTH_SHORT).show();
                }
            }
        });



        myLocBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (page.equals("amenity") || page.equals("")) {
                            Fragment fragment = null;
                            fragment = new AmenitiesListFragment();
//                            Bundle args = new Bundle();
//                            String currentAd = getContext().getSharedPreferences("Login", MODE_PRIVATE).getString("Address", "");
//                            if (!currentAd.equals("")) {
//                                currentAd += ", Victoria, Australia";
//                                args.putString("Address", currentAd);
//                                fragment.setArguments(args);
//                            }
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();
                        } else if (page.equals("safety")) {
                            Fragment fragment = null;
                            fragment = new SafetyFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();
                        }
                    }
                }
        );


        submitLocBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        address = suburbEd.getText().toString();
                        if (!address.equals("") && (page.equals("amenity") || page.equals(""))) {
                            Fragment fragment = null;
                            fragment = new AmenitiesListFragment();
                            Bundle args = new Bundle();
                            address += ", Victoria, Australia";
                            args.putString("Address", address);
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();
//                            DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.main_layout);
                        } else if (!address.equals("") && page.equals("safety")) {
                            GetSuburbAsyncTask getSuburbAsyncTask = new GetSuburbAsyncTask();
                            getSuburbAsyncTask.execute();
                        } else {
                            Toast.makeText(getContext(), "Suburb name cannot be empty", Toast.LENGTH_SHORT).show();
                            suburbEd.setText("");
                        }
                    }
                }
        );

        return view;
    }

    /**
     * check if address is valid, if valid
     * pass the data and go to safety map
     */
    private class GetSuburbAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String result = RestClient.getSuburbByAddress(address);
            if (result.equals("")){
                try {
                    result = RestClient.getSuburbByPostcode(address).replaceAll("[^a-zA-Z ]", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")) {
                Fragment fragment = null;
                fragment = new SafetyFragment();
                Bundle args = new Bundle();
                args.putString("Address", address);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
//                DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.main_layout);
            } else {
                Toast.makeText(getContext(), "Suburb name is invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
