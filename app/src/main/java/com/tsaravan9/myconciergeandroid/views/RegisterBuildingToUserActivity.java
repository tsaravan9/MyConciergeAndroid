package com.tsaravan9.myconciergeandroid.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityRegisterBuildingToUserBinding;
import com.tsaravan9.myconciergeandroid.helpers.LocationHelper;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterBuildingToUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnMapReadyCallback {

    private final String TAG = this.getClass().getCanonicalName();
    private ActivityRegisterBuildingToUserBinding binding;
    private UsersViewModel usersViewModel;
    private List<String> buildingList = new ArrayList<>();
    private LocationHelper locationHelper;
    private Location lastLocation;
    private LatLng selectedLocation;
    private LocationCallback locationCallback;
    private GoogleMap mMap;
    private String buildingSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityRegisterBuildingToUserBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(this);
        this.usersViewModel = UsersViewModel.getInstance(getApplication());
        selectedLocation = this.locationHelper.performReverseGeocoding(getApplicationContext(), "hyderabad");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mvShowBuilingMap);
        mapFragment.getMapAsync(this);
        this.usersViewModel.allbuildingsList.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> buildings) {
                if (buildings != null) {
                    for (String bldg : buildings) {
                        Log.d("registerbuilding", "onChanged: " + bldg);
                        buildingList.add(bldg);
                    }
                }
            }
        });
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, buildingList);
//        this.binding.spinBuildingReg.setAdapter(adapter);
//        this.binding.spinBuildingReg.setOnItemSelectedListener(this);
        this.binding.autoCompleteBuildingReg.setAdapter(adapter);
        this.binding.autoCompleteBuildingReg.setOnItemClickListener(this);

        this.locationHelper.getLastLocation(this).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null) {
                    lastLocation = location;
//                    binding.tvLocationAddress.setText(lastLocation.toString());
                    selectedLocation = locationHelper.performReverseGeocoding(getApplicationContext(), buildingSelected);

                    if (selectedLocation != null) {
//                        binding.tvLocationAddress.setText(obtainedAddress.getAddressLine(0));
                    } else {
//                        binding.tvLocationAddress.setText(lastLocation.toString());
                    }
                } else {
//                    binding.tvLocationAddress.setText("Last location not obtained");
                }
            }
        });
        this.initiateLocationListener();
    }

    private void initiateLocationListener() {
        Log.d(TAG, "building Selected" + this.buildingSelected);
//        Log.d(TAG, "location Selected" + this.selectedLocation.toString());
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location loc : locationResult.getLocations()) {
                    lastLocation = loc;
                    selectedLocation = locationHelper.performReverseGeocoding(getApplicationContext(), buildingSelected);
                    if (selectedLocation != null) {
//                        binding.tvLocationAddress.setText(obtainedAddress.getAddressLine(0));
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                for (Location loc : locationResult.getLocations()) {
                                    selectedLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                                    onMapReady(mMap);
                                }
                            }
                        };

                        locationHelper.requestLocationUpdates(getApplicationContext(), locationCallback);
                    } else {
//                        binding.tvLocationAddress.setText(lastLocation.toString());
                    }

                    //use the updated location

                    //call weather API with updated location
                }
            }
        };

        this.locationHelper.requestLocationUpdates(this, locationCallback);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        buildingSelected = buildingList.get(position);
//        Log.d(TAG, "inside item selected" + this.buildingSelected);
//        initiateLocationListener();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(selectedLocation).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15.0f));

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(true);

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        buildingSelected = buildingList.get(position);
        Log.d(TAG, "inside item selected" + this.buildingSelected);
        initiateLocationListener();
    }
}