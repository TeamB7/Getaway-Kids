package com.example.a5120app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * The MapsFragment program implements an class that
 * show the map display nearby facilities for user to
 * do exercises outdoor by retrieving data from google
 * map api
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private final static int TAG_CODE_PERMISSION_LOCATION = 0;
    //    private Button searchFacilitiesBtn, btnSecurity;
//    RadioButton rbParks, rbGym;
    private String recreationType = "", searchString = "", facilities = "";
    //    private EditText edSearch;
//    private Spinner spFacilityType;
    private TextView mapTitle;
    private double latitude;
    private double longitude;
    private MapView mapView;
    private LocationManager locationManager;
    //    Button searchPlaceBtn;
//    private RadioGroup radioGroup;
    private GoogleMap gMap;
    private UiSettings mUiSettings;
    private View view;
    private Address localAddress;

    /**
     * initialize the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
//        searchFacilitiesBtn = view.findViewById(R.id.search_facilities);
//        searchPlaceBtn = view.findViewById(R.id.search_place);
//        edSearch = view.findViewById(R.id.ed_search);
//        spFacilityType = view.findViewById(R.id.sp_facility_type);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MainActivity", MODE_PRIVATE);

        mapTitle = view.findViewById(R.id.tv_map_title);
        Bundle args = this.getArguments();
        if (args != null) {
            searchString = args.getString("Address", "");
            recreationType = args.getString("Amenity", "");
        }

        if (!searchString.equals("")) {
            String title = "Location Selected: " + searchString + "\n" + "Activity Selected: " + recreationType;
            mapTitle.setText(title);
        }

//        radioGroup = (RadioGroup) view.findViewById(R.id.relLayout2);
//        rbParks = (RadioButton) view.findViewById(R.id.radio_parks);
//        rbGym = (RadioButton) view.findViewById(R.id.radio_gyms);
//
//        btnSecurity = (Button) view.findViewById(R.id.btn_Security);
//        btnSecurity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment fragment = null;
//                fragment = new MainFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.content_frame, fragment);
//                fragmentTransaction.commit();
//                DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.main_layout);
//            }
//
//        });
//
//        List<String> types = new ArrayList<String>();
//        types.add("Recreation types");
//        types.add("Aerobics");
//        types.add("Boxing");
//        types.add("Calisthenics");
//        types.add("Cycling");
//        types.add("Fitness");
//        types.add("Swimming");
//        types.add("Dancing");
//        types.add("Orienteering");
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spFacilityType.setAdapter(adapter);
//        spFacilityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!spFacilityType.getSelectedItem().toString().equals("Recreation types")) {
//                    recreationType = spFacilityType.getSelectedItem().toString();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

//        searchPlaceBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                geoLocate();
//            }
//        });
//
//        searchFacilitiesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                geoLocate();
//                String facilitiesType = spFacilityType.getSelectedItem().toString();
//                if (!facilitiesType.isEmpty()) {
//                    facilities = facilitiesType;
//                }
//
//                gMap.clear();
//                String url = getUrl(latitude, longitude, facilities);
//                Object[] dataTransfer = new Object[2];
//                dataTransfer[0] = gMap;
//                dataTransfer[1] = url;
//                GetNearbyPlaces getNearbyPlacesData = new GetNearbyPlaces();
//                getNearbyPlacesData.execute(dataTransfer);
//                Toast.makeText(getContext(), "Nearby Facilities", Toast.LENGTH_LONG).show();
//            }
//        });


        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }

    /**
     * get user location and add marker to the map
     */
    private void geoLocate() {
//        String search = edSearch.getText().toString();

        List<Address> addressList = new ArrayList<>();
        if (!searchString.equals("")) {
            Geocoder geocoder = new Geocoder(getContext());

            try {
                addressList = geocoder.getFromLocationName(searchString, 1);
                Log.d("getUrl", addressList.toString());
            } catch (IOException e) {
                Log.e("GeoLocate", "Exception" + e.getMessage());
                return;
            }

        } else {
            getLocalLocation();
            if (!(localAddress == null)) {
                addressList.add(localAddress);
            }
        }

        if (addressList.size() > 0) {
//            MarkerOptions markerOptions = new MarkerOptions();
            Address address = addressList.get(0);
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

//            String placeName = address.getFeatureName();
//            String vicinity = address.getThoroughfare();
//
//            markerOptions.position(latLng);
//            markerOptions.title(placeName + " : " + vicinity);
            String title = "Location Selected: " + addressList.get(0).getLocality() + "\n" + "Activity Selected: " + recreationType;
            mapTitle.setText(title);
            String str = addressList.get(0).getLocality();
//            str += addressList.get(0).getFeatureName();
            gMap.clear();
            gMap.addMarker(new MarkerOptions().position(latLng).title(str)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

    }

    /**
     * get device location
     */
    public void getLocalLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }

        if (locationManager.isProviderEnabled(NETWORK_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            Geocoder geocoder = new Geocoder(getContext());

            try {
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                localAddress = addressList.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            locationManager.requestLocationUpdates(NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (searchString.equals("")) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        Geocoder geocoder = new Geocoder(getContext());

                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            localAddress = addressList.get(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } else if (locationManager.isProviderEnabled(GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            Geocoder geocoder = new Geocoder(getContext());

            try {
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                localAddress = addressList.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (searchString.equals("")) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Geocoder geocoder = new Geocoder(view.getContext());

                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            localAddress = addressList.get(0);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }

    /**
     * get user location and get nearby facilities
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        mUiSettings = gMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMapToolbarEnabled(true);

        gMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        geoLocate();

        String url = getUrl(latitude, longitude, facilities);
        Object[] dataTransfer = new Object[2];
        dataTransfer[0] = gMap;
        dataTransfer[1] = url;
        GetNearbyPlaces getNearbyPlacesData = new GetNearbyPlaces();
        getNearbyPlacesData.execute(dataTransfer);

    }

    /**
     * format url for google map api
     */
    private String getUrl(double latitude, double longitude, String nearbyPlace) {
//        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=park&keyword=Aerobics&key=AIzaSyD6xOdM_HrZN3jzNr38x4JAqGAPvgtqXh0
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 1500);
        if (!recreationType.equals("")) {
            recreationType = recreationType.replace(" ", "+");
            googlePlacesUrl.append("&keyword=" + recreationType);
        }
//        googlePlacesUrl.append("&name=tennis");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBYsKnizkMZZcu38CWd0hwBj6bOLom4Ko4");//AIzaSyCYZOuvnAFrPF2X_Yh4NHMBfAZRJTiM7C8


        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private void setMap() {
        if (gMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    private LatLng getLocation(String addressStr) {
        Geocoder geocoder = new Geocoder(this.getContext());
        LatLng latLng = null;
        List<Address> address;
        try {
            address = geocoder.getFromLocationName(addressStr, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return latLng;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * search nearby place and show them on map
     */
    public class GetNearbyPlaces extends AsyncTask<Object, String, String> {

        String googlePlacesData;
        GoogleMap mMap;
        String url;

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesData = downloadUrl.readUrl(url);
                Log.d("GooglePlacesReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList = dataParser.parse(result);
            ShowNearbyPlaces(nearbyPlacesList);
            Log.d("GooglePlacesReadTask", "onPostExecute Exit");
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                Log.d("onPostExecute", "Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                String open = googlePlace.get("open");
                if (open.equals("CLOSED_TEMPORARILY")) {
                    open = "Closed Now";
                } else {
                    open = "Open Now";
                }
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName);
                markerOptions.snippet(vicinity + ". " + open);
                mMap.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));//snippet(placeName).
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            }
            if (nearbyPlacesList.size() != 0) {
                Toast.makeText(getContext(), "Nearby facilities found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No matching facilities found", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * get nearby search result
     */
    public class DownloadUrl {

        public String readUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
    }

    /**
     * paring the nearby search result
     */
    public class DataParser {
        public List<HashMap<String, String>> parse(String jsonData) {
            JSONArray jsonArray = null;
            JSONObject jsonObject;

            try {
                Log.d("Places", "parse");
                jsonObject = new JSONObject((String) jsonData);
                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                Log.d("Places", "parse error");
                e.printStackTrace();
            }
            return getPlaces(jsonArray);
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
            int placesCount = jsonArray.length();
            List<HashMap<String, String>> placesList = new ArrayList<>();
            HashMap<String, String> placeMap = null;
            Log.d("Places", "getPlaces");

            for (int i = 0; i < placesCount; i++) {
                try {
                    placeMap = getPlace((JSONObject) jsonArray.get(i));
                    placesList.add(placeMap);
                    Log.d("Places", "Adding places");

                } catch (JSONException e) {
                    Log.d("Places", "Error in Adding places");
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
            HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";
            String open = "";

            Log.d("getPlace", "Entered");

            try {
                if (!googlePlaceJson.isNull("name")) {
                    placeName = googlePlaceJson.getString("name");
                }
                if (!googlePlaceJson.isNull("vicinity")) {
                    vicinity = googlePlaceJson.getString("vicinity");
                }
                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = googlePlaceJson.getString("reference");
                open = googlePlaceJson.getString("business_status");
                googlePlaceMap.put("place_name", placeName);
                googlePlaceMap.put("vicinity", vicinity);
                googlePlaceMap.put("lat", latitude);
                googlePlaceMap.put("lng", longitude);
                googlePlaceMap.put("reference", reference);
                googlePlaceMap.put("open", open);
                Log.d("getPlace", "Putting Places");
            } catch (JSONException e) {
                Log.d("getPlace", "Error");
                e.printStackTrace();
            }
            return googlePlaceMap;
        }
    }


}



