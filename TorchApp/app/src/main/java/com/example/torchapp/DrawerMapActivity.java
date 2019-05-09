package com.example.torchapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DrawerMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final String TAG = DrawerMapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final long LOCATION_UPDATE_INTERVAL = 5000;
    private static final long LOCATION_UPDATE_FASTEST_INTERVAL = 5000;
    private static final int MINIMUM_PICKUP_DISTANCE = 50;

    //Custom variables
    private Marker selectedMarker;
    private Marker carriedMarker;

    //Buttons and Interactibles
    private Button pickupButton;
    private Button dropButton;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_map);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Construct a LocationCallBack.

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                mLastKnownLocation = locationResult.getLastLocation();

            }


        };

        // Build the map.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Assign all the buttons
        pickupButton = findViewById(R.id.pickup_button);
        dropButton = findViewById(R.id.drop_button);
        fab = findViewById(R.id.floating_button);


        addListenersOnButtons();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
           // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();

        } else if (id == R.id.profile_profile) {
           // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else if (id == R.id.profile_achievements) {
           // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AchievementsFragment()).commit();

        } else if (id == R.id.profile_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        //Specifies the checks that are done when a user presses on an infoWindow
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {


            @Override
            public boolean onMarkerClick(Marker marker) {
                //Make the Pickup button visible and disabled
                pickupButton.setVisibility(View.VISIBLE);
                disableButton(pickupButton);
                //Checking if the user is close enough to the torch and is not carrying a torch already
                if (calculateDistance(mLastKnownLocation, marker.getPosition()) > MINIMUM_PICKUP_DISTANCE || carriedMarker != null) {
                    //The user should not be able to pickup the torch
                    //Keep the Pickup button disabled
                } else {
                    //The user should be able to pick up the torch
                    //Enable the Pickup button if the user does not have a torch already

                    selectedMarker = marker;
                    enableButton(pickupButton);
                    pickupButton.setClickable(true);

                }
                return false;
            }
        });

        //Specifies what happens when the user closes the infoWindow
        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                //Make the Pickup button vanish from the UI and disable it
                selectedMarker = null;
                pickupButton.setVisibility(View.INVISIBLE);
                disableButton(pickupButton);

            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }


    /**
     * Attaching listeners to our menu button and possibly other buttons
     */

    public void addListenersOnButtons() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Slide menu when user clicks on the FAB
                LatLng pos = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                mMap.addMarker(new MarkerOptions().position(pos)
                        .title("You placed this marker!")
                        .snippet("This marker was created by the user!")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ttorch_icon)));
                //Temporary for debugging purpose
                Log.d(TAG, "someone pressed the button!");

            }
        });


        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move the Selected Marker to the user's position
                //When the user picks up the marker make it their carriedMarker and enable the drop button
                if (selectedMarker != null && carriedMarker == null) {
                    carriedMarker = selectedMarker;
                    selectedMarker = null;
                    carriedMarker.setVisible(false);

                    disableButton(pickupButton);
                    enableButton(dropButton);
                    dropButton.setVisibility(View.VISIBLE);

                } else {
                }
            }
        });

        dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Place the marker at the user's current location set the carriedMarker to null and update the carriedMarker's position

                LatLng userPosition = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                carriedMarker.setPosition(userPosition);
                carriedMarker.setVisible(true);
                carriedMarker = null;

                disableButton(dropButton);
                dropButton.setVisibility(View.INVISIBLE);
            }
        });


    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        Log.d(TAG, "getDeviceLocation() called");

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            startLocationUpdates();
                            addTestMarkers2();

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }


    }


    private void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL);


        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }

    private void stopLocationUpdates(){
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }



    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
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


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Places 4 test markers on the map relative to the user's initial position when starting the application.
     */

    private void addTestMarkers(){

        //Add test markers relative to your initial location
        double offset = 0.0001;

        for(int i = 0; i < 4; i++){
            LatLng pos = new LatLng(mLastKnownLocation.getLatitude() + offset, mLastKnownLocation.getLongitude() + offset);


            mMap.addMarker(new MarkerOptions().position(pos)
                    .title("Marker " + i)
                    .snippet("This marker was about " + calculateDistance(mLastKnownLocation, pos) + " meters away \n from when you first launched the app!"));
            offset += offset;



        }


    }

    private void addTestMarkers2(){


        //Add test markers relative to your initial location
        double offset = 0.0001;

        for(int i = 0; i < 4; i++){
            LatLng pos = new LatLng(mLastKnownLocation.getLatitude() + offset, mLastKnownLocation.getLongitude() + offset);


            mMap.addMarker(new MarkerOptions().position(pos)
                    .title("Marker " + i)
                    .snippet("This marker was about " + calculateDistance(mLastKnownLocation, pos) + " meters away \n from when you first launched the app!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ttorch_icon)));
            //
            offset += offset;



        }


    }

    /**
     * Calculates the distance between the user's location and a marker position
     */
    private float calculateDistance(Location userLocation, LatLng markerPosition){
        Location markerLocation = new Location ("Location " + markerPosition.toString());
        markerLocation.setLatitude(markerPosition.latitude);
        markerLocation.setLongitude(markerPosition.longitude);
        return userLocation.distanceTo(markerLocation);
    }

    /**
     * Disables a button
     */
    private void disableButton(Button button){
        button.setAlpha(.5f);
        button.setBackgroundColor(Color.RED);
        button.setClickable(false);
    }

    /**
     * Enables a button
     */
    private void enableButton(Button button){
        button.setAlpha(1);
        button.setBackgroundColor(Color.GREEN);
        button.setClickable(true);
    }
}