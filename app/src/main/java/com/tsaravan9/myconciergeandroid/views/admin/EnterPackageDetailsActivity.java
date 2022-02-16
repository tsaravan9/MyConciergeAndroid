package com.tsaravan9.myconciergeandroid.views.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBuildingsListBinding;
import com.tsaravan9.myconciergeandroid.databinding.ActivityEnterPackageDetailsBinding;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.GlobalChatActivity;
import com.tsaravan9.myconciergeandroid.views.MainActivity;

public class EnterPackageDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityEnterPackageDetailsBinding binding;
    private UsersViewModel usersViewModel;
    private Boolean isVisitor = false;
    private SharedPreferences prefs;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityEnterPackageDetailsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        prefs = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        binding.visitorImg.setVisibility(View.GONE);
        this.binding.switchPackageVisitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!compoundButton.isChecked()) {
                    binding.tvPackageName.setText("Package Name");
                    binding.editPackageName.setHint("Enter Package Name");
                    binding.tvPackageDescription.setText("Package Description");
                    binding.editPackageDescription.setHint("Enter Package Details");
                    isVisitor = false;
                    binding.editPackageName.setText("");
                    binding.editPackageDescription.setText("");
                    binding.visitorImg.setVisibility(View.GONE);
                    binding.packageImg.setVisibility(View.VISIBLE);
                } else {
                    binding.tvPackageName.setText("Visitor Name");
                    binding.editPackageName.setHint("Enter Visitor's Name");
                    binding.tvPackageDescription.setText("Visitor Contact");
                    binding.editPackageDescription.setHint("Enter Visitor's Phone Number");
                    isVisitor = true;
                    binding.editPackageName.setText("");
                    binding.editPackageDescription.setText("");
                    binding.packageImg.setVisibility(View.GONE);
                    binding.visitorImg.setVisibility(View.VISIBLE);
                }
            }
        });

        this.binding.btnSubmitPackage.setOnClickListener(this);

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btn_submit_package: {
                    try {
                        this.validate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void validate() throws Exception {
        Boolean validData = true;
        String name = "";
        String description = "";

        if (this.binding.editPackageName.getText().toString().isEmpty()) {
            validData = false;
            this.binding.editPackageName.setError(this.binding.tvPackageName.getText().toString() + " Cannot Be Empty");
        } else {
            name = this.binding.editPackageName.getText().toString();
        }
        if (this.binding.editPackageDescription.getText().toString().isEmpty()) {
            validData = false;
            this.binding.editPackageDescription.setError(this.binding.tvPackageDescription.getText().toString() + " Cannot Be Empty");
        } else {
            description = this.binding.editPackageDescription.getText().toString();
        }

        if (validData) {
            Delivery delivery = new Delivery(name, description, this.isVisitor, System.currentTimeMillis());
            Log.d("delivery", delivery.toString());
            this.usersViewModel.addPackage(delivery);
            Toast.makeText(this, "Information Submitted!", Toast.LENGTH_SHORT).show();
            this.binding.editPackageName.setText("");
            this.binding.editPackageDescription.setText("");
        } else {
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void LogOutClicked() {
        if (prefs.contains("USER_EMAIL")) {
            prefs.edit().remove("USER_EMAIL").apply();
        }
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuth.signOut();
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
                Snackbar.make(binding.clPackageDetails, "Feature not implemented yet", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout:
                LogOutClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}