package com.tsaravan9.myconciergeandroid.views.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBuildingsListBinding;
import com.tsaravan9.myconciergeandroid.databinding.ActivitySignupBinding;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.BuildingAdapter;

import java.util.List;

public class BuildingsListActivity extends AppCompatActivity {

    private ActivityBuildingsListBinding binding;
    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityBuildingsListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.buildings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        BuildingAdapter adapter = new BuildingAdapter();
        recyclerView.setAdapter(adapter);

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersViewModel.getAllBuildings();
        usersViewModel.allBuildings.observe(this, new Observer<List<Building>>() {
            @Override
            public void onChanged(List<Building> buildings) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                if (buildings != null){
                    adapter.setBuildings(buildings);
                }
            }
        });

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                noteViewModel.delete(adaper.getNoteAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new BuildingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Building building) {
                Intent intent = new Intent(BuildingsListActivity.this, ResidentsListActivity.class);
                //put extras
                startActivity(intent);
            }
        });
    }
}