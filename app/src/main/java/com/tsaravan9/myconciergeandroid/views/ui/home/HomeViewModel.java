package com.tsaravan9.myconciergeandroid.views.ui.home;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.viewmodels.UsersViewModel;
import com.tsaravan9.myconciergeandroid.views.BottomNavigationActivity;
import com.tsaravan9.myconciergeandroid.views.MainActivity;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    private UsersViewModel usersViewModel;
    private final User loggedInUser;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        usersViewModel = UsersViewModel.getInstance(application);
        loggedInUser = usersViewModel.getUserRepository().loggedInUser;
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

//    public LiveData<String> getAnnouncements() {
////        binding.textView11.setText("");
//        final String[] test = new String[1];
//        usersViewModel.getUserRepository().currentBuilding = loggedInUser.getAddress();
//        usersViewModel.getAllAnnouncements();
//        usersViewModel.allAnnouncements.observe((LifecycleOwner) this, new Observer<List<Announcement>>() {
//            @Override
//            public void onChanged(List<Announcement> announcements) {
//                for (Announcement announcement : announcements) {
//                    Log.d("testMe", announcement.toString());
////                    binding.textView11.append(announcement.getDescription());
//                    test[0] = announcement.getDescription();
//                }
//            }
//        });
//        return test[0];
//    }

}