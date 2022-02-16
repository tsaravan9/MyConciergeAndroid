package com.tsaravan9.myconciergeandroid.views.ui.acceptReject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.FragmentAcceptRejectBinding;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class AcceptRejectFragment extends Fragment implements View.OnClickListener{

    private FragmentAcceptRejectBinding binding;
    private final String TAG = this.getClass().getCanonicalName();
    private List<Delivery> allDeliveries;
    private UsersViewModel usersViewModel;
    private List<Delivery> visitors;
    private Delivery currentVisitor;
    private int current = -1;
    private SharedPreferences prefs;
    private boolean flag = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AcceptRejectViewModel acceptRejectViewModel =
                new ViewModelProvider(this).get(AcceptRejectViewModel.class);

        binding = FragmentAcceptRejectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        visitors = new ArrayList<>();
        prefs = this.getActivity().getApplicationContext().getSharedPreferences(this.getActivity().getPackageName(), this.getContext().MODE_PRIVATE);

        usersViewModel = UsersViewModel.getInstance(this.getActivity().getApplication());
        usersViewModel.getUserRepository().loggedInUserEmail = prefs.getString("USER_EMAIL", "");

        usersViewModel.getAllDeliveries();
        usersViewModel.allDeliveries.observe(getViewLifecycleOwner(), new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                if (deliveries != null && !deliveries.isEmpty()){
                    binding.imageView4.setOnClickListener(AcceptRejectFragment.this);
                    binding.imageView3.setOnClickListener(AcceptRejectFragment.this);
                    Log.d("deliveries2", deliveries.toString());
                    allDeliveries = deliveries;
                    filterForVisitors();
                    sort(visitors);
                    nextVisitor();
                }
                else{
                    binding.clAcceptReject.setVisibility(View.GONE);
                    binding.tvNoRequests.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.imageView4: {
                    Log.d(TAG, "onClick: Accept Button Clicked");
                    if (currentVisitor != null){
                        currentVisitor.setAllowed(true);
                        usersViewModel.updateDelivery(currentVisitor);
                        Toast.makeText(this.getActivity(), "Accepted!", Toast.LENGTH_LONG).show();
                        nextVisitor();
                    }
                    break;

                }
                case R.id.imageView3: {
                    Log.d(TAG, "onClick: Reject Button Clicked");
                    if (currentVisitor != null){
                        currentVisitor.setRejected(true);
                        usersViewModel.updateDelivery(currentVisitor);
                        Toast.makeText(this.getActivity(), "Rejected!", Toast.LENGTH_LONG).show();
                        nextVisitor();
                    }
                    break;
                }
            }
        }
    }

    private void filterForVisitors(){
        if (!flag){
            for (Delivery delivery : allDeliveries){
                if (delivery.getVisitor()){
                    if (delivery.isRejected() == false && delivery.isAllowed() == false){
                        visitors.add(delivery);
                    }
                }
            }
            flag = true;
        }
        Log.d("visitors", visitors.toString());
    }

    private void sort(List<Delivery> deliveries){
        deliveries.sort((o1, o2)
                -> o1.getEnteredAt().compareTo(
                o2.getEnteredAt()));
    }

    private void displayRequest(){
        this.binding.textView2.setText("You have a new visitor : " + currentVisitor.getName() + ". Please accept or deny.");
    }

    private void nextVisitor(){
        current += 1;
        Log.d("current Num", current + "");
        if (current >= visitors.size()){
            //no requests pending - UI
            this.binding.clAcceptReject.setVisibility(View.GONE);
            this.binding.tvNoRequests.setVisibility(View.VISIBLE);
            currentVisitor = null;
        }
        else{
            currentVisitor = visitors.get(current);
            Log.d("current", currentVisitor.getName());
            displayRequest();
        }

    }
}