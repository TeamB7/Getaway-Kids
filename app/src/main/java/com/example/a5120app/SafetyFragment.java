package com.example.a5120app;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;


import androidx.fragment.app.Fragment;

public class SafetyFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private MapView mapView;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private String address = "", suburbAndPostcode = "", score = "";
    private GifImageView gifImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_safety, container, false);
        gifImageView = (GifImageView) view.findViewById(R.id.loading_img);
        gifImageView.setImageResource(R.drawable.loading);
        mapView = (MapView) view.findViewById(R.id.safety_map);

        Bundle args = this.getArguments();
        if (args != null) {
            address = args.getString("Address");
        }

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);

        // Add a marker in Sydney and move the camera
        if (address.equals("")) {
            address = "Melbourne";
        }
        GetSuburbAsyncTask getSuburbAsyncTask = new GetSuburbAsyncTask();
        getSuburbAsyncTask.execute();

        GeoJsonLayer layer = null;
        try {
            layer = new GeoJsonLayer(mMap, R.raw.suburbbounds_updated, getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layer.addLayerToMap();

        for (GeoJsonFeature feature : layer.getFeatures()) {
            if (feature.hasProperty("LC_PLY_PID")) {
                //String group = feature.getProperty("TEST_EXPORT_GROUP");
                int rating = Integer.parseInt(feature.getProperty("SAFETY_EXPORT_GROUP"));

                Log.d("ADebugTag", "Value:" + feature.getProperty("SAFETY_EXPORT_GROUP"));


                if (rating == 3) {
                    GeoJsonPolygonStyle polygonStyleRed = new GeoJsonPolygonStyle();
                    polygonStyleRed.setFillColor(Color.parseColor("#80FF6680"));
                    polygonStyleRed.setStrokeWidth(3);
                    feature.setPolygonStyle(polygonStyleRed);
                } else if (rating == 2) {
                    GeoJsonPolygonStyle polygonStyleYellow = new GeoJsonPolygonStyle();
                    polygonStyleYellow.setFillColor(Color.parseColor("#80EBC427"));
                    polygonStyleYellow.setStrokeWidth(3);
                    feature.setPolygonStyle(polygonStyleYellow);
                } else if (rating == 1) {
                    GeoJsonPolygonStyle polygonStyleGreen = new GeoJsonPolygonStyle();
                    polygonStyleGreen.setFillColor(Color.parseColor("#993f960d"));
                    polygonStyleGreen.setStrokeWidth(3);

                    feature.setPolygonStyle(polygonStyleGreen);
                } else {
                    GeoJsonPolygonStyle polygonStyleGray = new GeoJsonPolygonStyle();
                    polygonStyleGray.setFillColor(Color.parseColor("#998F8F8F"));
                    polygonStyleGray.setStrokeWidth(3);
                    feature.setPolygonStyle(polygonStyleGray);
                }
            }
        }

    }

    private void geoLocate() {
//        String search = edSearch.getText().toString();

        List<Address> addressList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(getContext());
        String searchString = address + ", Victoria, Australia";
        String str = address;

        try {
            addressList = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("GeoLocate", "Exception" + e.getMessage());
            return;
        }


        if (addressList.size() > 0) {
//            MarkerOptions markerOptions = new MarkerOptions();
            Address address = addressList.get(0);
            double latitude = address.getLatitude();
            double longitude = address.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(latLng).title(str));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }

        mapView.setVisibility(View.VISIBLE);
        gifImageView.setVisibility(View.GONE);
    }

    private class GetScoreAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String score = RestClient.getSuburbScore(suburbAndPostcode);
            String indicator = RestClient.getSuburbIndicator(suburbAndPostcode);
            return score;
        }

        @Override
        protected void onPostExecute(String s) {
            score = s;
            geoLocate();
        }
    }


    private class GetSuburbAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return RestClient.getSuburbByAddress(address);
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")) {
                suburbAndPostcode = address + ", " + s;
                SafetyFragment.GetScoreAsyncTask getScoreAsyncTask = new SafetyFragment.GetScoreAsyncTask();
                getScoreAsyncTask.execute();

            } else {
                address = "";
                suburbAndPostcode = "";
                Toast.makeText(getContext(), "Invalid Address", Toast.LENGTH_SHORT).show();
            }

        }
    }
}