package com.tsaravan9.myconciergeandroid.views.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.FragmentHomeBinding;
import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.MainActivity;
import com.tsaravan9.myconciergeandroid.views.admin.BuildingsListActivity;
import com.tsaravan9.myconciergeandroid.views.resident.BookAmenityActivity;

import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private UsersViewModel usersViewModel;
    private User loggedInUser;
    private List<Announcement> announcements;
    private List<Delivery> deliveries;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (announcements != null){
            Log.d("announcements", announcements.toString());
        }
        this.usersViewModel = UsersViewModel.getInstance(this.getActivity().getApplication());
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
        String fullName = "Hi, " + loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
        this.binding.textView5.setText(fullName);
        getData();

        this.binding.swimmingImg.setOnClickListener(this);
        this.binding.gymImg.setOnClickListener(this);
        this.binding.snookerImg.setOnClickListener(this);

        return root;
        //TODO: test this below method to migrate the firebase calls to view models
//        homeViewModel.getAnnouncements().observe(getViewLifecycleOwner(), binding.textView5::setText);
    }

    private void getData(){
        usersViewModel.getUserRepository().currentBuilding = loggedInUser.getAddress();
        usersViewModel.getAllAnnouncements();
        usersViewModel.allAnnouncements.observe(getViewLifecycleOwner(), new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements2) {
                binding.textView11.setText("");
                announcements = announcements2;
                displayAnnouncements();
                usersViewModel.getAllDeliveries();
                usersViewModel.allDeliveries.observe(getViewLifecycleOwner(), new Observer<List<Delivery>>() {
                    @Override
                    public void onChanged(List<Delivery> deliveries2) {
                        binding.textView12.setText("");
                        deliveries = deliveries2;
                        displayDeliveries();
                    }
                });
            }
        });
    }

    private void displayAnnouncements(){
        for (Announcement announcement : announcements) {
            Log.d("testMe", announcement.toString());
            binding.textView11.append(announcement.getDescription());
        }
    }

    private void displayDeliveries(){
        for (Delivery delivery : deliveries) {
            binding.textView12.append(delivery.getDescription());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void bookAmenity() {
        Log.d("lol", "lol");
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.swimmingImg: {
                    Intent intent = new Intent(HomeFragment.this.getActivity(), BookAmenityActivity.class);
                    intent.putExtra("AMENITY_NAME", "SWIMMING");
                    startActivity(intent);
                    break;
                }
                case R.id.gymImg: {
                    Intent intent = new Intent(HomeFragment.this.getActivity(), BookAmenityActivity.class);
                    intent.putExtra("AMENITY_NAME", "GYM");
                    startActivity(intent);
                    break;
                }
                case R.id.snookerImg: {
                    Intent intent = new Intent(HomeFragment.this.getActivity(), BookAmenityActivity.class);
                    intent.putExtra("AMENITY_NAME", "SNOOKER");
                    startActivity(intent);
                    break;
                }
            }
        }
    }
}