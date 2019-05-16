package com.example.torchapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;

public class DrawerMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    protected static final String TAG = DrawerMapActivity.class.getSimpleName();
    protected GoogleMap mMap;
    protected CameraPosition mCameraPosition;
    protected DrawerLayout drawer;

    // The entry point to the Fused Location Provider.
    protected FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    protected final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    protected static final int DEFAULT_ZOOM = 15;
    protected static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    protected boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    protected Location mLastKnownLocation;
    protected LocationRequest mLocationRequest;
    protected LocationCallback mLocationCallback;

    // Keys for storing activity state.
    protected static final String KEY_CAMERA_POSITION = "camera_position";
    protected static final String KEY_LOCATION = "location";
    protected static final long LOCATION_UPDATE_INTERVAL = 5000;
    protected static final long LOCATION_UPDATE_FASTEST_INTERVAL = 5000;
    protected static final int MINIMUM_PICKUP_DISTANCE = 50;

    //Custom variables
    protected Marker selectedMarker;
    protected Marker carriedMarker;
    protected Circle pickupCircle;

    //Buttons and Interactibles
    protected Button pickupButton;
    protected Button dropButton;
    protected FloatingActionButton fab;
    protected ImageView menuImg;
    NavigationView navigationView;

    //Instances
    protected UIUtils uiUtils;
    protected MapUtils mapUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Creating drawer map activity!");
        setContentView(R.layout.activity_drawer_map);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
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
                mapUtils.updateCircleLocation();

            }


        };

        // Build the map.
        navigationView.setCheckedItem(R.id.nav_home);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        uiUtils = UIUtils.getInstance(this);
        mapUtils = MapUtils.getInstance(this);

        //Assign all the buttons
        pickupButton = findViewById(R.id.pickup_button);
        dropButton = findViewById(R.id.drop_button);
        fab = findViewById(R.id.floating_button);
        menuImg = findViewById(R.id.menu_image);


        uiUtils.addListenersOnButtons();


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!drawer.isDrawerOpen(GravityCompat.START) && getSupportFragmentManager().getFragments().size() > 1){
            for(Fragment fragment: getSupportFragmentManager().getFragments()){
                if(fragment instanceof SupportMapFragment){
                    continue;
                } else if(fragment != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }

            navigationView.setCheckedItem(R.id.nav_home);
        } else {
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            for(Fragment fragment: getSupportFragmentManager().getFragments()){
                if(fragment instanceof SupportMapFragment){
                    continue;
                } else if(fragment != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        } else if (id == R.id.profile_profile) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else if (id == R.id.profile_achievements) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AchievementsFragment()).commit();

        } else if (id == R.id.profile_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

            SaveSharedPreference.getSharedPreferences(this).edit().clear().commit();
            startActivity(new Intent(this, MainActivity.class));
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Pause");
        mapUtils.stopLocationUpdates();

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume");
        mapUtils.startLocationUpdates();

    }
    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("Starting");
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
                uiUtils.disableButton(pickupButton);
                //Checking if the user is close enough to the torch and is not carrying a torch already
                if (mapUtils.calculateDistance(mLastKnownLocation, marker.getPosition()) > MINIMUM_PICKUP_DISTANCE || carriedMarker != null) {
                    //The user should not be able to pickup the torch
                    //Keep the Pickup button disabled
                } else {
                    //The user should be able to pick up the torch
                    //Enable the Pickup button if the user does not have a torch already

                    selectedMarker = marker;
                    uiUtils.enableButton(pickupButton);
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
                uiUtils.disableButton(pickupButton);

            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        mapUtils.updateLocationUI();

        // Get the current location of the device and set the position of the map.
        mapUtils.getDeviceLocation();


    }



    /**
     * Prompts the user for permission to use the device location.
     */
    public void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         *
         */

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
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
    @SuppressLint("MissingPermission")
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
                    mapUtils.updateLocationUI();
                } else {
                    mLocationPermissionGranted = true;
                    mMap.setMyLocationEnabled(false);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mLastKnownLocation = null;
                }
            }
            break;
        }


    }


    public MapUtils getMapUtils(){
        return this.mapUtils;
    }


}
