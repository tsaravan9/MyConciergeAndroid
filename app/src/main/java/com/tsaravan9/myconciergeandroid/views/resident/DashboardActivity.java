package com.tsaravan9.myconciergeandroid.views.resident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.tsaravan9.myconciergeandroid.R;
import com.tsaravan9.myconciergeandroid.databinding.ActivityPostAnnouncementsBinding;
import com.tsaravan9.myconciergeandroid.databinding.FragmentHomeBinding;
import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.Text;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.GlobalChatActivity;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private FragmentHomeBinding binding;
    private UsersViewModel usersViewModel;
    private String loggedInUserEmail;
    private SharedPreferences prefs;
    private User matchedUser;
    private UsersDBRepository usersDBRepository;
    private List<Announcement> savedAnnouncements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Dashboard", "got it");
        this.binding = FragmentHomeBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        prefs = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        loggedInUserEmail = prefs.getString("USER_EMAIL", "");
        usersViewModel.searchUserByEmail(loggedInUserEmail);

        usersViewModel = UsersViewModel.getInstance(this.getApplication());
        usersDBRepository = usersViewModel.getUserRepository();
        searchUserByEmail();

        usersDBRepository.currentBuilding = matchedUser.getAddress();
        usersViewModel.getAllAnnouncements();
        usersViewModel.allAnnouncements.observe(this, new Observer<List<Announcement>>() {
            @Override
            public void onChanged(List<Announcement> announcements) {
                //update RecyclerView Later
                //Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
                if (announcements != null){
                    savedAnnouncements = announcements;
                    sort(savedAnnouncements);
                }
            }
        });
    }

    //verify
    private void sort(List<Announcement> announcements){
        announcements.sort((o1, o2)
                -> o2.getPostedAt().compareTo(
                o1.getPostedAt()));
    }

    private void searchUserByEmail(){
        usersViewModel.searchUserByEmail(loggedInUserEmail);
        this.usersDBRepository.userFromDB.observe(DashboardActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                matchedUser = user;
            }
        });
    }


}