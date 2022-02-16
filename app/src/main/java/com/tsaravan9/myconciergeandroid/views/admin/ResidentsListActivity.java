package com.tsaravan9.myconciergeandroid.views.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBuildingsListBinding;
import com.tsaravan9.myconciergeandroid.databinding.ActivityResidentsListBinding;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.BuildingAdapter;
import com.tsaravan9.myconciergeandroid.views.GlobalChatActivity;
import com.tsaravan9.myconciergeandroid.views.MainActivity;
import com.tsaravan9.myconciergeandroid.views.ResidentAdapter;

import java.util.List;

public class ResidentsListActivity extends AppCompatActivity {

    private ActivityResidentsListBinding binding;
    private UsersViewModel usersViewModel;
    private UsersDBRepository usersDBRepository;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityResidentsListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        prefs = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);

        RecyclerView recyclerView = findViewById(R.id.residents_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ResidentAdapter adapter = new ResidentAdapter();
        recyclerView.setAdapter(adapter);

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersDBRepository = usersViewModel.getUserRepository();
        usersViewModel.getAllResidents();
        usersViewModel.allResidents.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> residents) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                if (residents != null) {
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

    private void LogOutClicked() {
        if (prefs.contains("USER_EMAIL")) {
            prefs.edit().remove("USER_EMAIL").apply();
        }
        if (prefs.contains("USER_PASSWORD")) {
            prefs.edit().remove("USER_PASSWORD").apply();
        }
        Intent logOutIntent = new Intent(this, MainActivity.class);
        startActivity(logOutIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_resident_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_makeAnnouncements:
                Intent announcement = new Intent(this, PostAnnouncementsActivity.class);
                startActivity(announcement);
                return true;
            case R.id.menu_globalChat:
                //TODO: make new activity and embedd existing global chat fragment instead of activity.
                Intent globalChat = new Intent(this, GlobalChatActivity.class);
                startActivity(globalChat);
                return true;
            case R.id.menu_settings:
                //TODO: add changes later in future
                Snackbar.make(binding.clResList, "Feature not implemented yet", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout:
                LogOutClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}