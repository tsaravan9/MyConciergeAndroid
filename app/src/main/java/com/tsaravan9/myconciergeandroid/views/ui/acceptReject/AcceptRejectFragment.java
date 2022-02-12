package com.tsaravan9.myconciergeandroid.views.ui.acceptReject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tsaravan9.myconciergeandroid.databinding.FragmentAcceptRejectBinding;

public class AcceptRejectFragment extends Fragment {

    private FragmentAcceptRejectBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AcceptRejectViewModel acceptRejectViewModel =
                new ViewModelProvider(this).get(AcceptRejectViewModel.class);

        binding = FragmentAcceptRejectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textNotifications;
//        acceptRejectViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}