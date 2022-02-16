package com.tsaravan9.myconciergeandroid.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityRegisterBuildingToUserBinding;
import com.tsaravan9.myconciergeandroid.helpers.LocationHelper;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterBuildingToUserActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnMapReadyCallback {

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
    private User userData;
    private FirebaseAuth mAuth;
    private Building matchedBuilding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityRegisterBuildingToUserBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(this);
        this.mAuth = FirebaseAuth.getInstance();
        selectedLocation = this.locationHelper.performReverseGeocoding(getApplicationContext(), "hyderabad");
        Log.d("test", selectedLocation.toString());
        if (getIntent().getExtras() != null) {
            userData = (User) getIntent().getSerializableExtra("preUserData");
        }
        Log.d("preUserData", userData.toString());
        this.usersViewModel = UsersViewModel.getInstance(getApplication());
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
        this.binding.submitReg.setOnClickListener(this);

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
////        buildingSelected = buildingList.get(position);
////        Log.d(TAG, "inside item selected" + this.buildingSelected);
////        initiateLocationListener();
//        Toast.makeText(getApplicationContext(), buildingList.get(position), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        Toast.makeText(getApplicationContext(), "nothing", Toast.LENGTH_SHORT).show();
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
//        Log.d(TAG, "inside item selected" + this.buildingSelected);
//        Toast.makeText(getApplicationContext(), binding.autoCompleteBuildingReg.getText().toString(), Toast.LENGTH_SHORT).show();
        String buildingChoosen = binding.autoCompleteBuildingReg.getText().toString();
        for (String building : buildingList) {
            if (buildingChoosen.equals(building)) {
                buildingSelected = building;
                Log.d("testuserdata", userData.toString());
                Toast.makeText(getApplicationContext(), buildingSelected, Toast.LENGTH_SHORT).show();
                initiateLocationListener();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.submitReg: {
                    submitUserData();
                    break;
                }
            }
        }
    }

    private void submitUserData() {
        int apartNum = 0;
        Boolean validData = false;
        if (!binding.autoCompleteBuildingReg.getText().toString().isEmpty()) {
            userData.setAddress(buildingSelected);
            validData = true;
        } else {
            binding.autoCompleteBuildingReg.setError("This field cannot be empty");
        }

        if (!binding.edAptNum.getText().toString().isEmpty()) {
            userData.setApartment(binding.edAptNum.getText().toString());
            validData = true;
        } else {
            binding.edAptNum.setError("This field cannot be empty");
        }

        if (validData) {
            createAcccount(userData);
        }
    }

    private void createAcccount(User newUser) {
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPass())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                usersViewModel.addUser(newUser);
                                searchBuildingByAddress(newUser.getAddress());
                                Toast.makeText(RegisterBuildingToUserActivity.this, "Sign up Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterBuildingToUserActivity.this, MainActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                Snackbar.make(binding.llcregister, "There was a problem creating your account, Please try again later", Snackbar.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            Log.e(TAG, "onComplete: Failed to create user with email and password" + task.getException() + task.getException().getLocalizedMessage());
                            Snackbar.make(binding.llcregister, "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//    }

    private void searchBuildingByAddress(String address){
        usersViewModel.searchUserByEmail(address);
        this.usersViewModel.getUserRepository().buildingFromDB.observe(RegisterBuildingToUserActivity.this, new Observer<Building>() {
            @Override
            public void onChanged(Building building) {
                matchedBuilding = building;
                int i = matchedBuilding.getTotalResidents();
                matchedBuilding.setTotalResidents(i + 1);
                usersViewModel.updateBuilding(matchedBuilding);
            }
        });
    }
}