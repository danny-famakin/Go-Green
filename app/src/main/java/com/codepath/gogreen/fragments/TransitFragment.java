package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.codepath.gogreen.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anyazhang on 7/13/17.
 */

public class TransitFragment extends ModalFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, LocationListener {
    String vehicleType;
    //TextView distance;
    LayoutInflater inflater;
    View v;
    double[] pointValues = new double[5];
    double newPoints;
    Map<String, Double[]> transitConstants = new HashMap<String, Double[]>();
    double totalDistance;
    String[] vehicles = {"bus", "subway", "train", "bike", "walking"};


    private static final String TAG = TransitFragment.class.getSimpleName();
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private static final long INTERVAL = 1000 * 5 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 5 * 1;
    private static final float SMALLEST_DISPLACEMENT = 5;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    //Toggle button to start tracking activity
    ToggleButton track;
    //List to hold latitudes and longitudes on location change
    private ArrayList<LatLng> points = new ArrayList<>();
    private ArrayList<Float> distances;
    private TextView distance;
    private double dist;
    Polyline polyline;
    int counter;


    public static TransitFragment newInstance() {

        Bundle args = new Bundle();

        TransitFragment fragment = new TransitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //To request location updates
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        counter++;
        Double[] busConstants = {0.2976, .0077, 0., 0.};
        Double[] subwayConstants = {.7408, .0454, 0., 0.};
        Double[] trainConstants = {0.7408, .0454, 0., 0.};
        Double[] bikeConstants = {0.9590, .0856, 0., 0.};
        Double[] walkingConstants = {0.9590, 0.0843, 0., 0.};
        transitConstants.put("bus", busConstants);
        transitConstants.put("subway", subwayConstants);
        transitConstants.put("train", trainConstants);
        transitConstants.put("bike", bikeConstants);
        transitConstants.put("walking", walkingConstants);

        List<Double[]> constants = Arrays.asList(busConstants, subwayConstants, trainConstants, bikeConstants, walkingConstants);
        for (int i = 0; i < constants.size(); i++) {
            pointValues[i] = new ResourceUtils(context).sumPoints(constants.get(i));
        }

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_log_transit, null);
        //v.inflate(R.layout.activity_log_transit, null);


        Spinner spVehicle = (Spinner) v.findViewById(R.id.spVehicle);
        distance = (TextView) v.findViewById(R.id.tvDistCounter);

        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                vehicleType = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicleType = parent.getItemAtPosition(0).toString();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.transportation_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spVehicle.setAdapter(adapter);

        openModal(v);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        track = (ToggleButton) v.findViewById(R.id.btnTrack);

        track.setOnClickListener(this);
        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        //createLocationRequest();

        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addConnectionCallbacks(this)
                        .addApi(LocationServices.API)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .build();
                mGoogleApiClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //SupportMapFragment fragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment fragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (fragment != null && fragment.isResumed())
            getFragmentManager().beginTransaction().remove(fragment).commit();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }



    double newDistance;
    public void onSave() {
        Intent i = new Intent();

        if (distance != null) {
            newDistance = Double.parseDouble(distance.getText().toString());
            updateData();
            modal.dismiss();
            onDestroyView();

        }
        else {
        }
    }

    private void updateData() {
        double pointVal = Arrays.asList(vehicles).indexOf(vehicleType);
        newPoints = (pointVal * newDistance);
        updateResources("transit", vehicleType, newPoints, newDistance, transitConstants.get(vehicleType), 0);

    }



    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
            //mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
            //mLastKnownLocation.getLongitude())));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void beginTrack() {
        Location location = mLastKnownLocation;
        if (mLastKnownLocation != null) {
            mGoogleApiClient.connect();
            onLocationChanged(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        if (location != null)
            points.add(latLng);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(points.get(0).latitude, points.get(0).longitude))
                .zoom(17)
                .bearing(90)
                .tilt(40)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        redrawLine();

        LatLng start = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        LatLng end = new LatLng(latLng.latitude, latLng.longitude);
        //dist = (CalculationByDistance(start, end)) * 0.000621371;
        Location locationA = new Location("point A");
        locationA.setLatitude(start.latitude);
        locationA.setLongitude(start.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(end.latitude);
        locationB.setLongitude(end.longitude);

        dist = locationA.distanceTo(locationB) * 0.0006213171;
        Log.d("DistanceIn", String.valueOf(dist));

        totalDistance += dist;
        ((TextView) v.findViewById(R.id.tvDistCounter)).setText(String.format("%.2f", totalDistance));
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius=6371000;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);

        return Radius * c;
    }

    private void redrawLine(){
        mMap.clear();  //clears all Markers and Polylines
        PolylineOptions options = new PolylineOptions().width(25).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(points.get(0).latitude, points.get(0).longitude))); //add Marker in current position
        polyline = mMap.addPolyline(options);//add Polyline
    }
    public void disconnect(){
        mGoogleApiClient.disconnect();
    }
    public void stopTrack(){
        disconnect();
    }

    @Override
    public void onClick(View v) {
            if (track.isChecked()) {
            Toast.makeText(getContext(), "Tracking", Toast.LENGTH_SHORT).show();
            beginTrack();
        } else {
            Toast.makeText(getContext(), "Stopped Tracking", Toast.LENGTH_SHORT).show();
            stopTrack();
        }
    }
}