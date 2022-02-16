package com.tsaravan9.myconciergeandroid.views.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBuildingsListBinding;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.BuildingAdapter;
import com.tsaravan9.myconciergeandroid.views.GlobalChatActivity;

import java.util.List;

public class BuildingsListActivity extends AppCompatActivity {

    private ActivityBuildingsListBinding binding;
    private UsersViewModel usersViewModel;
    private UsersDBRepository usersDBRepository;
    private BuildingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityBuildingsListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.buildings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new BuildingAdapter();
        recyclerView.setAdapter(adapter);

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersDBRepository = usersViewModel.getUserRepository();
        usersViewModel.getAllBuildings();
        usersViewModel.allBuildings.observe(this, new Observer<List<Building>>() {
            @Override
            public void onChanged(List<Building> buildings) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                if (buildings != null) {
                    adapter.setBuildings(buildings);
                    adapter.setOnItemClickListener(new BuildingAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Building building) {
                            BuildingsListActivity.this.usersDBRepository.currentBuilding = building.getAddress();
                            Intent intent = new Intent(BuildingsListActivity.this, ResidentsListActivity.class);
                            //Intent intent = new Intent(BuildingsListActivity.this, PostAnnouncementsActivity.class);
                            //Intent intent = new Intent(BuildingsListActivity.this, GlobalChatActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = this.binding.search;
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}