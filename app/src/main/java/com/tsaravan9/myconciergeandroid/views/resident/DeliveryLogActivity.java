package com.tsaravan9.myconciergeandroid.views.resident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBuildingsListBinding;
import com.tsaravan9.myconciergeandroid.databinding.DeliveryLogBinding;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.ActivityLogAdapter;
import com.tsaravan9.myconciergeandroid.views.BuildingAdapter;
import com.tsaravan9.myconciergeandroid.views.admin.BuildingsListActivity;
import com.tsaravan9.myconciergeandroid.views.admin.ResidentsListActivity;

import java.util.List;

public class DeliveryLogActivity extends AppCompatActivity {

    private DeliveryLogBinding binding;
    private UsersViewModel usersViewModel;
    private UsersDBRepository usersDBRepository;
    private ActivityLogAdapter adapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DeliveryLogBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        prefs = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);

        RecyclerView recyclerView = findViewById(R.id.activity_log_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ActivityLogAdapter();
        recyclerView.setAdapter(adapter);

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersDBRepository = usersViewModel.getUserRepository();
        usersDBRepository.loggedInUserEmail = prefs.getString("USER_EMAIL", "");
        usersViewModel.getAllDeliveries();
        usersViewModel.allDeliveries.observe(this, new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                if (deliveries != null) {
                    adapter.setDeliveries(deliveries);
                    binding.pbLoading.setVisibility(View.GONE);
                    binding.llcDeliveryLog.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
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