package com.tsaravan9.myconciergeandroid.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tsaravan9.myconciergeandroid.models.Announcement;
import com.tsaravan9.myconciergeandroid.models.Building;
import com.tsaravan9.myconciergeandroid.models.Delivery;
import com.tsaravan9.myconciergeandroid.models.Text;
import com.tsaravan9.myconciergeandroid.models.User;
import com.tsaravan9.myconciergeandroid.repositories.UsersDBRepository;

import java.util.List;

public class UsersViewModel extends AndroidViewModel {
    private static UsersViewModel ourInstance;
    private final UsersDBRepository usersDBRepository = new UsersDBRepository();
    //    public MutableLiveData<List<User>> allUsers;
    public MutableLiveData<List<Building>> allBuildings;
    public MutableLiveData<List<User>> allResidents;
    public MutableLiveData<List<String>> allbuildingsList;

    public MutableLiveData<List<Text>> allTexts;

    public UsersViewModel(@NonNull Application application) {
        super(application);
    }

    public static UsersViewModel getInstance(Application application) {
        if (ourInstance == null) {
            ourInstance = new UsersViewModel(application);
        }
        return ourInstance;
    }

    public UsersDBRepository getUserRepository() {
        return this.usersDBRepository;
    }

    public void addUser(User newUser) throws Exception {
        this.usersDBRepository.addUser(newUser);
    }

    public void addPackage(Delivery newPackage) throws Exception {
        this.usersDBRepository.addPackage(newPackage);
    }

    public void addTextToChat(Text newText) throws Exception {
        this.usersDBRepository.addTextToChat(newText);
    }

    public void addAnnouncement(Announcement newAnnouncement) throws Exception {
        this.usersDBRepository.addAnnouncement(newAnnouncement);
    }

    public void getAllBuildings(){
        this.usersDBRepository.getAllBuildings();
        this.allBuildings = this.usersDBRepository.allBuildings;
    }

    public void getAllResidents() {
        this.usersDBRepository.getAllResidents();
        this.allResidents = this.usersDBRepository.allResidents;
    }

    public void getAllBuildingsList() throws Exception {
        this.usersDBRepository.getAllBuildingsList();
        this.allbuildingsList = this.usersDBRepository.allBuildingList;
    }
    
    public void getAllTexts(){
        this.usersDBRepository.getAllTexts();
        this.allTexts = this.usersDBRepository.allTexts;
    }

    public void searchUserByEmail(String email){
        this.usersDBRepository.searchUserByEmail(email);
    }

}
