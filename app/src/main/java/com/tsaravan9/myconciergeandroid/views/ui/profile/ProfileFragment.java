package com.tsaravan9.myconciergeandroid.views.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tsaravan9.myconciergeandroid.databinding.FragmentProfileBinding;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private UsersViewModel usersViewModel;
    private User loggedInUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.usersViewModel = UsersViewModel.getInstance(this.getActivity().getApplication());
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
        String fullName = loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
        this.binding.textView3.setText(fullName);
        this.binding.textView4.setText(loggedInUser.getEmail());
        this.binding.textView2.setText("Mobile Number: " + loggedInUser.getMobileNumber() + "\n\nAddress:" + loggedInUser.getAddress());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}