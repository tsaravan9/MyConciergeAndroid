package com.tsaravan9.myconciergeandroid.views.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tsaravan9.myconciergeandroid.databinding.FragmentHomeBinding;
import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private UsersViewModel usersViewModel;
    private User loggedInUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.usersViewModel = UsersViewModel.getInstance(this.getActivity().getApplication());
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
        String fullName = "Hi, " + loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
        this.binding.textView5.setText(fullName);
        getAnnouncements();
        getActivityLog();
        return root;
        //TODO: test this below method to migrate the firebase calls to view models
//        homeViewModel.getAnnouncements().observe(getViewLifecycleOwner(), binding.textView5::setText);
    }

    private void getAnnouncements() {
        binding.textView11.setText("");
        usersViewModel.getUserRepository().currentBuilding = loggedInUser.getAddress();
        usersViewModel.getAllAnnouncements();
        usersViewModel.allAnnouncements.observe(getViewLifecycleOwner(), new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                for (Announcement announcement : announcements) {
                    Log.d("testMe", announcement.toString());
                    binding.textView11.append(announcement.getDescription());
                }
            }
        });
    }

    private void getActivityLog() {
        binding.textView12.setText("");
        usersViewModel.getAllDeliveries();
        usersViewModel.allDeliveries.observe(getViewLifecycleOwner(), new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                for (Delivery delivery : deliveries) {
                    Log.d("testMe", delivery.toString());
                    if (delivery.getVisitor()) {
                        delivery.setAccepted(true);
                        usersViewModel.updateDelivery(delivery);
                    }
                    binding.textView12.append(delivery.getDescription());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}