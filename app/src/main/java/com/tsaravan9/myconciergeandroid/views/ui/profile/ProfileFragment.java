package com.tsaravan9.myconciergeandroid.views.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.FragmentProfileBinding;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.BottomNavigationActivity;
import com.tsaravan9.myconciergeandroid.views.MainActivity;
import com.tsaravan9.myconciergeandroid.views.resident.ProfileActivity;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentProfileBinding binding;
    private UsersViewModel usersViewModel;
    private User loggedInUser;
    private SharedPreferences prefs;
    private Activity context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = this.getActivity();
        prefs = context.getApplicationContext().getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        this.usersViewModel = UsersViewModel.getInstance(this.context.getApplication());
        this.binding.logOut.setOnClickListener(this);
        this.binding.llcContactUsDisplay.setOnClickListener(this);
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
        String fullName = loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
        this.binding.fullNameDisplay.setText(fullName);
        this.binding.emailDisplay.setText(loggedInUser.getEmail());
        this.binding.mobileDisplay.setText("Mobile Number: " + loggedInUser.getMobileNumber());
        this.binding.addressDisplay.setText("Address:" + loggedInUser.getApartment() + " - " + loggedInUser.getAddress());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.logOut: {
                    LogOutClicked();
                    break;
                }
                case R.id.llcContactUsDisplay: {
                    Log.d("testContact", "here");
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + "+14166708067"));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        }
    }

    private void LogOutClicked() {
        if (prefs.contains("USER_EMAIL")) {
            prefs.edit().remove("USER_EMAIL").apply();
        }
        if (prefs.contains("USER_PASSWORD")) {
            prefs.edit().remove("USER_PASSWORD").apply();
        }
        Intent logOutIntent = new Intent(context, MainActivity.class);
        startActivity(logOutIntent);
    }
}