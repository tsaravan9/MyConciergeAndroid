package com.tsaravan9.myconciergeandroid.views.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
        String fullName = loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
        this.binding.fullNameDisplay.setText(fullName);
        this.binding.emailDisplay.setText(loggedInUser.getEmail());
        this.binding.mobileDisplay.setText("Mobile Number: " + loggedInUser.getMobileNumber());
        this.binding.addressDisplay.setText("Address:" + loggedInUser.getApartment() + " - " + loggedInUser.getAddress());

        this.binding.llcContactUsDisplay.setOnClickListener(this);
        this.binding.fbShareClicked.setOnClickListener(this);
        this.binding.waShareClicked.setOnClickListener(this);
        this.binding.instaShareClicked.setOnClickListener(this);
        this.binding.logOut.setOnClickListener(this);

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
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + "+16475108066"));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.fbShareClicked: {
                    if (isAppInstalled(context, "com.facebook.orca") || isAppInstalled(context, "com.facebook.katana")
                            || isAppInstalled(context, "com.example.facebook") || isAppInstalled(context, "com.facebook.android")) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/{fb_page_numerical_id}")));
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/{fb_page_name}")));
                    }
                    break;
                }
                case R.id.waShareClicked: {
                    Toast.makeText(context, "Instagram", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.instaShareClicked: {
                    if (isAppInstalled(context.getApplicationContext(), "com.instagram.android")) {
                        Toast.makeText(context, "Instagram", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/{instagram_page_name}")));
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/{instagram_page_name}")));
                    }
                    break;
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

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}