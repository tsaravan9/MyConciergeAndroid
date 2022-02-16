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
import com.tsaravan9.myconciergeandroid.databinding.ActivityResidentsListBinding;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.BuildingAdapter;
import com.tsaravan9.myconciergeandroid.views.ResidentAdapter;

import java.util.List;

public class ResidentsListActivity extends AppCompatActivity {

    private ActivityResidentsListBinding binding;
    private UsersViewModel usersViewModel;
    private UsersDBRepository usersDBRepository;
    private ResidentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityResidentsListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.residents_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ResidentAdapter();
        recyclerView.setAdapter(adapter);

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersDBRepository = usersViewModel.getUserRepository();
        usersViewModel.getAllResidents();
        usersViewModel.allResidents.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> residents) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                if (residents != null){
                    adapter.setResidents(residents);
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

        adapter.setOnItemClickListener(new ResidentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User resident) {
                ResidentsListActivity.this.usersDBRepository.currentResident = resident.getEmail();
                Intent intent = new Intent(ResidentsListActivity.this, EnterPackageDetailsActivity.class);
                //put extras
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
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