package com.tsaravan9.myconciergeandroid.views;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityRegisterBuildingToUserBinding;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterBuildingToUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityRegisterBuildingToUserBinding binding;
    private UsersViewModel usersViewModel;
    private List<String> buildingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityRegisterBuildingToUserBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.usersViewModel = UsersViewModel.getInstance(getApplication());
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
        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(), android.R.layout.simple_list_item_1, buildingList);
        this.binding.spinBuildingReg.setAdapter(adapter);
        this.binding.spinBuildingReg.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}